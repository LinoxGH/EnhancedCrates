package me.linoxgh.cratesenhanced.commands;

import me.linoxgh.cratesenhanced.data.CrateStorage;
import me.linoxgh.cratesenhanced.data.CrateType;
import me.linoxgh.cratesenhanced.gui.GUITracker;
import me.linoxgh.cratesenhanced.gui.MenuType;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class EditCommand extends Command {
    private final CrateStorage crates;
    private final GUITracker guiTracker;

    EditCommand(@NotNull CrateStorage crates, @NotNull GUITracker guiTracker) {
        this.crates = crates;
        this.guiTracker = guiTracker;
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
        p.openInventory(crate.getMenu().getInv());
        guiTracker.addToMenuTracker(p.getUniqueId(), MenuType.CRATE_TYPE);
        return true;
    }
}
