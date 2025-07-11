package net.am7n.muffinCore.Listeners;

import net.am7n.muffinCore.MuffinCore;
import net.am7n.muffinCore.Utils.WorldDataUtil;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class JoinListener implements Listener {

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        event.setJoinMessage(null);

        Player player = event.getPlayer();

        if (player.getWorld().getName().equals("spawn")) {
            player.getScheduler().runDelayed(
                    MuffinCore.getInstance(),                     // Plugin reference
                    scheduledTask -> {
                        player.teleportAsync(WorldDataUtil.getSpawn());
                    },
                    null,                                         // No cancel callback needed
                    2L                                            // Delay by 2 ticks for Folia safety
            );
        }
    }
}
