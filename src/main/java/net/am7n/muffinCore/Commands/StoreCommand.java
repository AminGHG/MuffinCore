package net.am7n.muffinCore.Commands;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class StoreCommand implements CommandExecutor {

    private final String storeLink = "https://store.muffinsmp.uk";

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (!(sender instanceof Player)) {
            sender.sendMessage("Only players can use this command.");
            return true;
        }

        Player player = (Player) sender;

        // Close inventory (if needed)
        player.closeInventory();

        // Messages
        player.sendMessage(ChatColor.of("#FC7900") + "" + ChatColor.BOLD + "SERVER STORE");
        player.sendMessage("");
        player.sendMessage(ChatColor.GRAY + "Did you know this " + ChatColor.of("#FC7900") + "server" + ChatColor.GRAY + " has a " + ChatColor.of("#FC7900") + "Store" + ChatColor.GRAY + "?");
        player.sendMessage(ChatColor.GRAY + "You can buy ranks &");
        player.sendMessage(ChatColor.GRAY + "support the " + ChatColor.of("#FC7900") + "server" + ChatColor.GRAY + "!");
        player.sendMessage("");
        player.sendMessage(ChatColor.of("#FC7900") + "" + ChatColor.BOLD + "VISIT TODAY!");
        player.sendMessage(ChatColor.of("#FC7900") + "" + ChatColor.UNDERLINE + storeLink);

        // Actionbar
        player.sendActionBar(ChatColor.GRAY + "Link message has been sent in chat!");

        // Sound
        player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1f, 2f);

        return true;
    }
}
