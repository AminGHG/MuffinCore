package net.am7n.muffinCore.Utils;

import net.am7n.muffinCore.MuffinCore;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.entity.Player;

import java.util.Map;
import java.util.UUID;

public class RTPUtils {

    /**
     * Starts a random-teleport sequence with a 5s countdown.
     * Cooldown & command dispatch are handled by TeleportTimer only if the player completes the timer.
     *
     * @param player    the player to teleport
     * @param worldName the world key for betterrtp
     * @param cooldowns shared map of UUID → cooldown-end-timestamp
     */
    public static void rtpWithCooldown(Player player, String worldName, Map<UUID, Long> cooldowns) {
        UUID uuid = player.getUniqueId();
        long now = System.currentTimeMillis();

        // 1) Check cooldown
        if (cooldowns.containsKey(uuid) && now < cooldowns.get(uuid)) {
            long remaining = (cooldowns.get(uuid) - now) / 1000;
            player.sendMessage(ChatColor.of("#FF0000")
                    + "You need to wait " + remaining + " seconds until you can random teleport again");
            player.playSound(player.getLocation(), org.bukkit.Sound.ENTITY_VILLAGER_HURT, 1f, 1f);
            player.closeInventory();
            return;
        }

        // 2) Close GUI and hand off to the countdown timer
        player.closeInventory();
        net.am7n.muffinCore.Tasks.TeleportTimer.start(player, worldName, cooldowns);
    }
}
