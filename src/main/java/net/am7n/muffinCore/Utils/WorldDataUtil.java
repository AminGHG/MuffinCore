package net.am7n.muffinCore.Utils;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.UUID;

public class WorldDataUtil {
    private static final Location SPAWN_LOC = new Location(Bukkit.getWorld("spawn"), 0.5, 97, 0.5, 180, 0);
    private static final Location AFK_LOC = new Location(Bukkit.getWorld("afk"), 0.5, 50, 0.5);
    private static final Location Crates_LOC = new Location(Bukkit.getWorld("spawn"), 0.5, 97, -64.5, 180, 0);
    private static final HashMap<UUID, Location> playerWorldLocations = new HashMap<>();
// test
    public static Location getSpawn() {
        return SPAWN_LOC;
    }

    public static Location getAFK() {
        return AFK_LOC;
    }

    public static Location getCrates() {
        return Crates_LOC;
    }

    public static void storePlayerWorldLocation(Player player) {
        playerWorldLocations.put(player.getUniqueId(), player.getLocation());
    }

    public static boolean hasWorldLocation(Player player) {
        return playerWorldLocations.containsKey(player.getUniqueId());
    }

    public static Location getWorldLocation(Player player) {
        return playerWorldLocations.get(player.getUniqueId());
    }
}
