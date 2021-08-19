package me.linoxgh.crates.Commands;

import java.util.HashMap;
import java.util.Map;

import me.linoxgh.crates.Data.CrateStorage;
import me.linoxgh.crates.Data.CrateType;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
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

        if (args.length == 5 && args[4].equals("silent")) {
            Player p = Bukkit.getPlayer(args[1]);
            if (p == null || !p.isOnline()) return true;
            CrateType crate = crates.getCrateType(args[3]);
            if (crate == null) return true;
            HashMap<Integer, ItemStack> unfits = p.getInventory().addItem(crate.getKey());
            if (!(unfits.isEmpty())) {
                for (Map.Entry<Integer, ItemStack> entry : unfits.entrySet()) {
                    p.getLocation().getWorld().dropItem(p.getLocation(), entry.getValue());
                }
            }
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

        HashMap<Integer, ItemStack> unfits = p.getInventory().addItem(crate.getKey());
        if (!(unfits.isEmpty())) {
            for (Map.Entry<Integer, ItemStack> entry : unfits.entrySet()) {
                p.getLocation().getWorld().dropItem(p.getLocation(), entry.getValue());
            }
        }
        sender.sendMessage("§aSuccessfully gave the key to this crate type.");
        return true;
    }
}
