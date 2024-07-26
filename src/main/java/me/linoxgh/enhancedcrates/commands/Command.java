package me.linoxgh.enhancedcrates.commands;

import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

public abstract class Command {

    protected Command(@NotNull String name, @NotNull Map<String, Command> commandMap) {
        commandMap.put(name, this);
    }

    public abstract boolean execute(@NotNull CommandSender sender, @NotNull String[] args);
    public abstract @Nullable String getPermission();

}
