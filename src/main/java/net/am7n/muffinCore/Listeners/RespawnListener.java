package net.am7n.muffinCore.Listeners;

import net.am7n.muffinCore.MuffinCore;
import net.am7n.muffinCore.Utils.WorldDataUtil;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemStack;

public class RespawnListener implements Listener {

    @EventHandler
    public void onRespawn(PlayerRespawnEvent event) {
        Player player = event.getPlayer();

        // Schedule teleport and gear give after player fully respawns
        player.getScheduler().runDelayed(MuffinCore.getInstance(), task -> {
            // Teleport player to spawn
            player.teleportAsync(WorldDataUtil.getSpawn());

            // Give starter gear
            player.getInventory().addItem(
                    new ItemStack(Material.COOKED_BEEF, 16),
                    new ItemStack(Material.CHAINMAIL_BOOTS),
                    new ItemStack(Material.CHAINMAIL_LEGGINGS),
                    new ItemStack(Material.CHAINMAIL_CHESTPLATE),
                    new ItemStack(Material.CHAINMAIL_HELMET),
                    new ItemStack(Material.IRON_SWORD),
                    new ItemStack(Material.IRON_SHOVEL),
                    new ItemStack(Material.IRON_AXE)
            );
        }, null, 20L); // Delay ~1 second to ensure full respawn
    }
}
