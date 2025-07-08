// ToggleDeathMessageCommand.java
package net.am7n.muffinCore.Commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

public class ToggleDeathMessageCommand implements CommandExecutor {

    private final File dataFile;
    private final YamlConfiguration config;

    public ToggleDeathMessageCommand(File dataFolder) {
        if (!dataFolder.exists()) dataFolder.mkdirs();
        this.dataFile = new File(dataFolder, "deaths.yml");
        this.config = YamlConfiguration.loadConfiguration(dataFile);
        if (!config.contains("enabled")) {
            config.createSection("enabled");
            save();
        }
    }

    private void save() {
        try {
            config.save(dataFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player player)) return true;

        UUID uuid = player.getUniqueId();
        String key = "enabled." + uuid.toString();
        boolean current = config.getBoolean(key, true);
        config.set(key, !current);
        save();

        return true;
    }
}
