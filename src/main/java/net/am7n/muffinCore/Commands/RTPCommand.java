package net.am7n.muffinCore.Commands;

import net.am7n.muffinCore.GUI.RTPGui;
import net.am7n.muffinCore.Listeners.RTPListener;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Sound;
import org.bukkit.command.*;
import org.bukkit.entity.Player;

public class RTPCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) return false;

        if (args.length == 0) {
            RTPGui.openRTPGUI(player);
            return true;
        }

        String worldName = switch (args[0].toLowerCase()) {
            case "overworld" -> "world";
            case "nether"    -> "world_nether";
            case "end"       -> "world_the_end";
            default           -> null;
        };

        if (worldName == null) {
            player.sendMessage(ChatColor.of("#FF0000") + "You need to select a valid world.");
            player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_HURT, 1, 1);
            return true;
        }

        // Use the same cooldown map as the GUI
        net.am7n.muffinCore.Utils.RTPUtils.rtpWithCooldown(player, worldName, RTPListener.getCooldownMap());
        return true;
    }
}
