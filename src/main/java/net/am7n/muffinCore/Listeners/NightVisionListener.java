// NightVisionListener.java
package net.am7n.muffinCore.Listeners;

import net.am7n.muffinCore.Commands.NightvisionCommand;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.entity.Player;

public class NightVisionListener implements Listener {

    private final NightvisionCommand command;

    public NightVisionListener(NightvisionCommand command) {
        this.command = command;
    }

    @EventHandler
    public void onDrinkMilk(PlayerItemConsumeEvent event) {
        if (event.getItem().getType() == Material.MILK_BUCKET) {
            command.reapplyIfEnabled(event.getPlayer());
        }
    }
}
