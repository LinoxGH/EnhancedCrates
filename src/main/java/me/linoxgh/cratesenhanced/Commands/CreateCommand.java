package me.linoxgh.cratesenhanced.Commands;

import me.linoxgh.cratesenhanced.Data.BlockPosition;
import me.linoxgh.cratesenhanced.Data.Crate;
import me.linoxgh.cratesenhanced.Data.CrateStorage;
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

            Crate testName = crates.getCrate(args[1]);
            if (testName != null) {
                sender.sendMessage("§4A crate with this name already exists.");
                return true;
            }

            BlockPosition pos = new BlockPosition(
                    Integer.parseInt(args[2]),
                    Integer.parseInt(args[3]),
                    Integer.parseInt(args[4]),
                    args[5]
            );

            Crate testPos = crates.getCrate(pos);
            if (testPos != null) {
                sender.sendMessage("§4A crate in this coordinates already exists.");
                return true;
            }

            Crate crate = new Crate(args[1], pos, args[6]);
            crates.addCrate(args[1], crate);

            sender.sendMessage("§aSuccessfully created a crate.");
            return true;
        } catch (NumberFormatException ignored) {
            sender.sendMessage("§4Please enter a valid coordinate.");
            return true;
        }
    }
}
