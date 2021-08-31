package me.linoxgh.cratesenhanced.commands;

import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class Command {

    public abstract boolean execute(@NotNull CommandSender sender, @NotNull String[] args);
    public abstract @Nullable String getPermission();

}
