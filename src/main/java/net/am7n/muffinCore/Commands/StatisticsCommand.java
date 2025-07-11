package net.am7n.muffinCore.Commands;

import net.am7n.muffinCore.GUI.StatisticsGui;
import net.am7n.muffinCore.Service.StatisticsService;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

public class StatisticsCommand implements CommandExecutor {

    private final StatisticsService statsService;

    public StatisticsCommand(StatisticsService statsService) {
        this.statsService = statsService;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        // Only players can open the GUI
        if (!(sender instanceof Player viewer)) {
            sender.sendMessage("Only players can use this command.");
            return true;
        }

        OfflinePlayer target;
        if (args.length == 0) {
            target = viewer;
        } else {
            target = Bukkit.getOfflinePlayerIfCached(args[0]);
            if (target == null) {
                target = Bukkit.getOfflinePlayer(args[0]);
            }
            if (target == null || target.getName() == null) {
                viewer.sendMessage("Player not found.");
                return true;
            }
        }

        UUID targetUUID = target.getUniqueId();

        // Open the GUI (it will pull from statsService internally)
        StatisticsGui.open(viewer, target.getName(), targetUUID, statsService);
        return true;
    }
}
