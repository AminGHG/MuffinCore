package net.am7n.muffinCore.Listeners;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockExplodeEvent;
import org.bukkit.event.entity.EntityExplodeEvent;

import java.util.Iterator;
import java.util.List;

public class ExplosionListener implements Listener {

    /**
     * Prevents chests and hoppers from being destroyed by any entity-caused explosion
     * (e.g. creepers, beds, respawn anchors).
     */
    @EventHandler
    public void onEntityExplode(EntityExplodeEvent event) {
        removeContainers(event.blockList());
    }

    /**
     * Prevents chests and hoppers from being destroyed by block-caused explosions
     * (e.g. TNT, respawn anchors overcharged).
     */
    @EventHandler
    public void onBlockExplode(BlockExplodeEvent event) {
        removeContainers(event.blockList());
    }

    /**
     * Common logic to strip chests and hoppers out of the explosion’s affected blocks.
     */
    private void removeContainers(List<Block> blocks) {
        Iterator<Block> it = blocks.iterator();
        while (it.hasNext()) {
            Material type = it.next().getType();
            if (type == Material.CHEST || type == Material.HOPPER) {
                it.remove();
            }
        }
    }
}
