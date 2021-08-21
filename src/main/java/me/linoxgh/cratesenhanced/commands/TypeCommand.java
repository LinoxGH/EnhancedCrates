package me.linoxgh.cratesenhanced.commands;

import me.linoxgh.cratesenhanced.data.CrateStorage;
import me.linoxgh.cratesenhanced.data.CrateType;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
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
            ItemStack heldItem = p.getInventory().getItemInMainHand();
            if (heldItem.getAmount() == 0 || heldItem.getType().isEmpty() || heldItem.getType().isAir()) {
                p.sendMessage("§4You must hold the key item in your hand.");
                return true;
            }

            CrateType crate = crates.getCrateType(args[2]);
            if (crate != null) {
                p.sendMessage("§4A crate type with this name already exists.");
                return true;
            }

            crates.addCrateType(args[2], new CrateType(heldItem));
            p.sendMessage("§aSuccessfully added a new crate type.");
            return true;

        } else if (args[1].equals("delete")) {
            CrateType crate = crates.getCrateType(args[2]);
            if (crate == null) {
                p.sendMessage("§4Could not find the crate type.");
                return true;
            }

            crates.removeCrateType(args[2]);
            p.sendMessage("§aSuccessfully deleted this crate type.");
            return true;
        }
        return false;
    }
}
