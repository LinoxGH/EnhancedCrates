package me.linoxgh.cratesenhanced.commands;

import me.linoxgh.cratesenhanced.data.CrateStorage;
import me.linoxgh.cratesenhanced.data.MessageStorage;
import me.linoxgh.cratesenhanced.gui.GUITracker;
import me.linoxgh.cratesenhanced.commands.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class MainCommand implements CommandExecutor {
    private final MessageStorage messages;

    private final Map<String, Command> map = new HashMap<>();
    private final HelpCommand help;
    private final ListCommand list;
    private final CreateCommand create;
    private final DeleteCommand delete;
    private final EditCommand edit;
    private final GiveCommand give;
    private final TypeCommand type;

    public MainCommand(@NotNull CrateStorage crates, @NotNull GUITracker guiTracker, @NotNull MessageStorage messages) {
        this.help   = new HelpCommand  ("help",   map,  messages);
        this.list   = new ListCommand  ("list",   map,  crates, guiTracker, messages);
        this.create = new CreateCommand("create", map,  crates, messages);
        this.delete = new DeleteCommand("delete", map,  crates, messages);
        this.edit   = new EditCommand  ("edit",   map,  crates, guiTracker, messages);
        this.give   = new GiveCommand  ("give",   map,  crates, messages);
        this.type   = new TypeCommand  ("type",   map,  crates, messages);

        this.messages = messages;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull org.bukkit.command.Command command, @NotNull String label, @NotNull String[] args) {

        Command cmd = (args.length > 0)
                ? map.getOrDefault(args[0].toLowerCase(), help)
                : help;

        String perm = cmd.getPermission();
        if (perm != null && !(sender.hasPermission(perm))) {
            sender.sendMessage(messages.getMessage("commands.general.no-permission"));
            return true;
        }
        return cmd.execute(sender, args);
    }
}
