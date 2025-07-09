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
     *  2) Sends the “You were randomly teleported.” message and sound
     *  3) Applies the 60s cooldown
     *
     * If the player moves, cancels without teleport or cooldown.
     *
     * @param player    the player to teleport
     * @param worldName the world key for betterrtp
     * @param cooldowns shared map of UUID → cooldown-end-timestamp
     */
    public static void start(Player player, String worldName, Map<UUID, Long> cooldowns) {
        // Record the block-location at start for movement detection
        var startLoc = player.getLocation().getBlock().getLocation();
        final int[] timer = {5};

        GlobalRegionScheduler scheduler = Bukkit.getGlobalRegionScheduler();
        scheduler.runAtFixedRate(
                MuffinCore.getInstance(),

                // This lambda runs every period tick
                (ScheduledTask task) -> {
                    // If player moved, cancel everything
                    if (!player.getLocation().getBlock().getLocation().equals(startLoc)) {
                        player.sendActionBar(ChatColor.of("#FF0000") + "Teleport cancelled because you moved!");
                        player.playSound(player.getLocation(), Sound.ITEM_SHIELD_BREAK, 1f, 1f);
                        task.cancel();
                        return;
                    }

                    // Countdown expired?
                    if (timer[0] <= 0) {
                        // 1) Dispatch the console command on the main thread
                        Bukkit.getGlobalRegionScheduler().run(
                                MuffinCore.getInstance(),
                                (ScheduledTask __) -> Bukkit.dispatchCommand(
                                        Bukkit.getConsoleSender(),
                                        "betterrtp player " + player.getName() + " " + worldName
                                )
                        );

                        // 2) Notify player
                        player.sendActionBar(ChatColor.of("#8DFB08") + "You were randomly teleported.");
                        player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1f, 1f);

                        // 3) Apply cooldown
                        UUID uuid = player.getUniqueId();
                        cooldowns.put(uuid, System.currentTimeMillis() + 60000);

                        task.cancel();
                        return;
                    }

                    // Show action-bar and sound for this tick
                    player.sendActionBar(ChatColor.of("#8DFB08") + "Teleporting in " + timer[0] + " seconds...");
                    player.playSound(player.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 1f, 1f);
                    timer[0]--;
                },

                1L,   // initial delay (must be ≥1 tick)
                20L   // period (20 ticks = 1 second)
        );
    }
}
