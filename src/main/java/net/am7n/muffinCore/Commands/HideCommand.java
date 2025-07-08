package net.am7n.muffinCore.Commands;

import net.skinsrestorer.api.SkinsRestorer;
import net.skinsrestorer.api.exception.DataRequestException;
import net.skinsrestorer.api.exception.MineSkinException;
import net.skinsrestorer.api.property.InputDataResult;
import net.skinsrestorer.api.property.SkinProperty;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class HideCommand implements CommandExecutor {

    private final File dataFile;
    private final YamlConfiguration config;
    private final SkinsRestorer skinsRestorer;

    // UPDATED constructor to accept SkinsRestorer explicitly
    public HideCommand(File dataFolder, SkinsRestorer skinsRestorer) {
        this.dataFile = new File(dataFolder, "hide.yml");
        this.config = YamlConfiguration.loadConfiguration(dataFile);
        if (!config.contains("hide-enabled")) {
            config.set("hide-enabled", new ArrayList<String>());
            save();
        }
        this.skinsRestorer = skinsRestorer;
    }

    private void save() {
        try {
            config.save(dataFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private boolean isHidden(UUID uuid) {
        return config.getStringList("hide-enabled").contains(uuid.toString());
    }

    private void setHidden(UUID uuid, boolean state) {
        List<String> list = config.getStringList("hide-enabled");
        if (state) {
            if (!list.contains(uuid.toString())) list.add(uuid.toString());
        } else {
            list.remove(uuid.toString());
        }
        config.set("hide-enabled", list);
        save();
    }

    public void reapplyIfHidden(Player player) {
        if (!isHidden(player.getUniqueId())) return;

        if (!player.hasPermission("muffinsmp.command.hide")) {
            setHidden(player.getUniqueId(), false);
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(),
                    "tab player " + player.getName() + " tagprefix");
            return;
        }

        Bukkit.getScheduler().runTaskLater(JavaPlugin.getProvidingPlugin(getClass()), () ->
                        Bukkit.dispatchCommand(Bukkit.getConsoleSender(),
                                "tab player " + player.getName() + " tagprefix &k"),
                2L
        );
        applySkinAsync(player, "Steve");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command,
                             String label, String[] args) {
        if (!(sender instanceof Player player)) return true;

        if (!player.hasPermission("muffinsmp.command.hide")) {
            player.sendMessage("§cYou don't have permission to use this.");
            return true;
        }

        UUID uuid = player.getUniqueId();

        if (!isHidden(uuid)) {
            // Enable hide
            setHidden(uuid, true);
            player.sendMessage("§aYou are now hidden.");

            Bukkit.getGlobalRegionScheduler().execute(
                    JavaPlugin.getProvidingPlugin(getClass()),
                    () -> Bukkit.dispatchCommand(Bukkit.getConsoleSender(),
                            "tab player " + player.getName() + " tagprefix &k")
            );

            applySkinAsync(player, "Steve");

        } else {
            // Disable hide
            setHidden(uuid, false);
            player.sendMessage("§cYou are not hidden anymore.");

            Bukkit.getGlobalRegionScheduler().execute(
                    JavaPlugin.getProvidingPlugin(getClass()),
                    () -> Bukkit.dispatchCommand(Bukkit.getConsoleSender(),
                            "tab player " + player.getName() + " tagprefix")
            );

            restoreOriginalSkinAsync(player);
        }

        return true;
    }

    private void applySkinAsync(Player player, String skinName) {
        // FOLIA COMPATIBLE ASYNC SCHEDULING
        Bukkit.getGlobalRegionScheduler().execute(
                JavaPlugin.getProvidingPlugin(getClass()),
                () -> {
                    try {
                        Optional<InputDataResult> result = skinsRestorer
                                .getSkinStorage()
                                .findOrCreateSkinData(skinName);
                        if (result.isPresent()) {
                            SkinProperty skin = result.get().getProperty();
                            skinsRestorer.getSkinApplier(Player.class).applySkin(player, skin);
                        } else {
                            player.sendMessage("§cCould not load skin: " + skinName);
                        }
                    } catch (DataRequestException | MineSkinException e) {
                        player.sendMessage("§cError applying skin: " + e.getMessage());
                        e.printStackTrace();
                    }
                }
        );
    }

    private void restoreOriginalSkinAsync(Player player) {
        // FOLIA COMPATIBLE ASYNC SCHEDULING
        Bukkit.getGlobalRegionScheduler().execute(
                JavaPlugin.getProvidingPlugin(getClass()),
                () -> {
                    try {
                        skinsRestorer.getSkinApplier(Player.class).applySkin(player);
                    } catch (DataRequestException e) {
                        player.sendMessage("§cError restoring skin: " + e.getMessage());
                        e.printStackTrace();
                    }
                }
        );
    }
}
