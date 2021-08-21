package me.linoxgh.cratesenhanced.commands;

import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public abstract class Command {

    public abstract boolean execute(@NotNull CommandSender sender, @NotNull String[] args);

}
