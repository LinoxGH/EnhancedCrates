package me.linoxgh.crates.Commands;

import java.util.Map;

import me.linoxgh.crates.Data.BlockPosition;
import me.linoxgh.crates.Data.Crate;
import me.linoxgh.crates.Data.CrateStorage;
import org.bukkit.command.CommandSender;
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
}
