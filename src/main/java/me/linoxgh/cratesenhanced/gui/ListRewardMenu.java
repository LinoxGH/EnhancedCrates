package me.linoxgh.cratesenhanced.gui;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import me.linoxgh.cratesenhanced.data.CrateType;
import me.linoxgh.cratesenhanced.data.rewards.CommandReward;
import me.linoxgh.cratesenhanced.data.rewards.ItemGroupReward;
import me.linoxgh.cratesenhanced.data.rewards.ItemReward;
import me.linoxgh.cratesenhanced.data.rewards.MoneyReward;
import me.linoxgh.cratesenhanced.data.rewards.Reward;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

public class ListRewardMenu {
    private final int[] BORDER_SLOTS = {45, 47, 48, 50, 51, 53};
    private final int[] CLICKABLE = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40, 41, 42, 43, 44, 46, 52};
    private final int[] REPLACEABLE = { };

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
    public int[] getClickableSlots() {
        return CLICKABLE;
    }
    public int[] getReplaceableSlots() {
        return REPLACEABLE;
    }

    public void populate() {
        int additional = type.getWeights().size() % 45;
        int pages = (type.getWeights().size() / 45) + (additional == 0 ? 0 : 1);
        inventories = new Inventory[pages];
        for (int i = 0; i < pages; i++) {
            inventories[i] = Bukkit.createInventory(null, 54, Component.text("§9Reward List §e- §a" + (i + 1) + "/" + pages));
        }

        List<Reward<?>> rewards = type.getWeights();

        for (int page = 0; page < inventories.length; page++) {
            Inventory inv = inventories[page];

            ItemStack border = new ItemStack(Material.GRAY_STAINED_GLASS_PANE);
            ItemMeta borderMeta = border.getItemMeta();
            borderMeta.setLocalizedName(" ");
            border.setItemMeta(borderMeta);
            for (int slot : BORDER_SLOTS) {
                inv.setItem(slot, border);
            }

            ItemStack help = new ItemStack(Material.OAK_SIGN);
            ItemMeta helpMeta = help.getItemMeta();
            helpMeta.setLocalizedName("§bClick a reward to remove it.");
            help.setItemMeta(helpMeta);
            inv.setItem(49, help);

            ItemStack previous = new ItemStack(Material.RED_DYE);
            ItemMeta previousMeta = previous.getItemMeta();
            previousMeta.setLocalizedName("§cClick to go to the previous page.");
            previous.setItemMeta(previousMeta);
            inv.setItem(46, previous);

            ItemStack next = new ItemStack(Material.GREEN_DYE);
            ItemMeta nextMeta = next.getItemMeta();
            nextMeta.setLocalizedName("§aClick to go to the next page.");
            next.setItemMeta(nextMeta);
            inv.setItem(52, next);

            for (int slot = 0; slot < 45; slot++) {
                Reward<?> reward = rewards.get(page * 45 + slot);
                if (reward instanceof ItemReward) {
                    inv.setItem(slot, ((ItemReward) reward).getReward());

                } else if (reward instanceof ItemGroupReward) {
                    ItemStack[] itemGroup = ((ItemGroupReward) reward).getReward();
                    ItemStack symbol = new ItemStack(itemGroup[ThreadLocalRandom.current().nextInt(itemGroup.length)].getType());
                    ItemMeta symbolMeta = symbol.getItemMeta();
                    symbolMeta.setLocalizedName("§9Item Group Reward");
                    List<Component> lore = new ArrayList<>();
                    lore.add(Component.text("§6[Click] §ato see items in this group."));
                    symbolMeta.lore(lore);
                    symbol.setItemMeta(symbolMeta);

                    inv.setItem(slot, symbol);

                } else if (reward instanceof CommandReward) {
                    ItemStack symbol = new ItemStack(Material.PAPER);
                    ItemMeta symbolMeta = symbol.getItemMeta();
                    symbolMeta.setLocalizedName("§9Item Group Reward");
                    List<Component> lore = new ArrayList<>();
                    lore.add(Component.text(((CommandReward) reward).getReward()));
                    symbolMeta.lore(lore);
                    symbol.setItemMeta(symbolMeta);

                    inv.setItem(slot, symbol);

                } else if (reward instanceof MoneyReward) {
                    ItemStack symbol = new ItemStack(Material.GOLD_INGOT);
                    ItemMeta symbolMeta = symbol.getItemMeta();
                    symbolMeta.setLocalizedName("§9Item Group Reward");
                    List<Component> lore = new ArrayList<>();
                    lore.add(Component.text("§6Money: §9" + ((MoneyReward) reward).getReward()));
                    symbolMeta.lore(lore);
                    symbol.setItemMeta(symbolMeta);

                    inv.setItem(slot, symbol);
                }
            }
        }
    }
}
