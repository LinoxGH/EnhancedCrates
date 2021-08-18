package me.linoxgh.crates.Commands;

import me.linoxgh.crates.Data.BlockPosition;
import me.linoxgh.crates.Data.Crate;
import me.linoxgh.crates.Data.CrateStorage;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class CreateCommand extends Command {
    private final CrateStorage crates;

    CreateCommand(@NotNull CrateStorage crates) {
        this.crates = crates;
    }

    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull String[] args) {
        if (!(sender.hasPermission("crates.create"))) {
            sender.sendMessage("§4You do not have enough permission to use this command.");
            return true;
        }
        if (args.length != 7) return false;

        try {
            if (Bukkit.getWorld(args[5]) == null) {
                sender.sendMessage("§4Please enter a valid world name.");
                return true;
            }

            BlockPosition pos = new BlockPosition(
                    Integer.parseInt(args[2]),
                    Integer.parseInt(args[3]),
                    Integer.parseInt(args[4]),
                    args[5]
            );
            Crate crate = new Crate(pos, args[6]);
            crates.addCrate(args[1], crate);

            sender.sendMessage("§aSuccessfully created a crate.");
            return true;
        } catch (NumberFormatException ignored) {
            sender.sendMessage("§4Please enter a valid coordinate.");
            return true;
        }
    }
}
