package me.linoxgh.cratesenhanced.commands;

import java.util.Map;

import me.linoxgh.cratesenhanced.data.BlockPosition;
import me.linoxgh.cratesenhanced.data.Crate;
import me.linoxgh.cratesenhanced.data.CrateStorage;
import me.linoxgh.cratesenhanced.data.CrateType;
import me.linoxgh.cratesenhanced.gui.GUITracker;
import me.linoxgh.cratesenhanced.gui.ListRewardMenu;
import me.linoxgh.cratesenhanced.gui.MenuType;
import net.kyori.adventure.text.Component;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class ListCommand extends Command {
    private final CrateStorage crates;
    private final GUITracker guiTracker;

    ListCommand(@NotNull CrateStorage crates, @NotNull GUITracker guiTracker) {
        this.crates = crates;
        this.guiTracker = guiTracker;
    }

    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull String[] args) {
        if (!(sender.hasPermission("crates.list"))) {
            sender.sendMessage("§4You do not have enough permission to use this command.");
            return true;
        }

        if (!(sender instanceof Player)) {
            sender.sendMessage("§4This command can only be used in game.");
            return true;
        }
        Player p = (Player) sender;

        if (args.length == 1) {
            p.sendMessage("§e.*.-----_-----{ §3Crates §e}-----_-----.*.");
            p.sendMessage("§9Crate Name §e- §9Location §e- §9Crate Type");
            for (Map.Entry<String, Crate> entry : crates.getCrates().entrySet()) {
                Crate crate = entry.getValue();
                BlockPosition pos = crate.getPos();
                String location = pos.getX() + ":" + pos.getY() + ":" + pos.getZ() + ":" + pos.getWorld();
                p.sendMessage("§e- §f" + entry.getKey() + " §e- §f" + location + " §e- §f" + crate.getCrateType());
            }
            return true;
        }

        if (args.length != 2 && args.length != 3) return false;

        switch (args[1]) {
            case "crates":
                p.sendMessage("§e.*.-----_-----{ §3Crates §e}-----_-----.*.");
                p.sendMessage("§9Crate Name §e- §9Location §e- §9Crate Type");
                for (Map.Entry<String, Crate> entry : crates.getCrates().entrySet()) {
                    Crate crate = entry.getValue();
                    BlockPosition pos = crate.getPos();
                    String location = pos.getX() + ":" + pos.getY() + ":" + pos.getZ() + ":" + pos.getWorld();
                    p.sendMessage("§e- §f" + entry.getKey() + " §e- §f" + location + " §e- §f" + crate.getCrateType());
                }
                return true;

            case "types":
                p.sendMessage("§e.*.-----_-----{ §3Crates §e}-----_-----.*.");
                p.sendMessage("§9Crate Types");
                for (Map.Entry<String, CrateType> entry : crates.getCrateTypes().entrySet()) {
                    ItemStack key = entry.getValue().getKey();
                    p.sendMessage(Component.text("§e- §f" + entry.getKey() + " §e- §f")
                            .append(key.displayName().hoverEvent(key.asHoverEvent()))
                            .append(Component.text(" §6x§9" + key.getAmount()))
                    );
                }
                return true;

            case "rewards":
                if (args.length != 3) return false;
                CrateType type = crates.getCrateType(args[2]);
                if (type == null) {
                    p.sendMessage("§4Could not find the specified crate type.");
                    return true;
                }

                ListRewardMenu list = new ListRewardMenu(type);
                p.openInventory(list.getInventories()[0]);
                guiTracker.addToMenuTracker(p.getUniqueId(), MenuType.LIST_REWARD);
                guiTracker.addToListTracker(p.getUniqueId(), list);
                return true;

            default:
                return false;
        }
    }
}
