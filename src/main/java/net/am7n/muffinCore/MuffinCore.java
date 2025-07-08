package net.am7n.muffinCore;

import net.am7n.muffinCore.Commands.*;
import net.am7n.muffinCore.Listeners.*;
import net.skinsrestorer.api.SkinsRestorer;
import net.skinsrestorer.api.SkinsRestorerProvider;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public final class MuffinCore extends JavaPlugin {

    private static MuffinCore instance;
    private SkinsRestorer skinsRestorer;


    @Override
    public void onEnable() {
        instance = this;

        // === Teleport Commands & Listeners ===
        getCommand("spawn").setExecutor(new SpawnCommand(this));
        getCommand("afk").setExecutor(new AFKCommand(this));
        getCommand("world").setExecutor(new WorldCommand());
        getCommand("crates").setExecutor(new CratesCommand(this));
        getServer().getPluginManager().registerEvents(new RespawnListener(), this);
        getServer().getPluginManager().registerEvents(new SpawnJoinListener(), this);
        getServer().getPluginManager().registerEvents(new DamageListener(), this);


        // Initialize SkinsRestorer API
        skinsRestorer = SkinsRestorerProvider.get();

        // Start AdCommand loop for all online players
        startAdLoopForAllPlayers();

        // Schedule ads for players who join later
        Bukkit.getPluginManager().registerEvents(new Listener() {
            @EventHandler
            public void onJoin(PlayerJoinEvent event) {
                scheduleAdLoop(event.getPlayer());
            }
        }, this);

        // Register existing commands
        this.getCommand("ad").setExecutor(new AdCommand());
        this.getCommand("discord").setExecutor(new DiscordCommand());
        this.getCommand("store").setExecutor(new StoreCommand());
        this.getCommand("announce").setExecutor(new AnnounceCommand());

        // Register core listeners
        getServer().getPluginManager().registerEvents(new JoinListener(), this);
        getServer().getPluginManager().registerEvents(new LeaveListener(), this);
        getServer().getPluginManager().registerEvents(
                new NetherRoofRedstoneBlockerListener(this),
                this
        );
        getServer().getPluginManager().registerEvents(new ExplosionListener(), this);

        // === Custom Data Folder for deaths/messages/etc. ===
        File dataFolder = new File(getDataFolder(), "data");
        if (!dataFolder.exists()) dataFolder.mkdirs();

        // Night vision command + listeners
        NightvisionCommand nightVisionCmd = new NightvisionCommand(dataFolder);
        getCommand("nightvision").setExecutor(nightVisionCmd);
        getServer().getPluginManager().registerEvents(new NightVisionListener(nightVisionCmd), this);
        getServer().getPluginManager().registerEvents(new OnTotemPopListener(nightVisionCmd), this);
        getServer().getPluginManager().registerEvents(new OnRespawnListener(nightVisionCmd), this);

        // Hide command + listener
        HideCommand hideCmd = new HideCommand(dataFolder, skinsRestorer);
        getCommand("hide").setExecutor(hideCmd);
        getServer().getPluginManager().registerEvents(new HideListener(hideCmd), this);

        // Death Toggle Command + Listener
        getServer().getPluginManager().registerEvents(new DeathListener(dataFolder), this);
        getCommand("toggledeathmessage").setExecutor(new ToggleDeathMessageCommand(dataFolder));
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public static MuffinCore getInstance() {
        return instance;
    }

    public SkinsRestorer getSkinsRestorer() {
        return skinsRestorer;
    }

    // ======= Ad Broadcasting Loop (Folia-safe) =======

    private void startAdLoopForAllPlayers() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            scheduleAdLoop(player);
        }
    }

    private void scheduleAdLoop(Player player) {
        long delay = 20L * 60 * 20; // 20 minutes in ticks

        player.getScheduler().runDelayed(this, task -> {
            player.sendTitle("§7", "§x§F§C§7§9§0§0§l/store", 10, 60, 10);

            player.getScheduler().runDelayed(this, secondTask -> {
                player.sendTitle("§7", "§x§0§0§A§2§F§8§l/discord", 10, 60, 10);
            }, null, 60L); // 3 seconds later

            // Repeat the loop
            scheduleAdLoop(player);

        }, null, delay);
    }
}
