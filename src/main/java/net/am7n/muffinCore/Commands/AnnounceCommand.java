package net.am7n.muffinCore.Commands;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class AnnounceCommand implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        if (!sender.hasPermission("muffinsmp.command.AnnounceCommand")) {
            sender.sendMessage("§cYou don't have permission to use this command.");
            return true;
        }

        if (args.length == 0) {
            sender.sendMessage("§cWrong Usage");
            if (sender instanceof Player player) {
                player.sendActionBar("§cWrong Usage");
            }
            return true;
        }

        String message = String.join(" ", args);

        for (Player player : Bukkit.getOnlinePlayers()) {
            player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_BIT, 1f, 1f);
            player.sendTitle("§x§0§0§A§2§F§8§lANNOUNCEMENT", "§f" + message, 20, 60, 20);
            player.sendMessage("");
            player.sendMessage("§x§0§0§A§2§F§8§lANNOUNCEMENT");
            player.sendMessage("§f" + message);
            player.sendMessage("");
        }

        return true;
    }
}
