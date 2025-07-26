package net.am7n.muffinCore.GUI;

import net.am7n.muffinCore.Service.StatisticsService;
import net.am7n.muffinCore.Utils.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class StatisticsGui {

    public static void open(Player viewer, String targetName,
                            UUID targetUUID,
                            StatisticsService stats) {
        String title = "              Statistics";
        Inventory inv = Bukkit.createInventory(new StatisticsGuiHolder(), 36, title);

        long playtimeTicks = stats.getPlayTime(targetUUID);
        String playtimeStr = formatTicks(playtimeTicks);
        inv.setItem(16, new ItemBuilder(Material.CLOCK)
                .setName(ChatColor.YELLOW + "" + ChatColor.BOLD + "PLAYTIME")
                .addLoreLine(ChatColor.WHITE + "Current playtime: " + ChatColor.GREEN + playtimeStr)
                .build());

        inv.setItem(13, new ItemBuilder(Material.DIAMOND_PICKAXE)
                .setName(ChatColor.BLUE + "" + ChatColor.BOLD + "BLOCKS MINED")
                .addLoreLine(ChatColor.WHITE + "Current blocks mined: " + ChatColor.GREEN + stats.getBlocksMined(targetUUID))
                .build());

        inv.setItem(10, new ItemBuilder(Material.DIAMOND_SWORD)
                .setName(ChatColor.RED + "" + ChatColor.BOLD + "PLAYER KILLS")
                .addLoreLine(ChatColor.WHITE + "Current kills: " + ChatColor.GREEN + stats.getPlayerKills(targetUUID))
                .build());

        inv.setItem(11, new ItemBuilder(Material.SKELETON_SKULL)
                .setName(ChatColor.WHITE + "" + ChatColor.BOLD + "DEATHS")
                .addLoreLine(ChatColor.WHITE + "Current deaths: " + ChatColor.GREEN + stats.getDeaths(targetUUID))
                .build());

        inv.setItem(12, new ItemBuilder(Material.ZOMBIE_HEAD)
                .setName(ChatColor.DARK_GREEN + "" + ChatColor.BOLD + "MOB KILLS")
                .addLoreLine(ChatColor.WHITE + "Current mob kills: " + ChatColor.GREEN + stats.getMobKills(targetUUID))
                .build());

        inv.setItem(15, new ItemBuilder(Material.GOLD_INGOT)
                .setName(ChatColor.GOLD + "" + ChatColor.BOLD + "BALANCE")
                .addLoreLine(ChatColor.WHITE + "Current balance: " + ChatColor.GREEN + stats.getBalance(targetUUID))
                .build());

        inv.setItem(22, new ItemBuilder(Material.AMETHYST_SHARD)
                .setName(ChatColor.DARK_PURPLE + "" + ChatColor.BOLD + "SHARDS")
                .addLoreLine(ChatColor.WHITE + "Current shards: " + ChatColor.GREEN + "0")
                .build());

        inv.setItem(31, new ItemBuilder(Material.SPECTRAL_ARROW)
                .setName(ChatColor.YELLOW + "" + ChatColor.BOLD + "STATISTICS")
                .addLoreLine(ChatColor.WHITE + "Here you can view your and other player's statistics!")
                .build());

        inv.setItem(14, new ItemBuilder(Material.DIRT)
                .setName(ChatColor.DARK_GRAY + "" + ChatColor.BOLD + "BLOCKS PLACED")
                .addLoreLine(ChatColor.WHITE + "Current block placed: " + ChatColor.GREEN + stats.getBlocksPlaced(targetUUID))
                .build());

        viewer.openInventory(inv);
    }

    private static String formatTicks(long ticks) {
        long totalSeconds = ticks / 20;
        long hours = TimeUnit.SECONDS.toHours(totalSeconds);
        long minutes = TimeUnit.SECONDS.toMinutes(totalSeconds) % 60;
        return String.format("%dh %02dm", hours, minutes);
    }
}
