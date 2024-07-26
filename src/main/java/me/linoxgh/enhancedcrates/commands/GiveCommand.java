package me.linoxgh.enhancedcrates.commands;

import java.util.HashMap;
import java.util.Map;

import me.linoxgh.enhancedcrates.data.CrateStorage;
import me.linoxgh.enhancedcrates.data.CrateType;
import me.linoxgh.enhancedcrates.data.MessageStorage;
import me.linoxgh.enhancedcrates.data.rewards.Reward;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class GiveCommand extends Command {
    private final CrateStorage crates;
    private final MessageStorage messages;

    GiveCommand(@NotNull String name, @NotNull Map<String, Command> commandMap, @NotNull CrateStorage crates, @NotNull MessageStorage messages) {
        super(name, commandMap);
        this.crates = crates;
        this.messages = messages;
    }

    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull String[] args) {
        if (args.length < 4 || args.length > 6) return false;

        Player p = Bukkit.getPlayer(args[1]);
        if (p == null || !p.isOnline()) {
            sender.sendMessage(messages.getMessage("commands.give.invalid-player"));
            return true;
        }

        CrateType crate = crates.getCrateType(args[3]);
        if (crate == null) {
            sender.sendMessage(messages.getMessage("commands.give.invalid-cratetype"));
            return true;
        }
        boolean isSilent = (args.length == 6) || (args.length == 5 && args[4].equals("silent"));
        int amount = args.length == 6 ? Integer.parseInt(args[4]) : (args.length == 5 && !isSilent ? Integer.parseInt(args[4]) : 1);

        switch (args[2]) {
            case "key":
                giveKey(p, crate, amount);
                if (!isSilent) {
                    sender.sendMessage(messages.getMessage("commands.give.success"));
                    return true;
                }

            case "reward":
                for (int i = 0; i < amount; i++) {
                    Reward<?> reward = crate.getRandomReward();
                    if (reward == null) {
                        sender.sendMessage(messages.getMessage("commands.give.no-reward"));
                        return true;
                    }
                    if (!reward.giveReward(p)) {
                        sender.sendMessage(messages.getMessage("commands.give.fail"));
                        return true;
                    }
                    if (isSilent) {
                        sender.sendMessage(messages.getMessage("commands.give.success"));
                        return true;
                    }
                }
            default:
                return false;
        }
    }

    private void giveKey(@NotNull Player p, @NotNull CrateType crate, int amount) {
        for (int i = 0; i < amount; i++) {
            HashMap<Integer, ItemStack> unfits = p.getInventory().addItem(crate.getKey().clone());
            if (!(unfits.isEmpty())) {
                for (Map.Entry<Integer, ItemStack> entry : unfits.entrySet()) {
                    p.getLocation().getWorld().dropItem(p.getLocation(), entry.getValue());
                }
            }
        }
    }

    @Override
    public @Nullable String getPermission() {
        return "crates.give";
    }
}
