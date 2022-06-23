package me.linoxgh.cratesenhanced.commands;

import me.linoxgh.cratesenhanced.data.CrateStorage;
import me.linoxgh.cratesenhanced.data.MessageStorage;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

public class RewardCommand extends Command {
    private final CrateStorage crates;
    private final MessageStorage messages;

    protected RewardCommand(@NotNull String name, @NotNull Map<String, Command> commandMap, @NotNull CrateStorage crates, @NotNull MessageStorage messages) {
        super(name, commandMap);
        this.crates = crates;
        this.messages = messages;
    }

    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull String[] args) {
        return false;
    }

    @Override
    public @Nullable String getPermission() {
        return null;
    }
}
