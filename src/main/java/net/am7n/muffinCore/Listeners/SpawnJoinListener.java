package net.am7n.muffinCore.Listeners;

import net.am7n.muffinCore.Utils.WorldDataUtil;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class SpawnJoinListener implements Listener {
    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        if (event.getPlayer().getWorld().getName().equals("spawn")) {
            event.getPlayer().teleportAsync(WorldDataUtil.getSpawn());
        }
    }
}
