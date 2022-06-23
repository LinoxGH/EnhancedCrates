package me.linoxgh.cratesenhanced.commands;

import me.linoxgh.cratesenhanced.data.CrateStorage;
import me.linoxgh.cratesenhanced.data.CrateType;
import me.linoxgh.cratesenhanced.data.MessageStorage;
import me.linoxgh.cratesenhanced.data.rewards.CommandReward;
import me.linoxgh.cratesenhanced.data.rewards.ItemReward;
import me.linoxgh.cratesenhanced.data.rewards.MoneyReward;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
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
        if (!(sender instanceof Player)) {
            sender.sendMessage(messages.getMessage("commands.reward.invalid-sender"));
            return true;
        }

        if (args.length < 4) {
            return false;
        }

        Player p = (Player) sender;
        CrateType crate = crates.getCrateType(args[1]);
        if (crate == null) {
            p.sendMessage(messages.getMessage("commands.reward.invalid-cratetype"));
            return true;
        }

        if (!args[3].matches("[0-9]+")) {
            p.sendMessage(messages.getMessage("commands.reward.invalid-weight"));
            return true;
        }
        int weight = Integer.parseInt(args[3]);
        if (weight <= 0) {
            p.sendMessage(messages.getMessage("commands.reward.invalid-weight"));
            return true;
        }

        switch (args[2]) {
            case "item":
                if (args.length != 4) return false;

                ItemStack heldItem = p.getInventory().getItemInMainHand();
                if (heldItem.getAmount() == 0 || heldItem.getType().isEmpty() || heldItem.getType().isAir()) {
                    p.sendMessage(messages.getMessage("commands.reward.invalid-item"));
                    return true;
                }

                ItemReward itemReward = new ItemReward(heldItem.clone(), weight);
                crate.addReward(weight, itemReward);
                p.sendMessage(messages.getMessage("commands.reward.success"));
                return true;

            case "money":
                if (args.length != 5) return false;

                double money;
                try {
                    money = Double.parseDouble(args[4]);
                } catch (NumberFormatException ignored) {
                    p.sendMessage(messages.getMessage("commands.reward.invalid-money"));
                    return true;
                }
                if (money <= 0) {
                    p.sendMessage(messages.getMessage("commands.reward.invalid-money"));
                    return true;
                }

                MoneyReward moneyReward = new MoneyReward(money, weight);
                crate.addReward(weight, moneyReward);
                p.sendMessage(messages.getMessage("commands.reward.success"));
                return true;

            case "command":
                if (args.length < 5) return false;

                StringBuilder cmd = new StringBuilder(args[4]);
                for (int i = 5; i < args.length; i++) {
                    cmd.append(" ").append(args[i]);
                }

                CommandReward commandReward = new CommandReward(cmd.toString(), weight);
                crate.addReward(weight, commandReward);
                p.sendMessage(messages.getMessage("commands.reward.success"));
                return true;
        }

        return false;
    }

    @Override
    public @Nullable String getPermission() {
        return "crates.edit";
    }
}
