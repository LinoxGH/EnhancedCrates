package me.linoxgh.enhancedcrates.commands;

import me.linoxgh.enhancedcrates.data.CrateStorage;
import me.linoxgh.enhancedcrates.data.MessageStorage;
import me.linoxgh.enhancedcrates.gui.GUITracker;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.stream.Collectors;

public class MainCommand implements CommandExecutor, TabCompleter {
    private final CrateStorage crates;
    private final MessageStorage messages;

    private final Map<String, Command> map = new HashMap<>();
    private final HelpCommand help;
    private final ListCommand list;
    private final CreateCommand create;
    private final DeleteCommand delete;
    private final EditCommand edit;
    private final GiveCommand give;
    private final TypeCommand type;
    private final RewardCommand reward;

    public MainCommand(@NotNull CrateStorage crates, @NotNull GUITracker guiTracker, @NotNull MessageStorage messages) {
        this.help   = new HelpCommand  ("help",   map,  messages);
        this.list   = new ListCommand  ("list",   map,  crates, guiTracker, messages);
        this.create = new CreateCommand("create", map,  crates, messages);
        this.delete = new DeleteCommand("delete", map,  crates, messages);
        this.edit   = new EditCommand  ("edit",   map,  crates, guiTracker, messages);
        this.give   = new GiveCommand  ("give",   map,  crates, messages);
        this.type   = new TypeCommand  ("type",   map,  crates, messages);
        this.reward = new RewardCommand("reward", map,  crates, messages);

        this.crates = crates;
        this.messages = messages;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull org.bukkit.command.Command command, @NotNull String label, @NotNull String[] args) {

        Command cmd = (args.length > 0)
                ? map.getOrDefault(args[0].toLowerCase(), help)
                : help;

        String perm = cmd.getPermission();
        if (perm != null && !(sender.hasPermission(perm))) {
            sender.sendMessage(messages.getMessage("commands.general.no-permission"));
            return true;
        }
        return cmd.execute(sender, args);
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull org.bukkit.command.Command command, @NotNull String label, @NotNull String[] args) {
        List<String> completions = new ArrayList<>();
        List<String> commands = new ArrayList<>();

        switch (args.length) {
            case 1:
                commands.addAll(Arrays.asList("help", "create", "type", "delete", "edit", "give", "list", "reward"));
                break;

            case 2:
                switch (args[0]) {
                    case "delete":
                        commands.addAll(Arrays.asList(crates.getCrates().keySet().toArray(new String[0])));
                        break;
                    case "edit":
                    case "reward":
                        commands.addAll(Arrays.asList(crates.getCrateTypes().keySet().toArray(new String[0])));
                        break;
                    case "type":
                        commands.addAll(Arrays.asList("add", "remove"));
                        break;
                    case "give":
                        commands.addAll(Bukkit.getOnlinePlayers().stream().map(Player::getName).collect(Collectors.toList()));
                        break;
                    case "list":
                        commands.addAll(Arrays.asList("crates", "types", "rewards"));
                        break;
                }
                break;

            case 3:
                switch (args[0]) {
                    case "reward":
                        commands.addAll(Arrays.asList("item", "money", "command"));
                        break;
                    case "type":
                        if (args[1].equals("remove")) {
                            commands.addAll(Arrays.asList(crates.getCrateTypes().keySet().toArray(new String[0])));
                        }
                        break;
                    case "give":
                        commands.addAll(Arrays.asList("key", "reward"));
                        break;
                    case "list":
                        if (args[1].equals("rewards")) {
                            commands.addAll(Arrays.asList(crates.getCrateTypes().keySet().toArray(new String[0])));
                        }
                        break;
                }
                break;

            case 4:
                if ("give".equals(args[0])) {
                    commands.addAll(Arrays.asList(crates.getCrateTypes().keySet().toArray(new String[0])));
                }
                break;

            case 5:
            case 6:
                if ("give".equals(args[0])) {
                    commands.addAll(Collections.singletonList("silent"));
                }
                break;
        }

        StringUtil.copyPartialMatches(args[args.length - 1], commands, completions);
        return completions;
    }
}
