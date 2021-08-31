package me.linoxgh.cratesenhanced.commands;

import me.linoxgh.cratesenhanced.data.Crate;
import me.linoxgh.cratesenhanced.data.CrateStorage;
import me.linoxgh.cratesenhanced.data.MessageStorage;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class DeleteCommand extends Command {
    private final CrateStorage crates;
    private final MessageStorage messages;

    DeleteCommand(@NotNull CrateStorage crates, @NotNull MessageStorage messages) {
        this.crates = crates;
        this.messages = messages;
    }

    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull String[] args) {
        if (args.length != 2) return false;

        Crate crate = crates.getCrate(args[1]);
        if (crate == null) {
            sender.sendMessage(messages.getMessage("commands.delete.invalid-cratename"));
            return true;
        }

        crates.removeCrate(args[1]);
        sender.sendMessage(messages.getMessage("commands.delete.success"));
        return true;
    }

    @Override
    public @Nullable String getPermission() {
        return "crates.delete";
    }
}
