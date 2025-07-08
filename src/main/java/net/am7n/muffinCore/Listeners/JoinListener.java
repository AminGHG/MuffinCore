package net.am7n.muffinCore.Listeners;

import net.am7n.muffinCore.Utils.WorldDataUtil;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class JoinListener implements Listener {

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        // Prevent the default join message from showing
        event.setJoinMessage(null);

        if (event.getPlayer().getWorld().getName().equals("spawn")) {
            event.getPlayer().teleportAsync(WorldDataUtil.getSpawn());
        }
    }
}
