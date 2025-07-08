package net.am7n.muffinCore.Commands;

import net.am7n.muffinCore.Tasks.TeleportTask;
import net.am7n.muffinCore.Utils.WorldDataUtil;
import org.bukkit.command.*;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class AFKCommand implements CommandExecutor {
    private final Plugin plugin;

    public AFKCommand(Plugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) return true;
        TeleportTask.initiateTeleport(player, WorldDataUtil.getAFK(), "afk", plugin);
        return true;
    }
}
