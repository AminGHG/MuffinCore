package net.am7n.muffinCore.Listeners;

import net.am7n.muffinCore.Utils.WorldDataUtil;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

public class DamageListener implements Listener {
    @EventHandler
    public void onDamage(EntityDamageEvent event) {
        if (!(event.getEntity() instanceof Player player)) return;

        // Check if event is not cancelled and actual damage > 0
        if (event.isCancelled() || event.getFinalDamage() <= 0) return;

        String worldName = player.getWorld().getName();

        if (worldName.equalsIgnoreCase("spawn")) {
            player.teleportAsync(WorldDataUtil.getSpawn());
        } else if (worldName.equalsIgnoreCase("afk")) {
            player.teleportAsync(WorldDataUtil.getAFK());
        }
    }
}
