package net.am7n.muffinCore.Commands;

import net.am7n.muffinCore.Utils.WorldDataUtil;
import org.bukkit.command.*;
import org.bukkit.entity.Player;

public class WorldCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) return true;

        if (player.getWorld().getName().equals("spawn") || player.getWorld().getName().equals("afk")) {
            if (WorldDataUtil.hasWorldLocation(player)) {
                player.teleport(WorldDataUtil.getWorldLocation(player));
                player.sendActionBar("§x§1§5§e§d§4§1Teleported you back into the world!");
                player.playSound(player.getLocation(), "entity.player.levelup", 1f, 1f);
            } else {
                player.sendActionBar("§x§f§f§0§0§0§0You weren't in the world yet.");
                player.playSound(player.getLocation(), "entity.villager.hurt", 1f, 1f);
            }
        } else {
            player.sendActionBar("§x§f§f§0§0§0§0You are already in the world!");
        }

        return true;
    }
}
