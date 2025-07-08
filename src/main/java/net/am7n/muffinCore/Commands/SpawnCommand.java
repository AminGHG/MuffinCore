package net.am7n.muffinCore.Commands;

import net.am7n.muffinCore.Tasks.TeleportTask;
import net.am7n.muffinCore.Utils.WorldDataUtil;
import org.bukkit.Sound;
import org.bukkit.command.*;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class SpawnCommand implements CommandExecutor {
    private final Plugin plugin;

    public SpawnCommand(Plugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) return true;
        TeleportTask.initiateTeleport(player, WorldDataUtil.getSpawn(), "spawn", plugin);
        return true;
    }
}
