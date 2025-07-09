package net.am7n.muffinCore.Tasks;

import org.bukkit.command.*;
import java.util.*;

public class RTPTabCompleter implements TabCompleter {

    private static final List<String> OPTIONS = Arrays.asList("overworld", "nether", "end");

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (args.length == 1) {
            String input = args[0].toLowerCase();
            List<String> matches = new ArrayList<>();
            for (String opt : OPTIONS) {
                if (opt.startsWith(input)) matches.add(opt);
            }
            return matches;
        }
        return Collections.emptyList();
    }
}
