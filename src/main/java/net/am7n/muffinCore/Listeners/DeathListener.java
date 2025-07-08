// DeathListener.java
package net.am7n.muffinCore.Listeners;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.entity.EntityDamageEvent;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

public class DeathListener implements Listener {
    private final File toggleFile;
    private final File locFile;
    private final YamlConfiguration locConfig;

    public DeathListener(File dataFolder) {
        if (!dataFolder.exists()) dataFolder.mkdirs();
        this.toggleFile = new File(dataFolder, "deaths.yml");
        this.locFile = new File(dataFolder, "locations.yml");
        this.locConfig = YamlConfiguration.loadConfiguration(locFile);
    }

    private boolean deathMessagesEnabled(UUID uuid) {
        // Reload config so toggles take effect immediately
        YamlConfiguration toggleConfig = YamlConfiguration.loadConfiguration(toggleFile);
        return toggleConfig.getBoolean("enabled." + uuid.toString(), true);
    }

    private void saveLocations(List<String> entries) {
        locConfig.set("deaths", entries);
        try {
            locConfig.save(locFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player victim = event.getEntity();
        UUID vicId = victim.getUniqueId();

        // Record death location
        Location loc = victim.getLocation();
        World world = loc.getWorld();
        String entry = String.format("%s, %s, %s, %d, %d, %d",
                victim.getName(), vicId,
                world != null ? world.getName() : "unknown",
                (int) Math.ceil(loc.getX()),
                (int) Math.ceil(loc.getY()),
                (int) Math.ceil(loc.getZ())
        );
        List<String> deaths = locConfig.getStringList("deaths");
        deaths.add(entry);
        saveLocations(deaths);

        // Always suppress the vanilla death message
        event.setDeathMessage(null);

        // If toggled off, skip custom broadcast
        if (!deathMessagesEnabled(vicId)) return;

        // Build coordinates
        int x = (int) Math.ceil(loc.getX());
        int y = (int) Math.ceil(loc.getY());
        int z = (int) Math.ceil(loc.getZ());
        String base = String.format("§7☠ %s §7died at %d, %d, %d", victim.getName(), x, y, z);

        String custom = base;
        EntityDamageEvent.DamageCause cause = victim.getLastDamageCause() != null
                ? victim.getLastDamageCause().getCause()
                : null;
        Player killer = victim.getKiller();

        if (cause != null) {
            switch (cause) {
                case ENTITY_ATTACK:
                    if (killer != null) custom = String.format(
                            "§7☠ %s §7was slain by %s at %d, %d, %d",
                            victim.getName(), killer.getName(), x, y, z);
                    break;
                case PROJECTILE:
                    if (killer != null) custom = String.format(
                            "§7☠ %s §7was shot by %s at %d, %d, %d",
                            victim.getName(), killer.getName(), x, y, z);
                    break;
                case MAGIC:
                    custom = String.format(
                            "§7☠ %s §7was killed by magic at %d, %d, %d",
                            victim.getName(), x, y, z);
                    break;
                case FALL:
                    custom = String.format(
                            "§7☠ %s §7died to fall damage at %d, %d, %d",
                            victim.getName(), x, y, z);
                    break;
                case LAVA:
                    custom = String.format(
                            "§7☠ %s §7burned at %d, %d, %d",
                            victim.getName(), x, y, z);
                    break;
                case SUFFOCATION:
                    custom = String.format(
                            "§7☠ %s §7suffocated in a wall at %d, %d, %d",
                            victim.getName(), x, y, z);
                    break;
                case DROWNING:
                    custom = String.format(
                            "§7☠ %s §7drowned at %d, %d, %d",
                            victim.getName(), x, y, z);
                    break;
                case STARVATION:
                    custom = String.format(
                            "§7☠ %s §7starved at %d, %d, %d",
                            victim.getName(), x, y, z);
                    break;
                case LIGHTNING:
                    custom = String.format(
                            "§7☠ %s §7was struck by lightning at %d, %d, %d",
                            victim.getName(), x, y, z);
                    break;
                case BLOCK_EXPLOSION:
                case ENTITY_EXPLOSION:
                    if (killer != null) custom = String.format(
                            "§7☠ %s §7got blown up by %s at %d, %d, %d",
                            victim.getName(), killer.getName(), x, y, z);
                    else custom = String.format(
                            "§7☠ %s §7died to an explosion at %d, %d, %d",
                            victim.getName(), x, y, z);
                    break;
                case VOID:
                    custom = String.format(
                            "§7☠ %s §7fell out of the world at %d, %d, %d",
                            victim.getName(), x, y, z);
                    break;
                case CONTACT:
                    custom = String.format(
                            "§7☠ %s §7died due to contact at %d, %d, %d",
                            victim.getName(), x, y, z);
                    break;
                case HOT_FLOOR:
                    custom = String.format(
                            "§7☠ %s §7stood on a hot floor at %d, %d, %d",
                            victim.getName(), x, y, z);
                    break;
                case WITHER:
                    custom = String.format(
                            "§7☠ %s §7withered away at %d, %d, %d",
                            victim.getName(), x, y, z);
                    break;
                case POISON:
                    custom = String.format(
                            "§7☠ %s §7was poisoned at %d, %d, %d",
                            victim.getName(), x, y, z);
                    break;
                default:
                    // use base
            }
        }

        // Broadcast only to players with messages enabled
        for (Player p : Bukkit.getOnlinePlayers()) {
            if (deathMessagesEnabled(p.getUniqueId())) {
                p.sendMessage(custom);
            }
        }
    }
}
