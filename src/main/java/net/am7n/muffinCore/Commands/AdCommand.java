package net.am7n.muffinCore.Commands;

import net.am7n.muffinCore.MuffinCore;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class AdCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        for (Player player : Bukkit.getOnlinePlayers()) {

            // First title immediately
            player.sendTitle("§7", "§x§F§C§7§9§0§0§l/store", 10, 60, 10);

            // Use Folia PlayerScheduler to delay the next title
            player.getScheduler().runDelayed(
                    MuffinCore.getInstance(),                      // Plugin
                    task -> player.sendTitle("§7", "§x§0§0§A§2§F§8§l/discord", 10, 60, 10), // Task
                    null,                                          // Executor (null = global)
                    60L                                            // Delay in ticks (3 seconds)
            );
        }

        return true;
    }
}
