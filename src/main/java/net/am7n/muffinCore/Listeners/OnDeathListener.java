package net.am7n.muffinCore.Listeners;

import net.am7n.muffinCore.Commands.NightvisionCommand;
import net.am7n.muffinCore.MuffinCore;
import net.am7n.muffinCore.Utils.WorldDataUtil;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;

public class OnDeathListener implements Listener {

    private final NightvisionCommand command;

    public OnDeathListener(NightvisionCommand command) {
        this.command = command;
    }

    @EventHandler
    public void onRespawn(PlayerDeathEvent event) {
        Player player = event.getPlayer();
        Location spawn = WorldDataUtil.getSpawn();
        player.teleportAsync(spawn);

        // Delay the giving of items by 1 tick using Folia PlayerScheduler
        player.getScheduler().runDelayed(
                MuffinCore.getInstance(), // plugin instance
                task -> {

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
                    command.reapplyIfEnabled(player);


                },
                null, // executor, null means global executor
                1L    // delay in ticks
        );
    }
}
