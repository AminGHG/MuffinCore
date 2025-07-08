package net.am7n.muffinCore.Listeners;

import net.am7n.muffinCore.Commands.NightvisionCommand;
import net.am7n.muffinCore.MuffinCore;
import net.am7n.muffinCore.Utils.WorldDataUtil;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemStack;

public class OnRespawnListener implements Listener {

    private final NightvisionCommand command;

    public OnRespawnListener(NightvisionCommand command) {
        this.command = command;
    }

    @EventHandler
    public void onRespawn(PlayerRespawnEvent event) {
        Player player = event.getPlayer();
        Location spawn = WorldDataUtil.getSpawn();

        // Setze immer einen Respawn-Standort (WorldDataUtil liefert nie null)
        event.setRespawnLocation(spawn);

        // Verzögerte Ausführung auf dem Haupt-Thread (Folia-safe)
        Bukkit.getScheduler().runTaskLater(MuffinCore.getInstance(), () -> {
            // Teleport asynchron, damit es Folia-konform bleibt
            player.teleportAsync(spawn);

            // Starter-Kit geben
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

            // Nightvision wieder anwenden, falls aktiviert
            command.reapplyIfEnabled(player);
        }, 20L); // 20 Ticks = 1 Sekunde
    }
}
