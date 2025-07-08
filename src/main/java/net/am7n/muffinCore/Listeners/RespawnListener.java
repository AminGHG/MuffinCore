package net.am7n.muffinCore.Listeners;

import net.am7n.muffinCore.Utils.WorldDataUtil;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerRespawnEvent;

public class RespawnListener implements Listener {
    @EventHandler
    public void onRespawn(PlayerRespawnEvent event) {
        event.setRespawnLocation(WorldDataUtil.getSpawn());
    }
}
