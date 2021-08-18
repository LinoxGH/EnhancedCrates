package me.linoxgh.crates.Commands;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class HelpCommand extends Command {

    private final HashMap<UUID, List<String[]>> cachedHelp = new HashMap<>();

    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull String[] args) {

        if (sender instanceof Player) {
            UUID id = ((Player) sender).getUniqueId();
            if (cachedHelp.containsKey(id)) {
                return sendMessage(sender, cachedHelp.get(id), args);
            }
        }

        List<String> messages = new ArrayList<>();
        messages.add("§e.*.-----_-----{ §3Crates §e}-----_-----.*.");
        if (sender.hasPermission("crates.main")) {
            messages.add("§6/crates help §3[page]");
            messages.add("§e- §aShows the help pages.");
            messages.add(" ");
        }
        if (sender.hasPermission("crates.list")) {
            messages.add("§6/crates list");
            messages.add("§e- §aLists down all of the crates and their types.");
            messages.add(" ");
        }
        if (sender.hasPermission("crates.create")) {
            messages.add("§6/crates create &9<crate-name> <x> <y> <z> <world> <crate-type>");
            messages.add("§e- §aCreates a new crate.");
            messages.add("§e- §aYou can edit the rewards with §6/crates edit§a.");
            messages.add(" ");

            messages.add("§6/crates type add|delete §9<crate-type>");
            messages.add("§e- §aCreates|Deletes a crate type.");
            messages.add(" ");
        }
        if (sender.hasPermission("crates.delete")) {
            messages.add("§6/crates delete §9<crate-name>");
            messages.add("§e- §aDeletes the crate.");
            messages.add("§e- §cThis will also delete the block in that position!");
            messages.add(" ");
        }
        if (sender.hasPermission("crates.edit")) {
            messages.add("§6/crates edit §9<crate-type> §6reward add|set-weight §9<name> <weight>");
            messages.add("§e- §aAdds a new reward to the crate type.");
            messages.add("§e- §aYou can also use this to change the weight of a reward.");
            messages.add("§6/crates edit §9<crate-type> §6reward remove");
            messages.add("§e- §aRemoves a reward from the crate type.");
            messages.add("§e- §4You must hold the reward item in your hand.");
            messages.add(" ");

            messages.add("§6/crates edit §9<crate-type> §6key set");
            messages.add("§e- §aChanges the key of the crate type.");
            messages.add("§e- §4You most hold the key item in your hand.");
            messages.add(" ");
        }
        if (sender.hasPermission("crates.give")) {
            messages.add("§6/crates give §9<player> §6key §9<crate-type>");
            messages.add("§e- §aGives a crate key.");
            messages.add(" ");
        }

        List<String[]> pages = divide(messages);
        if (sender instanceof Player) {
            cachedHelp.put(((Player) sender).getUniqueId(), pages);
        }
        return sendMessage(sender, pages, args);
    }

    private @NotNull List<String[]> divide(@NotNull List<String> messages) {
        List<String[]> result = new ArrayList<>();

        int total = 0;
        int pageIndex = 0;
        List<List<String>> pages = new ArrayList<>();
        for (String message : messages) {
            if (total > 3) {
                pages.get(pageIndex).set(pages.get(pageIndex).size() - 1, "§e.*.-----_-----{ §3Help " + (pageIndex + 1) + " §e}-----_-----.*.");
                pageIndex++;
            }

            if (pages.size() < pageIndex + 1) pages.add(new ArrayList<>());
            pages.get(pageIndex).add(message);
            if (message.equals(" ")) total++;
        }

        for (List<String> page : pages) {
            result.add(page.toArray(new String[0]));
        }
        return result;
    }

    private boolean sendMessage(@NotNull CommandSender sender, @NotNull List<String[]> messages, @NotNull String[] args) {
        if (args.length == 0) {
            sender.sendMessage(messages.get(0));
            return true;
        } else {
            try {
                int page = Integer.parseInt(args[1]);
                if (page > messages.size()) return false;

                sender.sendMessage(messages.get(page));
                return true;
            } catch (NumberFormatException ignored) {
                return false;
            }
        }
    }
}
