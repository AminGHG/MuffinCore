package net.am7n.muffinCore.GUI;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.*;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Collections;

public class RTPGui {

    public static void openRTPGUI(Player player) {
        Inventory gui = Bukkit.createInventory(null, 27, "Random Teleport");

        gui.setItem(11, createItem(Material.GRASS_BLOCK,
                ChatColor.of("#8DFB08") + "§lOVERWORLD",
                ChatColor.WHITE + "Click to random teleport in the overworld!"));
        gui.setItem(13, createItem(Material.NETHERRACK,
                ChatColor.of("#FF0000") + "§lNETHER",
                ChatColor.WHITE + "Click to random teleport in the nether!"));
        gui.setItem(15, createItem(Material.END_STONE,
                ChatColor.of("#f5d89e") + "§lEND",
                ChatColor.WHITE + "Click to random teleport in the end!"));

        player.openInventory(gui);
    }

    private static ItemStack createItem(Material mat, String name, String lore) {
        ItemStack item = new ItemStack(mat);
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(name);
            meta.setLore(Collections.singletonList(lore));
            item.setItemMeta(meta);
        }
        return item;
    }
}
