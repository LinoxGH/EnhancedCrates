package me.linoxgh.cratesenhanced.commands;

import me.linoxgh.cratesenhanced.data.CrateStorage;
import me.linoxgh.cratesenhanced.data.CrateType;
import me.linoxgh.cratesenhanced.data.MessageStorage;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class TypeCommand extends Command {
    private final CrateStorage crates;
    private final MessageStorage messages;

    TypeCommand(@NotNull CrateStorage crates, @NotNull MessageStorage messages) {
        this.crates = crates;
        this.messages = messages;
    }

    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(messages.getMessage("commands.type.invalid-sender"));
            return true;
        }
        Player p = (Player) sender;

        if (args.length != 3) return false;
        CrateType crate = crates.getCrateType(args[2]);
        if (args[1].equals("add")) {
            ItemStack heldItem = p.getInventory().getItemInMainHand();
            if (heldItem.getAmount() == 0 || heldItem.getType().isEmpty() || heldItem.getType().isAir()) {
                p.sendMessage(messages.getMessage("commands.type.invalid-key"));
                return true;
            }

            if (crate != null) {
                p.sendMessage(messages.getMessage("commands.type.preexisting-cratetype"));
                return true;
            }

            crates.addCrateType(args[2], new CrateType(args[2], heldItem));
            p.sendMessage(messages.getMessage("commands.type.success-add"));
            return true;

        } else if (args[1].equals("remove")) {
            if (crate == null) {
                p.sendMessage(messages.getMessage("commands.type.invalid-cratetype"));
                return true;
            }

            crates.removeCrateType(args[2]);
            p.sendMessage(messages.getMessage("commands.type.success-remove"));
            return true;
        }
        return false;
    }

    @Override
    public @Nullable String getPermission() {
        return "crates.create";
    }
}
