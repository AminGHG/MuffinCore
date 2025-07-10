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
     * Runs a 5-second Folia-safe countdown. If the player stays still, then:
     *  1) Dispatches the betterrtp console command
     *  2) Applies the 60s cooldown
     *  3) Notifies the player & plays the level-up sound
     *
     * If the player moves, cancels with shield-break sound and no cooldown applied.
     *
     * @param player    the player to teleport
     * @param worldName the world key for betterrtp
     * @param cooldowns shared map of UUID → cooldown-end-timestamp
     */
    public static void start(Player player, String worldName, Map<UUID, Long> cooldowns) {
        var startLoc = player.getLocation().getBlock().getLocation();
        final int[] timer = {5};

        GlobalRegionScheduler scheduler = Bukkit.getGlobalRegionScheduler();
        scheduler.runAtFixedRate(
                MuffinCore.getInstance(),

                // This runs every second (20 ticks) after an initial 1-tick delay
                (ScheduledTask task) -> {
                    // 1) Cancel if moved
                    if (!player.getLocation().getBlock().getLocation().equals(startLoc)) {
                        player.sendActionBar(ChatColor.of("#FF0000") + "Teleport cancelled because you moved!");
                        player.playSound(player.getLocation(), Sound.ITEM_SHIELD_BREAK, 1f, 1f);
                        task.cancel();
                        return;
                    }

                    // 2) Countdown finished?
                    if (timer[0] <= 0) {
                        UUID uuid = player.getUniqueId();
                        long expiry = System.currentTimeMillis() + 60000;

                        // Dispatch command & set cooldown together on the main thread
                        scheduler.run(
                                MuffinCore.getInstance(),
                                (ScheduledTask __) -> {
                                    Bukkit.dispatchCommand(
                                            Bukkit.getConsoleSender(),
                                            "betterrtp player " + player.getName() + " " + worldName
                                    );
                                    cooldowns.put(uuid, expiry);
                                }
                        );

                        // Notify player & play sound
                        player.sendActionBar(ChatColor.of("#8DFB08") + "You were randomly teleported.");
                        player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1f, 1f);

                        task.cancel();
                        return;
                    }

                    // 3) Still counting down
                    player.sendActionBar(ChatColor.of("#8DFB08") + "Teleporting in " + timer[0] + " seconds...");
                    player.playSound(player.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 1f, 1f);
                    timer[0]--;
                },

                1L,  // initial delay (must be ≥1 tick)
                20L  // period (20 ticks = 1s)
        );
    }
}
