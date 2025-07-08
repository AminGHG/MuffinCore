package net.am7n.muffinCore.Commands;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.command.*;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class NightvisionCommand implements CommandExecutor {

    private final Set<UUID> nightVisionPlayers = new HashSet<>();
    private final File dataFolder;

    private File nightVisionFile;
    private FileConfiguration nightVisionConfig;

    private final PotionEffect NIGHT_VISION_EFFECT = new PotionEffect(
            PotionEffectType.NIGHT_VISION,
            Integer.MAX_VALUE,
            0,
            false,
            false
    );

    public NightvisionCommand(File dataFolder) {
        this.dataFolder = dataFolder;
        setupConfig();
        loadFromConfig();
    }

    private void setupConfig() {
        try {
            if (!dataFolder.exists()) {
                dataFolder.mkdirs();
            }
            nightVisionFile = new File(dataFolder, "nightvision.yml");
            if (!nightVisionFile.exists()) {
                nightVisionFile.createNewFile();
            }
            nightVisionConfig = YamlConfiguration.loadConfiguration(nightVisionFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void saveConfig() {
        try {
            nightVisionConfig.save(nightVisionFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadFromConfig() {
        List<String> uuids = nightVisionConfig.getStringList("nightvision-enabled");
        for (String uuidStr : uuids) {
            try {
                nightVisionPlayers.add(UUID.fromString(uuidStr));
            } catch (IllegalArgumentException ignored) {
            }
        }
    }

    private void saveToConfig() {
        List<String> uuidStrings = new ArrayList<>();
        for (UUID uuid : nightVisionPlayers) {
            uuidStrings.add(uuid.toString());
        }
        nightVisionConfig.set("nightvision-enabled", uuidStrings);
        saveConfig();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (!(sender instanceof Player player)) {
            sender.sendMessage("Only players can use this command.");
            return true;
        }

        UUID uuid = player.getUniqueId();

        if (!nightVisionPlayers.contains(uuid)) {
            nightVisionPlayers.add(uuid);
            saveToConfig();
            player.addPotionEffect(NIGHT_VISION_EFFECT);
            player.sendMessage(ChatColor.of("#8DFB08") + "You enabled Night vision.");
            player.sendActionBar(ChatColor.of("#8DFB08") + "You enabled night vision.");
        } else {
            nightVisionPlayers.remove(uuid);
            saveToConfig();
            player.removePotionEffect(PotionEffectType.NIGHT_VISION);
            player.sendMessage(ChatColor.of("#FF0000") + "You disabled night vision.");
            player.sendActionBar(ChatColor.of("#FF0000") + "You disabled Night vision.");
        }

        player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1f, 1f);
        return true;
    }

    public boolean isNightVisionEnabled(UUID uuid) {
        return nightVisionPlayers.contains(uuid);
    }

    public void reapplyIfEnabled(Player player) {
        if (isNightVisionEnabled(player.getUniqueId())) {
            player.getScheduler().runDelayed(
                    Bukkit.getPluginManager().getPlugin("MuffinCore"),
                    scheduled -> player.addPotionEffect(NIGHT_VISION_EFFECT),
                    null, // onCancel callback
                    20L
            );

        }
    }
    public void reapplyIfEnabledTotem(Player player) {
        if (isNightVisionEnabled(player.getUniqueId())) {
            player.getScheduler().runDelayed(
                    Bukkit.getPluginManager().getPlugin("MuffinCore"),
                    scheduled -> player.addPotionEffect(NIGHT_VISION_EFFECT),
                    null, // onCancel callback
                    1L
            );
        }
    }
}
