package net.am7n.muffinCore.Tasks;

import net.am7n.muffinCore.Utils.WorldDataUtil;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class TeleportTask {

    public static void initiateTeleport(Player player, Location target, String type, Plugin plugin) {
        Location start = player.getLocation().getBlock().getLocation();

        player.playSound(player.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 1f, 1f);
        player.sendActionBar("§x§8§D§F§B§0§8Teleporting in 5 seconds...");

        player.getScheduler().runDelayed(plugin, task ->
                loopTeleport(player, target, start, type, plugin, 5), null, 20L);
    }

    private static void loopTeleport(Player player, Location target, Location initialLoc, String type, Plugin plugin, int secondsLeft) {
        if (!player.getLocation().getBlock().getLocation().equals(initialLoc)) {
            player.sendActionBar("§x§f§f§0§0§0§0Teleport cancelled because you moved!");
            player.playSound(player.getLocation(), Sound.ITEM_SHIELD_BREAK, 1f, 1f);
            return;
        }

        if (secondsLeft <= 1) {
            WorldDataUtil.storePlayerWorldLocation(player);
            player.teleportAsync(target); // ✅ Folia-safe teleport
            player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1f, 1f);
            player.sendActionBar("§x§8§D§F§B§0§8Teleported to " + type + ".");
        } else {
            player.sendActionBar("§x§8§D§F§B§0§8Teleporting in " + (secondsLeft - 1) + " seconds...");
            player.playSound(player.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 1f, 1f);
            player.getScheduler().runDelayed(plugin, task ->
                    loopTeleport(player, target, initialLoc, type, plugin, secondsLeft - 1), null, 20L);
        }
    }
}
