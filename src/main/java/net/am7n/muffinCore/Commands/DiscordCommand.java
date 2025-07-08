package net.am7n.muffinCore.Commands;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class DiscordCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (!(sender instanceof Player)) {
            sender.sendMessage("Only players can use this command.");
            return true;
        }

        Player player = (Player) sender;

        player.sendMessage(ChatColor.of("#00A2F8") + "" + ChatColor.BOLD + "DISCORD SERVER");
        player.sendMessage("");
        player.sendMessage(ChatColor.GRAY + "Did you know that the " + ChatColor.of("#00A2F8") + "server" + ChatColor.GRAY + " has a " + ChatColor.of("#00A2F8") + "discord server?");
        player.sendMessage(ChatColor.GRAY + "You can participate in Giveaways &");
        player.sendMessage(ChatColor.GRAY + "support the " + ChatColor.of("#00A2F8") + "server");
        player.sendMessage("");
        player.sendMessage(ChatColor.of("#00A2F8") + "" + ChatColor.BOLD + "VISIT TODAY");
        player.sendMessage(ChatColor.of("#00A2F8") + "" + ChatColor.UNDERLINE + "https://discord.gg/u7TZNPxXZJ");

        // Actionbar message
        player.sendActionBar(ChatColor.GRAY + "Link message was sent in chat!");

        // Play sound
        player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1f, 2f);

        return true;
    }
}
