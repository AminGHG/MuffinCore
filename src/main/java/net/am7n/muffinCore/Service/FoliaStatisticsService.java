package net.am7n.muffinCore.Service;

import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.Statistic;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

public class FoliaStatisticsService implements StatisticsService {

    private final Plugin plugin;
    private final File file;
    private final YamlConfiguration data;

    public FoliaStatisticsService(Plugin plugin) {
        this.plugin = plugin;
        // ← point at the data/ subfolder
        this.file = new File(plugin.getDataFolder(), "data/statistics.yml");
        this.data = YamlConfiguration.loadConfiguration(file);
    }


    private OfflinePlayer getOfflinePlayer(UUID uuid) {
        return Bukkit.getOfflinePlayer(uuid);
    }

    private void save() {
        try {
            data.save(file);
        } catch (IOException e) {
            plugin.getLogger().warning("Failed to save statistics.yml: " + e.getMessage());
        }
    }

    private String getPlaceholder(UUID uuid, String placeholder) {
        OfflinePlayer player = getOfflinePlayer(uuid);
        return PlaceholderAPI.setPlaceholders(player, placeholder);
    }

    private int getIntStat(UUID uuid, String key, String placeholder) {
        String value = getPlaceholder(uuid, placeholder);
        int parsed = parseInt(value);
        data.set(uuid + "." + key, parsed);
        save();
        return parsed;
    }

    private long getLongStat(UUID uuid, String key, String placeholder) {
        String value = getPlaceholder(uuid, placeholder);
        long parsed = parseLong(value);
        data.set(uuid + "." + key, parsed);
        save();
        return parsed;
    }

    private int parseInt(String value) {
        try {
            return Integer.parseInt(value.replaceAll("[^\\d]", ""));
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    private long parseLong(String value) {
        try {
            return Long.parseLong(value.replaceAll("[^\\d]", ""));
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    @Override
    public long getPlayTime(UUID uuid) {
        OfflinePlayer player = Bukkit.getOfflinePlayer(uuid);
        return player.getStatistic(Statistic.PLAY_ONE_MINUTE);
    }

    @Override
    public int getBlocksPlaced(UUID uuid) {
        return getIntStat(uuid, "blocks_placed", "%statistic_place_block%");
    }


    @Override
    public int getBlocksMined(UUID uuid) {
        return getIntStat(uuid, "blocks_mined", "%statistic_mine_block%");
    }

    @Override
    public int getPlayerKills(UUID uuid) {
        return getIntStat(uuid, "player_kills", "%statistic_player_kill%");
    }

    @Override
    public int getDeaths(UUID uuid) {
        return getIntStat(uuid, "deaths", "%statistic_deaths%");
    }

    @Override
    public int getMobKills(UUID uuid) {
        return getIntStat(uuid, "mob_kills", "%statistic_mob_kills%");
    }

    @Override
    public String getBalance(UUID uuid) {
        OfflinePlayer player = getOfflinePlayer(uuid);
        String result = PlaceholderAPI.setPlaceholders(player, "%vault_eco_balance_formatted%");
        data.set(uuid + ".balance", result);
        save();
        return result;
    }

    @Override
    public int getShards(UUID uuid) {
        data.set(uuid + ".shards", 0);
        save();
        return 0;
    }
}
