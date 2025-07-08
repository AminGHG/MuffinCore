package net.am7n.muffinCore.Listeners;

import net.am7n.muffinCore.Commands.NightvisionCommand;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityResurrectEvent;

public class OnTotemPopListener implements Listener {

    private final NightvisionCommand command;

    public OnTotemPopListener(NightvisionCommand command) {
        this.command = command;
    }

    @EventHandler
    public void onTotemPop(EntityResurrectEvent event) {
        if (!(event.getEntity() instanceof Player player)) return;

        // Reapply night vision if enabled
        command.reapplyIfEnabled(player);
    }
}
