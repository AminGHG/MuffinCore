package net.am7n.muffinCore.Utils;

import net.am7n.muffinCore.MuffinCore;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import io.papermc.paper.threadedregions.scheduler.GlobalRegionScheduler;

import java.util.Map;
import java.util.UUID;

public class RTPUtils {

    /**
     * Initiates a random teleport: immediately dispatches the console command,
     * then starts a 5‑second visuals-and-movement-check timer. Only if the
     * player remains still for 5s is the cooldown applied and the success
     * message/sound shown.
     */
    public static void rtpWithCooldown(Player player, String worldName, Map<UUID, Long> cooldowns) {
        UUID uuid = player.getUniqueId();
        long now = System.currentTimeMillis();

        // 1) Cooldown check
        if (cooldowns.containsKey(uuid) && now < cooldowns.get(uuid)) {
            long rem = (cooldowns.get(uuid) - now) / 1000;
            player.sendMessage(ChatColor.of("#FF0000")
                    + "You need to wait " + rem + " seconds until you can random teleport again");
            player.playSound(player.getLocation(), org.bukkit.Sound.ENTITY_VILLAGER_HURT, 1f, 1f);
            player.closeInventory();
            return;
        }

        // 2) Close the GUI
        player.closeInventory();

        // 3) Dispatch the betterrtp console command immediately (on main thread)
        GlobalRegionScheduler scheduler = Bukkit.getGlobalRegionScheduler();
        scheduler.run(MuffinCore.getInstance(), __ -> {
            Bukkit.dispatchCommand(
                    Bukkit.getConsoleSender(),
                    "betterrtp player " + player.getName() + " " + worldName
            );
        });

        // 4) Start the 5s countdown; pass the same cooldown map and uuid so
        //    that TeleportTimer can apply cooldown only if the countdown succeeds
        net.am7n.muffinCore.Tasks.TeleportTimer.start(player, cooldowns, uuid);
    }
}
