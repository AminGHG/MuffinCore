package net.am7n.muffinCore.Commands;

import net.am7n.muffinCore.MuffinCore;
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
    private final List<String> usernames = new ArrayList<>(500);

    private static final String[] BASE_PHRASES = {
            "almost done my base", "tpa 4 1v1", "tpa 4 free loot", "tpa 4 team",
            "lf group", "who wanna trade?", "need help w/ farm", "sellin diamonds, pm",
            "any1 near spawn?", "let's build lol", "bedrock ppl here", "any invites 2 factions?",
            "minin trip soon", "need redstone, trade?", "who got food?", "come raid me!",
            "lookin 4 fight", "free potions here", "tradin ench books", "team up?"
    };

    private static final String[] NAME_PARTS = {
            "Red", "Stone", "Craft", "Mine", "Block", "Build", "Iron", "Diamond", "Wolf", "Shadow",
            "Ghost", "Fire", "Ice", "Dark", "Light", "Sky", "Storm", "Dragon", "Magic", "Lucky",
            "Fast", "Epic", "Silent", "Crazy", "Ghost", "Wolf", "Ninja", "Knight", "Wizard", "Hunter"
    };

    private static final String[] REAL_USERNAMES = {
            "_ItzRealDren", "PhilClan", "Alex", "Steve", "Herobrine", "Notch", "Creeper", "Enderman", "Zombie", "Skeleton",
            "DiamondDigger", "CraftySteve", "MinerMike", "PixelPro", "NetherNinja", "BlockBuilder", "RedstoneRick",
            "LapisLad", "IronMan", "TheWolf", "SkyWalker", "DragonSlayer", "MagicMage", "LuckyLuke", "SilentShadow",
            "CrazyCat", "GhostGamer", "NinjaNick", "KnightRider", "WizardWalt", "HunterHank", "DunkleEnte", "ty4u",
            "trwollo", "DrDonutt", "DrBonut", "JavaMio", "VertrauterDavid_", "AminGHG", "StevenGHG", "P1kz", "Zeltuv"
    };

    public StartChatCommand() {
        generateUsernames();
    }

    private void generateUsernames() {
        // add real users
        for (String real : REAL_USERNAMES) {
            usernames.add(real);
        }
        // then generate until 500
        while (usernames.size() < 500) {
            int parts = 2 + random.nextInt(2);
            StringBuilder sb = new StringBuilder();
            if (random.nextDouble() < 0.1) sb.append('.');
            for (int i = 0; i < parts; i++) {
                sb.append(NAME_PARTS[random.nextInt(NAME_PARTS.length)]);
            }
            if (random.nextDouble() < 0.5) sb.append(random.nextInt(1000));
            String name = sb.toString();
            if (!usernames.contains(name)) {
                usernames.add(name);
            }
        }
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender,
                             @NotNull Command command,
                             @NotNull String label,
                             @NotNull String[] args) {

        if (!(sender instanceof Player player)) {
            sender.sendMessage("§cOnly players can use this command.");
            return true;
        }
        if (args.length < 1 || args.length > 2) {
            player.sendMessage("§cUsage: /startchat <durationInSeconds> [speedInTicks]");
            return true;
        }

        int durationSeconds;
        int speedTicks = 4;
        try {
            durationSeconds = Integer.parseInt(args[0]);
            if (durationSeconds <= 0) throw new NumberFormatException();
        } catch (NumberFormatException e) {
            player.sendMessage("§cInvalid duration. Use a positive number.");
            return true;
        }

        if (args.length == 2) {
            try {
                speedTicks = Integer.parseInt(args[1]);
                if (speedTicks < 1) speedTicks = 1;
            } catch (NumberFormatException e) {
                player.sendMessage("§cInvalid speed. Using default.");
            }
        }

        long endTime = System.currentTimeMillis() + durationSeconds * 1000L;
        sendChatMessages(player, endTime, speedTicks, 0, false);
        return true;
    }

    private void sendChatMessages(Player player,
                                  long endTimeMillis,
                                  int speedTicks,
                                  int index,
                                  boolean specialSent) {
        if (System.currentTimeMillis() >= endTimeMillis) return;

        boolean nextSpecialSent = specialSent;
        String username;
        String message;
        if (!specialSent
                && index >= 20
                && System.currentTimeMillis() >= endTimeMillis - 3000) {
            username = "gk5n";
            message = "Tpa for free loot and team";
            nextSpecialSent = true;
        } else {
            username = usernames.get(random.nextInt(usernames.size()));
            message = randomMessage();
        }

        // hex #C7CEDC = §x§C§7§C§E§D§C
        player.sendMessage("§7" + username + ": §x§C§7§C§E§D§C" + message);

        int randomDelay = speedTicks + random.nextInt(6);
        final boolean specialFlagForNext = nextSpecialSent;
        player.getScheduler().runDelayed(
                MuffinCore.getInstance(),
                task -> sendChatMessages(player, endTimeMillis, speedTicks, index + 1, specialFlagForNext),
                null,
                randomDelay
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
