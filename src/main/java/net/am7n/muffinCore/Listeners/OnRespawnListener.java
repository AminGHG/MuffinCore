package net.am7n.muffinCore.Listeners;

import net.am7n.muffinCore.Commands.NightvisionCommand;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerRespawnEvent;

public class OnRespawnListener implements Listener {

    private final NightvisionCommand command;

    public OnRespawnListener(NightvisionCommand command) {
        this.command = command;
    }

    @EventHandler
    public void onRespawn(PlayerRespawnEvent event) {
        Player player = event.getPlayer();
        command.reapplyIfEnabled(player);
    }
}
