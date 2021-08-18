package me.linoxgh.crates.Commands;

import me.linoxgh.crates.Data.CrateStorage;
import me.linoxgh.crates.Data.CrateType;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class GiveCommand extends Command {
    private final CrateStorage crates;

    GiveCommand(@NotNull CrateStorage crates) {
        this.crates = crates;
    }

    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull String[] args) {
        if (!(sender.hasPermission("crates.give"))) {
            sender.sendMessage("§4You do not have enough permission to use this command.");
            return true;
        }

        if (args.length < 4 || args.length > 5) return false;
        if (!(args[2].equals("key"))) return false;

        if (args[4].equals("silent")) {
            Player p = Bukkit.getPlayer(args[1]);
            if (p == null || !p.isOnline()) return true;
            CrateType crate = crates.getCrateType(args[3]);
            if (crate == null) return true;
            p.getInventory().addItem(crate.getKey().getItem());
            return true;
        }

        Player p = Bukkit.getPlayer(args[1]);
        if (p == null || !p.isOnline()) {
            sender.sendMessage("§4The specified player is offline.");
            return true;
        }

        CrateType crate = crates.getCrateType(args[3]);
        if (crate == null) {
            sender.sendMessage("§4Could not find the specified crate type.");
            return true;
        }

        p.getInventory().addItem(crate.getKey().getItem());
        sender.sendMessage("§aSuccessfully gave the key to this crate type.");
        return false;
    }
}
