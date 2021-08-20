package me.linoxgh.cratesenhanced.Commands;

import java.util.Map;

import me.linoxgh.cratesenhanced.Data.BlockPosition;
import me.linoxgh.cratesenhanced.Data.Crate;
import me.linoxgh.cratesenhanced.Data.CrateStorage;
import me.linoxgh.cratesenhanced.Data.CrateType;
import net.kyori.adventure.text.Component;
import org.bukkit.command.CommandSender;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class ListCommand extends Command {

    private final CrateStorage crates;

    ListCommand(@NotNull CrateStorage crates) {
        this.crates = crates;
    }

    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull String[] args) {
        if (!(sender.hasPermission("crates.list"))) {
            sender.sendMessage("§4You do not have enough permission to use this command.");
            return true;
        }

        if (args.length == 1) {
            sender.sendMessage("§e.*.-----_-----{ §3Crates §e}-----_-----.*.");
            sender.sendMessage("§9Crate Name §e- §9Location §e- §9Crate Type");
            for (Map.Entry<String, Crate> entry : crates.getCrates().entrySet()) {
                Crate crate = entry.getValue();
                BlockPosition pos = crate.getPos();
                String location = pos.getX() + ":" + pos.getY() + ":" + pos.getZ() + ":" + pos.getWorld();
                sender.sendMessage("§e- §f" + entry.getKey() + " §e- §f" + location + " §e- §f" + crate.getCrateType());
            }
            return true;
        }

        if (args.length != 2 && args.length != 3) return false;

        switch (args[1]) {
            case "crates":
                sender.sendMessage("§e.*.-----_-----{ §3Crates §e}-----_-----.*.");
                sender.sendMessage("§9Crate Name §e- §9Location §e- §9Crate Type");
                for (Map.Entry<String, Crate> entry : crates.getCrates().entrySet()) {
                    Crate crate = entry.getValue();
                    BlockPosition pos = crate.getPos();
                    String location = pos.getX() + ":" + pos.getY() + ":" + pos.getZ() + ":" + pos.getWorld();
                    sender.sendMessage("§e- §f" + entry.getKey() + " §e- §f" + location + " §e- §f" + crate.getCrateType());
                }
                return true;

            case "types":
                sender.sendMessage("§e.*.-----_-----{ §3Crates §e}-----_-----.*.");
                sender.sendMessage("§9Crate Types");
                for (Map.Entry<String, CrateType> entry : crates.getCrateTypes().entrySet()) {
                    ItemStack key = entry.getValue().getKey();
                    sender.sendMessage(Component.text("§e- §f" + entry.getKey() + " §e- §f")
                            .append(key.displayName().hoverEvent(key.asHoverEvent()))
                            .append(Component.text(" §6x§9" + key.getAmount()))
                    );
                }
                return true;

            case "rewards":
                if (args.length != 3) return false;
                CrateType crate = crates.getCrateType(args[2]);
                if (crate == null) {
                    sender.sendMessage("§4Could not find the specified crate type.");
                    return true;
                }
                sender.sendMessage("§e.*.-----_-----{ §3Crates §e}-----_-----.*.");
                sender.sendMessage("§9Reward §e- §9Weight");
                for (Map.Entry<ItemStack, Integer> entry : crate.getWeights().entrySet()) {
                    ItemStack drop = entry.getKey();
                    sender.sendMessage(Component.text("§e- §f")
                            .append(drop.displayName().hoverEvent(drop.asHoverEvent()))
                            .append(Component.text(" §6x§9" + drop.getAmount()))
                            .append(Component.text(" §e- §f" + entry.getValue().toString()))
                    );
                }
                return true;

            default:
                return false;
        }
    }
}
