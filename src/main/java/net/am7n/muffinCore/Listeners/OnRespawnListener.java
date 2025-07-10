package net.am7n.muffinCore.Listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerRespawnEvent;

import static net.am7n.muffinCore.Utils.WorldDataUtil.getSpawn;

public class OnRespawnListener implements Listener {

    @EventHandler
    public void onPlayerRespawn(PlayerRespawnEvent event) {
        Player player = event.getPlayer();

        // Force the respawn location
        event.setRespawnLocation(getSpawn());

        player.teleportAsync(getSpawn());


    }
}

