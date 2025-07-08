package net.am7n.muffinCore.Listeners;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.Sound;
import org.bukkit.World.Environment;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.EnumSet;
import java.util.Set;

public class NetherRoofRedstoneBlockerListener implements Listener {

    private static final Set<Material> BLOCKED_COMPONENTS = EnumSet.of(
            Material.REDSTONE_WIRE,
            Material.REPEATER,
            Material.COMPARATOR,
            Material.PISTON,
            Material.STICKY_PISTON,
            Material.OBSERVER,
            Material.RAIL,
            Material.POWERED_RAIL,
            Material.DETECTOR_RAIL,
            Material.ACTIVATOR_RAIL
    );

    private final JavaPlugin plugin;

    public NetherRoofRedstoneBlockerListener(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        Player player = event.getPlayer();
        Block block = event.getBlockPlaced();
        World world = block.getWorld();

        // Only run in the Nether dimension
        if (world.getEnvironment() != Environment.NETHER) return;

        // Only blocks above the roof threshold
        if (block.getY() <= 127) return;

        // If it’s a blocked redstone component, cancel and notify
        if (BLOCKED_COMPONENTS.contains(block.getType())) {
            event.setCancelled(true);
            player.spigot().sendMessage(
                    ChatMessageType.ACTION_BAR,
                    TextComponent.fromLegacyText("§cYou can't place any redstone components on the nether roof.")
            );
            player.playSound(
                    player.getLocation(),
                    Sound.ENTITY_VILLAGER_NO,
                    1.0f,
                    1.0f
            );
        }
    }
}
