package net.am7n.muffinCore.GUI;

import net.am7n.muffinCore.Service.StatisticsService;
import net.am7n.muffinCore.Utils.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.concurrent.TimeUnit;

public class StatisticsGui {

    public static void open(Player viewer, String targetName,
                            java.util.UUID targetUUID,
                            StatisticsService stats) {
        String title = "              Statistics";
        Inventory inv = Bukkit.createInventory(null, 36, title);

        // 0: Playtime
        long playtimeTicks = stats.getPlayTime(targetUUID);
        String playtimeStr = formatTicks(playtimeTicks);
        inv.setItem(16, new ItemBuilder(org.bukkit.Material.CLOCK)
                .setName(ChatColor.YELLOW.toString() + ChatColor.BOLD.toString() + "PLAYTIME")
                .addLoreLine(ChatColor.WHITE + "Current playtime: " + ChatColor.GREEN + playtimeStr)
                .build());

        // 1: Blocks mined
        inv.setItem(13, new ItemBuilder(Material.DIAMOND_PICKAXE)
                .setName(ChatColor.BLUE.toString() + ChatColor.BOLD.toString() + "BLOCKS MINED")
                .addLoreLine(ChatColor.WHITE + "Current blocks mined: " + ChatColor.GREEN + String.valueOf(stats.getBlocksMined(targetUUID)))
                .build());

        // 2: Player kills
        inv.setItem(10, new ItemBuilder(org.bukkit.Material.DIAMOND_SWORD)
                .setName(ChatColor.RED.toString() + ChatColor.BOLD.toString() + "PLAYER KILLS")
                .addLoreLine(ChatColor.WHITE + "Current kills: " + ChatColor.GREEN + String.valueOf(stats.getPlayerKills(targetUUID)))
                .build());

        // 3: Deaths
        inv.setItem(11, new ItemBuilder(Material.SKELETON_SKULL)
                .setName(ChatColor.WHITE.toString() + ChatColor.BOLD.toString() + "DEATHS")
                .addLoreLine(ChatColor.WHITE + "Current deaths: " + ChatColor.GREEN + String.valueOf(stats.getDeaths(targetUUID)))
                .build());

        // 4: Mob kills
        inv.setItem(12, new ItemBuilder(Material.ZOMBIE_HEAD)
                .setName(ChatColor.DARK_GREEN.toString() + ChatColor.BOLD.toString() + "MOB KILLS")
                .addLoreLine(ChatColor.WHITE + "Current mob kills: " + ChatColor.GREEN + String.valueOf(stats.getMobKills(targetUUID)))
                .build());

        // 5: Balance
        inv.setItem(15, new ItemBuilder(org.bukkit.Material.GOLD_INGOT)
                .setName(ChatColor.GOLD.toString() + ChatColor.BOLD.toString() + "BALANCE")
                .addLoreLine(ChatColor.WHITE + "Current balance: " + ChatColor.GREEN + stats.getBalance(targetUUID))
                .build());

        // 6: Shards (hard‑coded 0)
        inv.setItem(22, new ItemBuilder(Material.AMETHYST_SHARD)
                .setName(ChatColor.DARK_PURPLE.toString() + ChatColor.BOLD.toString() + "SHARDS")
                .addLoreLine(ChatColor.WHITE + "Current shards: " + ChatColor.GREEN + "0")
                .build());

        // 7: Info arrow
        inv.setItem(31, new ItemBuilder(org.bukkit.Material.SPECTRAL_ARROW)
                .setName(ChatColor.YELLOW.toString() + ChatColor.BOLD.toString() + "STATISTICS")
                .addLoreLine(ChatColor.WHITE + "Here you can view your and other player's statistics!")
                .build());

        inv.setItem(14, new ItemBuilder(Material.DIRT)
                .setName(ChatColor.DARK_GRAY.toString() + ChatColor.BOLD.toString() + "BLOCKS PLACED")
                .addLoreLine(ChatColor.WHITE + "Current block placed: " + ChatColor.GREEN + String.valueOf(stats.getBlocksPlaced(targetUUID)))
                .build());

        viewer.openInventory(inv);
    }

    private static String formatTicks(long ticks) {
        // Assuming placeholder returns total PLAY_TICKS (1 tick = 1/20s)
        long totalSeconds = ticks / 20;
        long hours = TimeUnit.SECONDS.toHours(totalSeconds);
        long minutes = TimeUnit.SECONDS.toMinutes(totalSeconds) % 60;
        return String.format("%dh %02dm", hours, minutes);
    }
}
