package net.am7n.muffinCore.Tasks;

import net.am7n.muffinCore.MuffinCore;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import io.papermc.paper.threadedregions.scheduler.GlobalRegionScheduler;
import io.papermc.paper.threadedregions.scheduler.ScheduledTask;

import java.util.Map;
import java.util.UUID;

public class TeleportTimer {

    /**
     * Runs a 5‑second Folia‑safe countdown. If the player moves, cancels
     * with no cooldown or message. If they stand still, applies their
     * cooldown and shows the “You were randomly teleported.” message/sound.
     *
     * @param player    the player who initiated the teleport
     * @param cooldowns shared cooldown map
     * @param uuid      the player’s UUID (key into that map)
     */
    public static void start(Player player, Map<UUID, Long> cooldowns, UUID uuid) {
        var startLoc = player.getLocation().getBlock().getLocation();
        final int[] timer = {5};

        GlobalRegionScheduler scheduler = Bukkit.getGlobalRegionScheduler();
        scheduler.runAtFixedRate(
                MuffinCore.getInstance(),

                (ScheduledTask task) -> {
                    // Cancel on movement
                    if (!player.getLocation().getBlock().getLocation().equals(startLoc)) {
                        player.sendMessage(ChatColor.of("#FF0000") + "Teleport cancelled because you moved!");
                        player.playSound(player.getLocation(), Sound.ITEM_SHIELD_BREAK, 1f, 1f);
                        task.cancel();
                        return;
                    }

                    // Countdown ended?
                    if (timer[0] <= 0) {
                        // 1) Apply cooldown immediately
                        cooldowns.put(uuid, System.currentTimeMillis() + 60000);

                        // 2) Notify player
                        player.sendMessage(ChatColor.of("#8DFB08") + "You were randomly teleported.");
                        player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1f, 1f);

                        task.cancel();
                        return;
                    }

                    // Show action‑bar + sound tick
                    player.sendActionBar(ChatColor.of("#8DFB08") + "Teleporting in " + timer[0] + " seconds...");
                    player.playSound(player.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 1f, 1f);
                    timer[0]--;
                },

                1L,   // initial delay (≥1 tick)
                20L   // repeat every 20 ticks (1s)
        );
    }
}
