package net.am7n.muffinCore.Commands;

import net.am7n.muffinCore.MuffinCore;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class StartChatCommand implements CommandExecutor {

    private final Random random = ThreadLocalRandom.current();

    // Mix of some real-style usernames + generated ones for variety
    private final List<String> usernames = new ArrayList<>(500);

    private static final String[] BASE_PHRASES = {
            "almost done my base",
            "tpa 4 1v1",
            "tpa 4 free loot",
            "tpa 4 team",
            "lf group",
            "who wanna trade?",
            "need help w/ farm",
            "sellin diamonds, pm",
            "any1 near spawn?",
            "let's build lol",
            "bedrock ppl here",
            "any invites 2 factions?",
            "minin trip soon",
            "need redstone, trade?",
            "who got food?",
            "come raid me!",
            "lookin 4 fight",
            "free potions here",
            "tradin ench books",
            "team up?"
    };

    private static final String[] NAME_PARTS = {
            "Red", "Stone", "Craft", "Mine", "Block", "Build", "Iron", "Diamond", "Wolf", "Shadow",
            "Ghost", "Fire", "Ice", "Dark", "Light", "Sky", "Storm", "Dragon", "Magic", "Lucky",
            "Fast", "Epic", "Silent", "Crazy", "Ghost", "Wolf", "Ninja", "Knight", "Wizard", "Hunter"
    };

    // Add some real usernames manually
    private static final String[] REAL_USERNAMES = {
            "Amin2000", "PhilClan", "Alex", "Steve", "Herobrine", "Notch", "Creeper", "Enderman", "Zombie", "Skeleton",
            "DiamondDigger", "CraftySteve", "MinerMike", "PixelPro", "NetherNinja", "BlockBuilder", "RedstoneRick",
            "LapisLad", "IronMan", "TheWolf", "SkyWalker", "DragonSlayer", "MagicMage", "LuckyLuke", "SilentShadow",
            "CrazyCat", "GhostGamer", "NinjaNick", "KnightRider", "WizardWalt", "HunterHank"
    };

    public StartChatCommand() {
        generateUsernames();
    }

    private void generateUsernames() {
        // Add real usernames first
        for (String realName : REAL_USERNAMES) {
            usernames.add(realName);
        }

        // Fill rest to 500 with generated usernames
        while (usernames.size() < 500) {
            int partsCount = 2 + random.nextInt(2); // 2 or 3 parts
            StringBuilder sb = new StringBuilder();

            // 10% chance bedrock style prefix '.'
            if (random.nextDouble() < 0.1) sb.append('.');

            for (int i = 0; i < partsCount; i++) {
                sb.append(NAME_PARTS[random.nextInt(NAME_PARTS.length)]);
            }

            // Optional number suffix
            if (random.nextDouble() < 0.5) {
                sb.append(random.nextInt(1000)); // add 0-999
            }

            String username = sb.toString();

            if (!usernames.contains(username)) {
                usernames.add(username);
            }
        }
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command,
                             @NotNull String label, @NotNull String[] args) {

        if (!(sender instanceof Player player)) {
            sender.sendMessage(ChatColor.RED + "Only players can run this command.");
            return true;
        }

        if (args.length < 1 || args.length > 2) {
            player.sendMessage(ChatColor.RED + "Usage: /startchat <durationInSeconds> [<speedTicks>]");
            player.sendMessage(ChatColor.GRAY + "Speed is delay between messages in ticks (default 2 ticks)");
            return true;
        }

        int durationSeconds;
        try {
            durationSeconds = Integer.parseInt(args[0]);
            if (durationSeconds <= 0) throw new NumberFormatException();
        } catch (NumberFormatException e) {
            player.sendMessage(ChatColor.RED + "Please provide a positive integer for the duration in seconds.");
            return true;
        }

        // default speed = 2 ticks (fast chat)
        int speedTicks = 2;
        if (args.length == 2) {
            try {
                speedTicks = Integer.parseInt(args[1]);
                if (speedTicks < 1) speedTicks = 1; // minimum 1 tick delay
            } catch (NumberFormatException e) {
                player.sendMessage(ChatColor.RED + "Speed must be a positive integer (ticks). Using default 2.");
                speedTicks = 2;
            }
        }

        int totalMessages = (int) Math.min(1000, (durationSeconds * 20) / speedTicks);

        sendChatMessages(player, 0, totalMessages, speedTicks);
        return true;
    }

    private void sendChatMessages(Player player, int index, int totalMessages, int speedTicks) {
        if (index >= totalMessages) return;

        String username;
        String message;
        if (index == totalMessages - 20) {
            username = "PhilClan";
            message = "Tpa for free loot and team";
        } else {
            username = usernames.get(random.nextInt(usernames.size()));
            message = randomMessage();
        }

        String chatLine = ChatColor.GRAY + username + ":" + ChatColor.WHITE + " " + message;
        player.sendMessage(chatLine);

        int delay = speedTicks + random.nextInt(10); // add some random delay for realism

        player.getScheduler().runDelayed(
                MuffinCore.getInstance(),
                task -> sendChatMessages(player, index + 1, totalMessages, speedTicks),
                null,
                delay
        );
    }

    private String randomMessage() {
        String base = BASE_PHRASES[random.nextInt(BASE_PHRASES.length)];

        if (random.nextDouble() < 0.3) {
            String[] suffixes = {" :)", " lol", "!", " anyone?", " asap", " plz", " xd"};
            base += suffixes[random.nextInt(suffixes.length)];
        }
        if (random.nextDouble() < 0.2) {
            String[] prefixes = {"hey ", "yo ", "sup ", "lol ", "brb ", "np "};
            base = prefixes[random.nextInt(prefixes.length)] + base;
        }
        return base;
    }
}
