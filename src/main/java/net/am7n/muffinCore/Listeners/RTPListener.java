package net.am7n.muffinCore.Listeners;

import net.am7n.muffinCore.Utils.RTPUtils;
import org.bukkit.entity.Player;
import org.bukkit.event.*;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.*;

public class RTPListener implements Listener {

    // Single shared cooldown map
    private static final Map<UUID, Long> cooldowns = new HashMap<>();

    public static Map<UUID, Long> getCooldownMap() {
        return cooldowns;
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent evt) {
        if (!(evt.getWhoClicked() instanceof Player player)) return;
        if (!"Random Teleport".equals(evt.getView().getTitle())) return;

        evt.setCancelled(true);
        int slot = evt.getRawSlot();

        String worldName = switch (slot) {
            case 11 -> "world";
            case 13 -> "world_nether";
            case 15 -> "world_the_end";
            default -> null;
        };

        if (worldName != null) {
            // same cooldown logic as typed command
            RTPUtils.rtpWithCooldown(player, worldName, cooldowns);
        }
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent evt) {
        // reset cooldown on join
        cooldowns.remove(evt.getPlayer().getUniqueId());
    }
}
