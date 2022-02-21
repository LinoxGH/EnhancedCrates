package me.linoxgh.cratesenhanced.commands;

import me.linoxgh.cratesenhanced.data.CrateStorage;
import me.linoxgh.cratesenhanced.data.CrateType;
import me.linoxgh.cratesenhanced.data.MessageStorage;
import me.linoxgh.cratesenhanced.gui.GUITracker;
import me.linoxgh.cratesenhanced.gui.MenuType;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

public class EditCommand extends Command {
    private final CrateStorage crates;
    private final GUITracker guiTracker;
    private final MessageStorage messages;

    EditCommand(@NotNull String name, @NotNull Map<String, Command> commandMap, @NotNull CrateStorage crates, @NotNull GUITracker guiTracker, @NotNull MessageStorage messages) {
        super(name, commandMap);
        this.crates = crates;
        this.guiTracker = guiTracker;
        this.messages = messages;
    }

    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(messages.getMessage("commands.edit.invalid-sender"));
            return true;
        }
        Player p = (Player) sender;

        if (args.length < 2) return false;
        CrateType crate = crates.getCrateType(args[1]);
        if (crate == null) {
            p.sendMessage(messages.getMessage("commands.edit.invalid-cratetype"));
            return true;
        }
        p.openInventory(crate.getMenu().getInv());
        guiTracker.addToMenuTracker(p.getUniqueId(), MenuType.CRATE_TYPE);
        return true;
    }

    @Override
    public @Nullable String getPermission() {
        return "crates.edit";
    }
}
