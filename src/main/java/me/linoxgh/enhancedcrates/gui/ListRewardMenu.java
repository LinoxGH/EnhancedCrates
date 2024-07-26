package me.linoxgh.enhancedcrates.gui;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import me.linoxgh.enhancedcrates.data.CrateType;
import me.linoxgh.enhancedcrates.data.rewards.CommandReward;
import me.linoxgh.enhancedcrates.data.rewards.ItemGroupReward;
import me.linoxgh.enhancedcrates.data.rewards.ItemReward;
import me.linoxgh.enhancedcrates.data.rewards.MoneyReward;
import me.linoxgh.enhancedcrates.data.rewards.Reward;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

public class ListRewardMenu {
    private final int[] BORDER_SLOTS = {45, 46, 48, 50, 52};
    private static final int[] CLICKABLE = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40, 41, 42, 43, 44, 47, 51, 53};
    private static final int[] REPLACEABLE = { };

    private final CrateType type;
    private Inventory[] inventories;

    public ListRewardMenu(@NotNull CrateType type) {
        this.type = type;
        inventories = new Inventory[0];
        populate();
    }

    public @NotNull Inventory[] getInventories() {
        return inventories;
    }
    public @NotNull CrateType getType() {
        return type;
    }
    public static int[] getClickableSlots() {
        return CLICKABLE;
    }
    public static int[] getReplaceableSlots() {
        return REPLACEABLE;
    }

    public void populate() {
        int additional = type.getWeights().size() % 45;
        int pages = (type.getWeights().size() / 45) + (additional == 0 ? 0 : 1);
        if (pages == 0) {
            inventories = new Inventory[1];
            Inventory inv = Bukkit.createInventory(null, 54, Component.text("§9Reward List §e- §21/1"));

            ItemStack border = new ItemStack(Material.GRAY_STAINED_GLASS_PANE);
            ItemMeta borderMeta = border.getItemMeta();
            borderMeta.displayName(Component.text(" "));
            border.setItemMeta(borderMeta);
            for (int slot : BORDER_SLOTS) {
                inv.setItem(slot, border);
            }

            ItemStack help = new ItemStack(Material.OAK_SIGN);
            ItemMeta helpMeta = help.getItemMeta();
            helpMeta.displayName(Component.text("§bClick a reward to remove it."));
            help.setItemMeta(helpMeta);
            inv.setItem(49, help);

            ItemStack previous = new ItemStack(Material.RED_DYE);
            ItemMeta previousMeta = previous.getItemMeta();
            previousMeta.displayName(Component.text("§cClick to go to the previous page."));
            previous.setItemMeta(previousMeta);
            inv.setItem(47, previous);

            ItemStack next = new ItemStack(Material.GREEN_DYE);
            ItemMeta nextMeta = next.getItemMeta();
            nextMeta.displayName(Component.text("§aClick to go to the next page."));
            next.setItemMeta(nextMeta);
            inv.setItem(51, next);

            ItemStack back = new ItemStack(Material.BARRIER);
            ItemMeta backMeta = back.getItemMeta();
            backMeta.displayName(Component.text("§cBACK"));
            back.setItemMeta(backMeta);
            inv.setItem(53, back);
            
            inventories[0] = inv;
            return;
        }
        inventories = new Inventory[pages];
        for (int i = 0; i < pages; i++) {
            inventories[i] = Bukkit.createInventory(null, 54, Component.text("§9Reward List §e- §2" + (i + 1) + "/" + pages));
        }

        List<Reward<?>> rewards = type.getWeights();

        for (int page = 0; page < inventories.length; page++) {
            Inventory inv = inventories[page];

            ItemStack border = new ItemStack(Material.GRAY_STAINED_GLASS_PANE);
            ItemMeta borderMeta = border.getItemMeta();
            borderMeta.displayName(Component.text(" "));
            border.setItemMeta(borderMeta);
            for (int slot : BORDER_SLOTS) {
                inv.setItem(slot, border);
            }

            ItemStack help = new ItemStack(Material.OAK_SIGN);
            ItemMeta helpMeta = help.getItemMeta();
            helpMeta.displayName(Component.text("§bClick a reward to remove it."));
            help.setItemMeta(helpMeta);
            inv.setItem(49, help);

            ItemStack previous = new ItemStack(Material.RED_DYE);
            ItemMeta previousMeta = previous.getItemMeta();
            previousMeta.displayName(Component.text("§cClick to go to the previous page."));
            previous.setItemMeta(previousMeta);
            inv.setItem(47, previous);

            ItemStack next = new ItemStack(Material.GREEN_DYE);
            ItemMeta nextMeta = next.getItemMeta();
            nextMeta.displayName(Component.text("§aClick to go to the next page."));
            next.setItemMeta(nextMeta);
            inv.setItem(51, next);

            ItemStack back = new ItemStack(Material.BARRIER);
            ItemMeta backMeta = back.getItemMeta();
            backMeta.displayName(Component.text("§cBACK"));
            back.setItemMeta(backMeta);
            inv.setItem(53, back);

            for (int slot = 0; slot < 45; slot++) {
                if ((page * 45 + slot) >= rewards.size()) break;
                Reward<?> reward = rewards.get(page * 45 + slot);
                if (reward instanceof ItemReward) {
                    ItemStack item = ((ItemReward) reward).getReward();
                    ItemMeta itemMeta = item.getItemMeta();
                    List<Component> lore = (itemMeta.lore() == null) ? new ArrayList<>() : itemMeta.lore();
                    lore.add(Component.text("§f "));
                    lore.add(Component.text("§3Weight: §f" + reward.getWeight()).decoration(TextDecoration.ITALIC, false));
                    itemMeta.lore(lore);
                    item.setItemMeta(itemMeta);

                    inv.setItem(slot, item);

                } else if (reward instanceof ItemGroupReward) {
                    ItemStack[] itemGroup = ((ItemGroupReward) reward).getReward();
                    ItemStack symbol = new ItemStack(itemGroup[ThreadLocalRandom.current().nextInt(itemGroup.length)].getType());
                    ItemMeta symbolMeta = symbol.getItemMeta();
                    symbolMeta.displayName(Component.text("§9Item Group Reward"));
                    List<Component> lore = new ArrayList<>();
                    for (ItemStack rewardStack : itemGroup) {
                        lore.add(Component.text("§6" + rewardStack.getAmount() + "§3x §f- ").append(rewardStack.displayName()).decoration(TextDecoration.ITALIC, false));
                    }
                    lore.add(Component.text("§f "));
                    lore.add(Component.text("§3Weight: §f" + reward.getWeight()).decoration(TextDecoration.ITALIC, false));
                    symbolMeta.lore(lore);
                    symbol.setItemMeta(symbolMeta);

                    inv.setItem(slot, symbol);

                } else if (reward instanceof CommandReward) {
                    ItemStack symbol = new ItemStack(Material.PAPER);
                    ItemMeta symbolMeta = symbol.getItemMeta();
                    symbolMeta.displayName(Component.text("§9Command Reward"));
                    List<Component> lore = new ArrayList<>();
                    lore.add(Component.text(((CommandReward) reward).getReward()).decoration(TextDecoration.ITALIC, false));
                    lore.add(Component.text("§f "));
                    lore.add(Component.text("§3Weight: §f" + reward.getWeight()).decoration(TextDecoration.ITALIC, false));
                    symbolMeta.lore(lore);
                    symbol.setItemMeta(symbolMeta);

                    inv.setItem(slot, symbol);

                } else if (reward instanceof MoneyReward) {
                    ItemStack symbol = new ItemStack(Material.GOLD_INGOT);
                    ItemMeta symbolMeta = symbol.getItemMeta();
                    symbolMeta.displayName(Component.text("§9Money Reward"));
                    List<Component> lore = new ArrayList<>();
                    lore.add(Component.text("§6Money: §9" + ((MoneyReward) reward).getReward()).decoration(TextDecoration.ITALIC, false));
                    lore.add(Component.text("§f "));
                    lore.add(Component.text("§3Weight: §f" + reward.getWeight()).decoration(TextDecoration.ITALIC, false));
                    symbolMeta.lore(lore);
                    symbol.setItemMeta(symbolMeta);

                    inv.setItem(slot, symbol);
                }
            }
        }
    }
}
