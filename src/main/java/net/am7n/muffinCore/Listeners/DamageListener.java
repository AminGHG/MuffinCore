package net.am7n.muffinCore.Listeners;

import net.am7n.muffinCore.Utils.WorldDataUtil;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.entity.Player;

public class DamageListener implements Listener {
    @EventHandler
    public void onDamage(EntityDamageEvent event) {
        if (!(event.getEntity() instanceof Player player)) return;

        String worldName = player.getWorld().getName();
        if (worldName.equals("spawn")) {
            player.teleportAsync(WorldDataUtil.getSpawn());
            event.setCancelled(true);
        } else if (worldName.equals("afk")) {
            player.teleportAsync(WorldDataUtil.getAFK());
            event.setCancelled(true);
        }
    }
}
