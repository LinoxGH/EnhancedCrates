package me.linoxgh.cratesenhanced.commands;

import me.linoxgh.cratesenhanced.data.CrateStorage;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class MainTabComplete implements TabCompleter {
    private final CrateStorage crates;

    public MainTabComplete(@NotNull CrateStorage crates) {
        this.crates = crates;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        switch (args.length) {
            case 0:
                return Arrays.asList("help", "create", "type", "delete", "edit", "give", "list", "reward");

            case 1:
                switch (args[0]) {
                    case "delete":
                        return Arrays.asList(crates.getCrates().keySet().toArray(new String[0]));
                    case "edit":
                    case "reward":
                        return Arrays.asList(crates.getCrateTypes().keySet().toArray(new String[0]));
                    case "type":
                        return Arrays.asList("add", "remove");
                    case "give":
                        return Bukkit.getOnlinePlayers().stream().map(Player::getName).collect(Collectors.toList());
                    case "list":
                        return Arrays.asList("crates", "types", "rewards");
                    default:
                        return null;
                }

            case 2:
                switch (args[0]) {
                    case "reward":
                        return Arrays.asList("item", "money", "command");
                    case "type":
                        if (args[1].equals("remove")) {
                            return Arrays.asList(crates.getCrateTypes().keySet().toArray(new String[0]));
                        } else return null;
                    case "give":
                        return Arrays.asList("key", "reward");
                    case "list":
                        if (args[1].equals("rewards")) {
                            return Arrays.asList(crates.getCrateTypes().keySet().toArray(new String[0]));
                        } else return null;
                    default:
                        return null;
                }

            case 3:
                if ("give".equals(args[0])) {
                    return Arrays.asList(crates.getCrateTypes().keySet().toArray(new String[0]));
                }
                return null;

            case 4:
            case 5:
                if ("give".equals(args[0])) {
                    return Collections.singletonList("silent");
                }
                return null;

            default:
                return null;
        }
    }
}
