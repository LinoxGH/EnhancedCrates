package me.linoxgh.enhancedcrates.commands;

import java.util.*;

import me.linoxgh.enhancedcrates.data.MessageStorage;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class HelpCommand extends Command {
    private final MessageStorage messageStorage;

    private final HashMap<UUID, List<String[]>> cachedHelp = new HashMap<>();
    private final HashMap<UUID, Long> cacheTimestamp = new HashMap<>();

    protected HelpCommand(@NotNull String name, @NotNull Map<String, Command> commandMap, @NotNull MessageStorage messageStorage) {
        super(name, commandMap);
        this.messageStorage = messageStorage;
    }

    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull String[] args) {
        if (sender instanceof Player) {
            UUID id = ((Player) sender).getUniqueId();
            if (cacheTimestamp.containsKey(id)) {
                if ((System.currentTimeMillis() - cacheTimestamp.get(id)) > 900_000) {
                    cachedHelp.remove(id);
                    cacheTimestamp.remove(id);
                }
            }
            cacheTimestamp.put(id, System.currentTimeMillis());
            if (cachedHelp.containsKey(id)) {
                return sendMessage(sender, cachedHelp.get(id), args);
            }
        }

        List<String> messages = new ArrayList<>(32);
        if (sender.hasPermission("crates.main")) {
            messages.add("§6/crates help §9[page]");
            messages.add(messageStorage.getMessage("commands.help.cmd-help1"));
            messages.add(" ");
        }
        if (sender.hasPermission("crates.create")) {
            messages.add("§6/crates create §9<crate-name> <x> <y> <z> <world> <crate-type>");
            messages.add(messageStorage.getMessage("commands.help.cmd-create1"));
            messages.add(messageStorage.getMessage("commands.help.cmd-create2"));
            messages.add(" ");

            messages.add("§6/crates type add|remove §9<crate-type>");
            messages.add(messageStorage.getMessage("commands.help.cmd-type1"));
            messages.add(" ");
        }
        if (sender.hasPermission("crates.delete")) {
            messages.add("§6/crates delete §9<crate-name>");
            messages.add(messageStorage.getMessage("commands.help.cmd-delete1"));
            messages.add(messageStorage.getMessage("commands.help.cmd-delete2"));
            messages.add(" ");
        }
        if (sender.hasPermission("crates.edit")) {
            messages.add("§6/crates edit §9<crate-type>");
            messages.add(messageStorage.getMessage("commands.help.cmd-edit1"));
            messages.add(" ");
        }
        if (sender.hasPermission("crates.give")) {
            messages.add("§6/crates give §9<player> §6key|reward §9<crate-type> §6[amount] §6[silent]");
            messages.add(messageStorage.getMessage("commands.help.cmd-give1"));
            messages.add(messageStorage.getMessage("commands.help.cmd-give2"));
            messages.add(" ");
        }
        if (sender.hasPermission("crates.list")) {
            messages.add("§6/crates list crates|types");
            messages.add(messageStorage.getMessage("commands.help.cmd-list1"));
            messages.add("§6/crates list rewards §9<crate-type>");
            messages.add(messageStorage.getMessage("commands.help.cmd-list2"));
            messages.add(" ");
        }
        if (sender.hasPermission("crates.edit")) {
            messages.add("§6/crates reward §9<crate-type> §6item §9<weight>");
            messages.add(messageStorage.getMessage("commands.help.cmd-reward1"));
            messages.add("§6/crates reward §9<crate-type> §6money §9<weight> <amount>");
            messages.add(messageStorage.getMessage("commands.help.cmd-reward2"));
            messages.add("§6/crates reward §9<crate-type> §6command §9<weight> <command>");
            messages.add(messageStorage.getMessage("commands.help.cmd-reward3"));
            messages.add(messageStorage.getMessage("commands.help.cmd-reward4"));
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
                pageIndex++;
                total = 0;
            }

            if (pages.size() < pageIndex + 1) pages.add(new ArrayList<>());
            pages.get(pageIndex).add(message);
            if (message.equals(" ")) total++;
        }

        for (int i = 0; i < pages.size(); i++) {
            List<String> page = pages.get(i);
            page.add(0, "§e.*.-----_-----{ §3Crates Enhanced §e}-----_-----.*.");
            page.add("§e.*.-----_-----{ §3Help - " + (i + 1) + " §e}-----_-----.*.");
            result.add(page.toArray(new String[0]));
        }
        return result;
    }

    private boolean sendMessage(@NotNull CommandSender sender, @NotNull List<String[]> messages, @NotNull String[] args) {
        if (args.length == 0 || args.length == 1) {
            sender.sendMessage(messages.get(0));
            return true;
        } else {
            try {
                int page = Integer.parseInt(args[1]);
                if (page > messages.size()) {
                    return false;
                }
                if (page <= 0) {
                    sender.sendMessage(messages.get(0));
                    return true;
                }
                sender.sendMessage(messages.get(page - 1));
                return true;
            } catch (NumberFormatException ignored) {
                return false;
            }
        }
    }

    @Override
    public @Nullable String getPermission() {
        return null;
    }
}
