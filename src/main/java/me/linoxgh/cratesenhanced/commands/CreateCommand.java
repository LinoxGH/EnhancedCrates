package me.linoxgh.cratesenhanced.commands;

import me.linoxgh.cratesenhanced.data.BlockPosition;
import me.linoxgh.cratesenhanced.data.Crate;
import me.linoxgh.cratesenhanced.data.CrateStorage;
import me.linoxgh.cratesenhanced.data.MessageStorage;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

public class CreateCommand extends Command {
    private final CrateStorage crates;
    private final MessageStorage messages;

    protected CreateCommand(@NotNull String name, @NotNull Map<String, Command> commandMap, @NotNull CrateStorage crates, @NotNull MessageStorage messages) {
        super(name, commandMap);
        this.crates = crates;
        this.messages = messages;
    }

    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull String[] args) {
        if (args.length != 7) return false;

        try {
            if (Bukkit.getWorld(args[5]) == null) {
                sender.sendMessage(messages.getMessage("commands.create.invalid-world"));
                return true;
            }

            Crate testName = crates.getCrate(args[1]);
            if (testName != null) {
                sender.sendMessage(messages.getMessage("commands.create.preexisting.cratename"));
                return true;
            }

            BlockPosition pos = new BlockPosition(
                    Integer.parseInt(args[2]),
                    Integer.parseInt(args[3]),
                    Integer.parseInt(args[4]),
                    args[5]
            );

            Crate testPos = crates.getCrate(pos);
            if (testPos != null) {
                sender.sendMessage(messages.getMessage("commands.create.preexisting-coordinate"));
                return true;
            }

            Crate crate = new Crate(args[1], pos, args[6]);
            crates.addCrate(args[1], crate);

            sender.sendMessage(messages.getMessage("commands.create.success"));
            return true;
        } catch (NumberFormatException ignored) {
            sender.sendMessage(messages.getMessage("commands.create.invalid-coordinate"));
            return true;
        }
    }

    @Override
    public @Nullable String getPermission() {
        return "crates.create";
    }
}
