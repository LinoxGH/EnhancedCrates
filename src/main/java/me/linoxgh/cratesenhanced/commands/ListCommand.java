package me.linoxgh.cratesenhanced.commands;

import java.util.Map;

import me.linoxgh.cratesenhanced.data.BlockPosition;
import me.linoxgh.cratesenhanced.data.Crate;
import me.linoxgh.cratesenhanced.data.CrateStorage;
import me.linoxgh.cratesenhanced.data.CrateType;
import me.linoxgh.cratesenhanced.data.MessageStorage;
import me.linoxgh.cratesenhanced.gui.GUITracker;
import me.linoxgh.cratesenhanced.gui.ListRewardMenu;
import me.linoxgh.cratesenhanced.gui.MenuType;
import me.linoxgh.cratesenhanced.gui.SimplifiedListRewardMenu;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ListCommand extends Command {
    private final CrateStorage crates;
    private final GUITracker guiTracker;
    private final MessageStorage messages;

    protected ListCommand(@NotNull String name, @NotNull Map<String, Command> commandMap, @NotNull CrateStorage crates, @NotNull GUITracker guiTracker, @NotNull MessageStorage messages) {
        super(name, commandMap);
        this.crates = crates;
        this.guiTracker = guiTracker;
        this.messages = messages;
    }

    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(messages.getMessage("commands.list.invalid-sender"));
            return true;
        }
        Player p = (Player) sender;

        if (args.length == 1) {
            p.sendMessage(messages.getMessage("commands.list.header"));
            p.sendMessage(messages.getMessage("commands.list.columns"));
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
                p.sendMessage(messages.getMessage("commands.list.header"));
                p.sendMessage(messages.getMessage("commands.list.columns"));
                for (Map.Entry<String, Crate> entry : crates.getCrates().entrySet()) {
                    Crate crate = entry.getValue();
                    BlockPosition pos = crate.getPos();
                    String location = pos.getX() + ":" + pos.getY() + ":" + pos.getZ() + ":" + pos.getWorld();
                    p.sendMessage("§e- §f" + entry.getKey() + " §e- §f" + location + " §e- §f" + crate.getCrateType());
                }
                return true;

            case "types":
                p.sendMessage(messages.getMessage("commands.list.header"));
                p.sendMessage(messages.getMessage("commands.list.cratetypes"));
                for (Map.Entry<String, CrateType> entry : crates.getCrateTypes().entrySet()) {
                    ItemStack key = entry.getValue().getKey();
                    p.sendMessage(Component.text("§e- §f" + entry.getKey() + " §e- §f")
                            .append(key.displayName().hoverEvent(key.asHoverEvent()))
                            .append(Component.text(" §6x§9" + key.getAmount()))
                            .clickEvent(ClickEvent.runCommand("/crate give " + p.getName() + " key " + entry.getKey()))
                    );
                }
                return true;

            case "rewards":
                if (args.length != 3) return false;
                CrateType type = crates.getCrateType(args[2]);
                if (type == null) {
                    p.sendMessage(messages.getMessage("commands.list.invalid-cratetype"));
                    return true;
                }

                SimplifiedListRewardMenu list = new SimplifiedListRewardMenu(type);
                if (list.getInventories().length == 0) return true;
                p.openInventory(list.getInventories()[0]);
                guiTracker.addToMenuTracker(p.getUniqueId(), MenuType.SIMPLIFIED_LIST_REWARD);
                guiTracker.addToSimplifiedListTracker(p.getUniqueId(), list);
                return true;

            default:
                return false;
        }
    }

    @Override
    public @Nullable String getPermission() {
        return "crates.list";
    }
}
