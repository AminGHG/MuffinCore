package net.am7n.muffinCore;

import net.am7n.muffinCore.Commands.*;
import net.am7n.muffinCore.Listeners.*;
import net.am7n.muffinCore.Tasks.RTPTabCompleter;
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
        getLogger().info("MuffinCore enabling...");

        // === Teleport Commands & Listeners ===
        getCommand("spawn").setExecutor(new SpawnCommand(this));
        getLogger().info("Command /spawn registered.");

        getCommand("startchat").setExecutor(new StartChatCommand());
        getLogger().info("Command /startchat registered.");

        getCommand("afk").setExecutor(new AFKCommand(this));
        getLogger().info("Command /afk registered.");

        getCommand("world").setExecutor(new WorldCommand());
        getLogger().info("Command /world registered.");

        getCommand("crates").setExecutor(new CratesCommand(this));
        getLogger().info("Command /crates registered.");

        getServer().getPluginManager().registerEvents(new DamageListener(), this);
        getLogger().info("DamageListener loaded.");

        // Initialize SkinsRestorer API
        skinsRestorer = SkinsRestorerProvider.get();
        getLogger().info("SkinsRestorer API initialized.");

        // Start AdCommand loop for all online players
        getLogger().info("Starting ad loops for all online players...");
        startAdLoopForAllPlayers();

        // Schedule ads for players who join later
        Bukkit.getPluginManager().registerEvents(new Listener() {
            @EventHandler
            public void onJoin(PlayerJoinEvent event) {
                getLogger().info("Player joined: " + event.getPlayer().getName() + ", scheduling ad loop.");
                scheduleAdLoop(event.getPlayer());
            }
        }, this);
        getLogger().info("Player join listener for ad scheduling registered.");

        // Register existing commands
        this.getCommand("ad").setExecutor(new AdCommand());
        getLogger().info("Command /ad registered.");

        this.getCommand("discord").setExecutor(new DiscordCommand());
        getLogger().info("Command /discord registered.");

        this.getCommand("store").setExecutor(new StoreCommand());
        getLogger().info("Command /store registered.");

        this.getCommand("announce").setExecutor(new AnnounceCommand());
        getLogger().info("Command /announce registered.");

        // Register core listeners
        getServer().getPluginManager().registerEvents(new JoinListener(), this);
        getLogger().info("JoinListener loaded.");

        getServer().getPluginManager().registerEvents(new LeaveListener(), this);
        getLogger().info("LeaveListener loaded.");

        getServer().getPluginManager().registerEvents(
                new NetherRoofRedstoneBlockerListener(this),
                this
        );
        getLogger().info("NetherRoofRedstoneBlockerListener loaded.");

        getServer().getPluginManager().registerEvents(new ExplosionListener(), this);
        getLogger().info("ExplosionListener loaded.");

        // === Custom Data Folder for deaths/messages/etc. ===
        File dataFolder = new File(getDataFolder(), "data");
        if (!dataFolder.exists()) {
            dataFolder.mkdirs();
            getLogger().info("Data folder created at " + dataFolder.getAbsolutePath());
        } else {
            getLogger().info("Data folder exists at " + dataFolder.getAbsolutePath());
        }

        // Night vision command + listeners
        NightvisionCommand nightVisionCmd = new NightvisionCommand(dataFolder);
        getCommand("nightvision").setExecutor(nightVisionCmd);
        getLogger().info("Command /nightvision registered.");

        getServer().getPluginManager().registerEvents(new NightVisionListener(nightVisionCmd), this);
        getLogger().info("NightVisionListener loaded.");

        getServer().getPluginManager().registerEvents(new OnTotemPopListener(nightVisionCmd), this);
        getLogger().info("OnTotemPopListener loaded.");

        getServer().getPluginManager().registerEvents(new OnDeathListener(nightVisionCmd), this);
        getLogger().info("OnRespawnListener loaded.");

        // RTP command + listener
        instance = this;

        getCommand("rtp").setExecutor(new RTPCommand());
        getCommand("rtp").setTabCompleter(new RTPTabCompleter());
        getServer().getPluginManager().registerEvents(new RTPListener(), this);

        // Hide command + listener
        HideCommand hideCmd = new HideCommand(dataFolder, skinsRestorer);
        getCommand("hide").setExecutor(hideCmd);
        getLogger().info("Command /hide registered.");

        getServer().getPluginManager().registerEvents(new HideListener(hideCmd), this);
        getLogger().info("HideListener loaded.");

        // Death Toggle Command + Listener
        getServer().getPluginManager().registerEvents(new DeathListener(dataFolder), this);
        getLogger().info("DeathListener loaded.");

        getCommand("toggledeathmessage").setExecutor(new ToggleDeathMessageCommand(dataFolder));
        getLogger().info("Command /toggledeathmessage registered.");
    }

    @Override
    public void onDisable() {
        getLogger().info("MuffinCore disabling...");
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
        getLogger().info("Scheduling ad loops for all online players...");
        for (Player player : Bukkit.getOnlinePlayers()) {
            getLogger().info("Scheduling ad loop for player " + player.getName());
            scheduleAdLoop(player);
        }
    }

    private void scheduleAdLoop(Player player) {
        long delay = 20L * 60 * 20; // 20 minutes in ticks
        getLogger().info("Scheduling ad loop for player " + player.getName() + " with delay " + delay + " ticks.");

        player.getScheduler().runDelayed(this, task -> {
            getLogger().info("Sending first ad title to player " + player.getName());
            player.sendTitle("§7", "§x§F§C§7§9§0§0§l/store", 10, 60, 10);

            player.getScheduler().runDelayed(this, secondTask -> {
                getLogger().info("Sending second ad title to player " + player.getName());
                player.sendTitle("§7", "§x§0§0§A§2§F§8§l/discord", 10, 60, 10);
            }, null, 60L); // 3 seconds later

            // Repeat the loop
            scheduleAdLoop(player);

        }, null, delay);
    }
}
