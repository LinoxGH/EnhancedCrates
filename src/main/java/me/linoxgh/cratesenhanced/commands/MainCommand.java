package me.linoxgh.cratesenhanced.commands;

import me.linoxgh.cratesenhanced.data.CrateStorage;
import me.linoxgh.cratesenhanced.gui.GUITracker;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class MainCommand implements CommandExecutor {

    private final HelpCommand help;
    private final ListCommand list;
    private final CreateCommand create;
    private final DeleteCommand delete;
    private final EditCommand edit;
    private final GiveCommand give;
    private final TypeCommand type;

    public MainCommand(@NotNull CrateStorage crates, @NotNull GUITracker guiTracker) {
        this.help = new HelpCommand();
        this.list = new ListCommand(crates, guiTracker);
        this.create = new CreateCommand(crates);
        this.delete = new DeleteCommand(crates);
        this.edit = new EditCommand(crates, guiTracker);
        this.give = new GiveCommand(crates);
        this.type = new TypeCommand(crates);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length == 0) {
            return help.execute(sender, args);
        }

        switch (args[0]) {
            case "help":
                return help.execute(sender, args);

            case "list":
                return list.execute(sender, args);

            case "create":
                return create.execute(sender, args);

            case "type":
                return type.execute(sender, args);

            case "delete":
                return delete.execute(sender, args);

            case "edit":
                return edit.execute(sender, args);

            case "give":
                return give.execute(sender, args);

            default:
                return false;
        }
    }
}
