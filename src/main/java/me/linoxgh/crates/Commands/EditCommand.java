package me.linoxgh.crates.Commands;

import me.linoxgh.crates.Data.CrateStorage;
import me.linoxgh.crates.Data.CrateType;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class EditCommand extends Command {
    private final CrateStorage crates;

    EditCommand(@NotNull CrateStorage crates) {
        this.crates = crates;
    }

    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull String[] args) {
        if (!(sender.hasPermission("crates.edit"))) {
            sender.sendMessage("§4You do not have enough permission to use this command.");
            return true;
        }
        if (!(sender instanceof Player)) {
            sender.sendMessage("§4This command can only be used in game.");
            return true;
        }
        Player p = (Player) sender;

        if (args.length < 2) return false;
        CrateType crate = crates.getCrateType(args[1]);
        if (crate == null) {
            p.sendMessage("§4Could not find the specified crate type.");
            return true;
        }
        ItemStack heldItem = p.getInventory().getItemInMainHand();

        // Removing a reward or changing the key.
        if (args.length == 4 && (args[2].equals("reward") || args[2].equals("key"))) {
            if (args[2].equals("reward") && args[3].equals("remove")) {
                crate.deleteDrop(heldItem);
                p.sendMessage("§aSuccessfully removed the held reward from this crate type");
                return true;

            } else if (args[2].equals("key") && args[3].equals("set")) {
                crate.setKey(heldItem);
                p.sendMessage("§aSuccessfully changed the key of this crate type.");
                return true;

            }
        // Adding/Setting weight of a reward.
        } else if (args.length == 5 && args[2].equals("reward")) {
            int weight;
            try {
                weight = Integer.parseInt(args[4]);
            } catch (NumberFormatException ignored) {
                p.sendMessage("§4Please enter a valid reward weight.");
                return true;
            }

            if (args[3].equals("add")) {
                crate.addDrop(weight, heldItem);
                p.sendMessage("§aSuccessfully added a reward to this crate type.");
                return true;

            } else if (args[3].equals("set-weight")) {
                crate.deleteDrop(heldItem);
                crate.addDrop(weight, heldItem);
                p.sendMessage("§aSuccessfully changed the weight of this reward.");
                return true;
            }
        }
        return false;
    }
}
