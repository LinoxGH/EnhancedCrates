package me.linoxgh.cratesenhanced.commands;

import me.linoxgh.cratesenhanced.data.CrateStorage;
import me.linoxgh.cratesenhanced.data.MessageStorage;
import me.linoxgh.cratesenhanced.gui.GUITracker;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class MainCommand implements CommandExecutor {
    private final MessageStorage messages;

    private final HelpCommand help;
    private final ListCommand list;
    private final CreateCommand create;
    private final DeleteCommand delete;
    private final EditCommand edit;
    private final GiveCommand give;
    private final TypeCommand type;

    public MainCommand(@NotNull CrateStorage crates, @NotNull GUITracker guiTracker, @NotNull MessageStorage messages) {
        this.help = new HelpCommand(messages);
        this.list = new ListCommand(crates, guiTracker, messages);
        this.create = new CreateCommand(crates, messages);
        this.delete = new DeleteCommand(crates, messages);
        this.edit = new EditCommand(crates, guiTracker, messages);
        this.give = new GiveCommand(crates, messages);
        this.type = new TypeCommand(crates, messages);

        this.messages = messages;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length == 0) {
            return help.execute(sender, args);
        }
        me.linoxgh.cratesenhanced.commands.Command cmd;
        switch (args[0]) {
            case "help":
                cmd = help;
                break;

            case "list":
                cmd = list;
                break;

            case "create":
                cmd = create;
                break;

            case "type":
                cmd = type;
                break;

            case "delete":
                cmd = delete;
                break;

            case "edit":
                cmd = edit;
                break;

            case "give":
                cmd = give;
                break;

            default:
                return false;
        }
        String perm = cmd.getPermission();
        if (perm != null && !(sender.hasPermission(perm))) {
            sender.sendMessage(messages.getMessage("commands.general.no-permission"));
            return true;
        }
        return cmd.execute(sender, args);
    }
}
