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

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.net.URL;

public final class MuffinCore extends JavaPlugin {

    private static MuffinCore instance;
    private SkinsRestorer skinsRestorer;
    private final String allowedIp = "62.72.177.7";

    @Override
    public void onEnable() {
        instance = this;

        try {
            String publicIp = getPublicIp();
            getLogger().info("Public IP detected: " + publicIp);

            if (!publicIp.trim().equals(allowedIp)) {
                getLogger().severe("This plugin is not allowed to run on IP: " + publicIp);
                getServer().getPluginManager().disablePlugin(this);
                return;
            }

            // Proceed with plugin enable
            continueEnable();

        } catch (Exception e) {
            getLogger().severe("Failed to retrieve public IP: " + e.getMessage());
            getServer().getPluginManager().disablePlugin(this);
        }
    }

    private String getPublicIp() throws Exception {
        URL url = new URL("https://api.ipify.org");
        try (BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()))) {
            return in.readLine();
        }
    }

    private void continueEnable() {
        getLogger().info("MuffinCore enabling...");

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

        skinsRestorer = SkinsRestorerProvider.get();
        getLogger().info("SkinsRestorer API initialized.");

        getLogger().info("Starting ad loops for all online players...");
        startAdLoopForAllPlayers();

        Bukkit.getPluginManager().registerEvents(new Listener() {
            @EventHandler
            public void onJoin(PlayerJoinEvent event) {
                getLogger().info("Player joined: " + event.getPlayer().getName() + ", scheduling ad loop.");
                scheduleAdLoop(event.getPlayer());
            }
        }, this);
        getLogger().info("Player join listener for ad scheduling registered.");

        this.getCommand("ad").setExecutor(new AdCommand());
        getLogger().info("Command /ad registered.");

        this.getCommand("discord").setExecutor(new DiscordCommand());
        getLogger().info("Command /discord registered.");

        this.getCommand("store").setExecutor(new StoreCommand());
        getLogger().info("Command /store registered.");

        this.getCommand("announce").setExecutor(new AnnounceCommand());
        getLogger().info("Command /announce registered.");

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

        File dataFolder = new File(getDataFolder(), "data");
        if (!dataFolder.exists()) {
            dataFolder.mkdirs();
            getLogger().info("Data folder created at " + dataFolder.getAbsolutePath());
        } else {
            getLogger().info("Data folder exists at " + dataFolder.getAbsolutePath());
        }

        NightvisionCommand nightVisionCmd = new NightvisionCommand(dataFolder);
        getCommand("nightvision").setExecutor(nightVisionCmd);
        getLogger().info("Command /nightvision registered.");

        getServer().getPluginManager().registerEvents(new NightVisionListener(nightVisionCmd), this);
        getLogger().info("NightVisionListener loaded.");

        getServer().getPluginManager().registerEvents(new OnTotemPopListener(nightVisionCmd), this);
        getLogger().info("OnTotemPopListener loaded.");

        getServer().getPluginManager().registerEvents(new OnDeathListener(nightVisionCmd), this);
        getLogger().info("OnRespawnListener loaded.");

        getCommand("rtp").setExecutor(new RTPCommand());
        getCommand("rtp").setTabCompleter(new RTPTabCompleter());
        getServer().getPluginManager().registerEvents(new RTPListener(), this);

        HideCommand hideCmd = new HideCommand(dataFolder, skinsRestorer);
        getCommand("hide").setExecutor(hideCmd);
        getLogger().info("Command /hide registered.");

        getServer().getPluginManager().registerEvents(new HideListener(hideCmd), this);
        getLogger().info("HideListener loaded.");

        getServer().getPluginManager().registerEvents(new DeathListener(dataFolder), this);
        getLogger().info("DeathListener loaded.");

        getCommand("toggledeathmessage").setExecutor(new ToggleDeathMessageCommand(dataFolder));
        getLogger().info("Command /toggledeathmessage registered.");
    }

    @Override
    public void onDisable() {
        getLogger().info("MuffinCore disabling...");
    }

    public static MuffinCore getInstance() {
        return instance;
    }

    public SkinsRestorer getSkinsRestorer() {
        return skinsRestorer;
    }

    private void startAdLoopForAllPlayers() {
        getLogger().info("Scheduling ad loops for all online players...");
        for (Player player : Bukkit.getOnlinePlayers()) {
            getLogger().info("Scheduling ad loop for player " + player.getName());
            scheduleAdLoop(player);
        }
    }

    private void scheduleAdLoop(Player player) {
        long delay = 20L * 60 * 20;

        player.getScheduler().runDelayed(this, task -> {
            getLogger().info("Sending first ad title to player " + player.getName());
            player.sendTitle("§7", "§x§F§C§7§9§0§0§l/store", 10, 60, 10);

            player.getScheduler().runDelayed(this, secondTask -> {
                getLogger().info("Sending second ad title to player " + player.getName());
                player.sendTitle("§7", "§x§0§0§A§2§F§8§l/discord", 10, 60, 10);
            }, null, 60L);

            scheduleAdLoop(player);
        }, null, delay);
    }
}
