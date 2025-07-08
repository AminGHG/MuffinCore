package net.am7n.muffinCore.Listeners;

import net.am7n.muffinCore.Commands.HideCommand;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class HideListener implements Listener {

    private final HideCommand command;

    public HideListener(HideCommand command) {
        this.command = command;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        command.reapplyIfHidden(event.getPlayer());
    }
}
