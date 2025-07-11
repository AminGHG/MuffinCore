package net.am7n.muffinCore.Listeners;

import net.am7n.muffinCore.GUI.StatisticsGuiHolder;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

public class StatisticsGuiListener implements Listener {

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (event.getInventory().getHolder() instanceof StatisticsGuiHolder) {
            event.setCancelled(true); // Prevent item movement
        }
    }
}
