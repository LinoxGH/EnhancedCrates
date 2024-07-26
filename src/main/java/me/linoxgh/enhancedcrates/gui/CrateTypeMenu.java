package me.linoxgh.enhancedcrates.gui;

import java.util.ArrayList;
import java.util.List;

import me.linoxgh.enhancedcrates.data.CrateType;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

public class CrateTypeMenu {
    private final int[] BORDER_SLOTS = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 17, 18, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35};
    private final int[] CLICKABLE = {11, 12, 20, 21, 15};
    private final int[] REPLACEABLE = {24};

    private final Inventory inv;
    private final CrateType type;

    public CrateTypeMenu(@NotNull CrateType type) {
        this.type = type;
        inv = Bukkit.createInventory(null, 36, Component.text("§3CratesEnhanced §e- §9" + type.getName()));
        populate();
    }

    public @NotNull Inventory getInv() {
        return inv;
    }
    public int[] getClickableSlots() {
        return CLICKABLE;
    }
    public int[] getReplaceableSlots() {
        return REPLACEABLE;
    }

    public void populate() {
        ItemStack border = new ItemStack(Material.GRAY_STAINED_GLASS_PANE);
        ItemMeta borderMeta = border.getItemMeta();
        borderMeta.displayName(Component.text(" "));
        border.setItemMeta(borderMeta);
        for (int slot : BORDER_SLOTS) {
            inv.setItem(slot, border);
        }

        ItemStack addCommandReward = new ItemStack(Material.WRITABLE_BOOK);
        ItemMeta addCommandRewardMeta = addCommandReward.getItemMeta();
        addCommandRewardMeta.displayName(Component.text("§bPress to add a command reward"));
        addCommandReward.setItemMeta(addCommandRewardMeta);
        inv.setItem(11, addCommandReward);

        ItemStack addItemReward = new ItemStack(Material.DIAMOND);
        ItemMeta addItemRewardMeta = addItemReward.getItemMeta();
        addItemRewardMeta.displayName(Component.text("§bPress to add an item stack reward"));
        addItemReward.setItemMeta(addItemRewardMeta);
        inv.setItem(12, addItemReward);

        ItemStack addMoneyReward = new ItemStack(Material.GOLD_INGOT);
        ItemMeta addMoneyRewardMeta = addMoneyReward.getItemMeta();
        addMoneyRewardMeta.displayName(Component.text("§bPress to add a money reward"));
        List<Component> addMoneyRewardLore = new ArrayList<>();
        addMoneyRewardLore.add(Component.text("§cThis requires Vault."));
        addMoneyReward.lore(addMoneyRewardLore);
        addMoneyReward.setItemMeta(addMoneyRewardMeta);
        inv.setItem(20, addMoneyReward);

        ItemStack addItemGroupReward = new ItemStack(Material.DIAMOND_BLOCK);
        ItemMeta addItemGroupRewardMeta = addItemGroupReward.getItemMeta();
        addItemGroupRewardMeta.displayName(Component.text("§bPress to add a group of item stacks as reward"));
        addItemGroupReward.setItemMeta(addItemGroupRewardMeta);
        inv.setItem(21, addItemGroupReward);

        ItemStack rewardList = new ItemStack(Material.BOOK);
        ItemMeta rewardListMeta = rewardList.getItemMeta();
        rewardListMeta.displayName(Component.text("§bPress to see a list of possible rewards"));
        rewardList.setItemMeta(rewardListMeta);
        inv.setItem(15, rewardList);

        ItemStack changeKey = type.getKey();
        inv.setItem(24, changeKey);
    }
}
