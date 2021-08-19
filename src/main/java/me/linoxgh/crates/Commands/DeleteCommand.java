package me.linoxgh.crates.Commands;

import me.linoxgh.crates.Data.Crate;
import me.linoxgh.crates.Data.CrateStorage;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class DeleteCommand extends Command {
    private final CrateStorage crates;

    DeleteCommand(@NotNull CrateStorage crates) {
        this.crates = crates;
    }

    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull String[] args) {
        if (!(sender.hasPermission("crates.delete"))) {
            sender.sendMessage("§4You do not have enough permission to use this command.");
            return true;
        }
        if (args.length != 2) return false;

        Crate crate = crates.getCrate(args[1]);
        if (crate == null) {
            sender.sendMessage("§4Could not find the crate.");
            return true;
        }

        crates.removeCrate(args[1]);
        sender.sendMessage("§aSuccessfully removed crate.");
        return true;
    }
}
