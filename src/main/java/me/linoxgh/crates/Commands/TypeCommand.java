package me.linoxgh.crates.Commands;

import me.linoxgh.crates.Data.CrateStorage;
import me.linoxgh.crates.Data.CrateType;
import me.linoxgh.crates.Data.Key;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class TypeCommand extends Command {
    private final CrateStorage crates;

    TypeCommand(@NotNull CrateStorage crates) {
        this.crates = crates;
    }

    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull String[] args) {
        if (!(sender.hasPermission("crates.create"))) {
            sender.sendMessage("§4You do not have enough permission to use this command.");
            return true;
        }
        if (!(sender instanceof Player)) {
            sender.sendMessage("§4This command can only be used in game.");
            return true;
        }
        Player p = (Player) sender;

        if (args.length != 3) return false;
        if (args[1].equals("add")) {
            crates.addCrateType(args[2], new CrateType(new Key(p.getInventory().getItemInMainHand())));
            p.sendMessage("§aSuccessfully added a new crate type.");
            return true;

        } else if (args[1].equals("delete")) {
            crates.removeCrateType(args[2]);
            p.sendMessage("§aSuccessfully deleted this crate type.");
            return true;
        }
        return false;
    }
}
