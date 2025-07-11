package net.am7n.muffinCore.Service;

import java.util.UUID;

public interface StatisticsService {
    long getPlayTime(UUID uuid);
    int getBlocksPlaced(UUID uuid);
    int getBlocksMined(UUID uuid);
    int getPlayerKills(UUID uuid);
    int getDeaths(UUID uuid);
    int getMobKills(UUID uuid);
    String getBalance(UUID uuid);
    int getShards(UUID uuid);
}
