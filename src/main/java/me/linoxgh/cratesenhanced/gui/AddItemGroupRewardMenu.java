package me.linoxgh.cratesenhanced.gui;

import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

public class AddItemGroupRewardMenu {
    private final int[] BORDER_SLOTS = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 17, 18, 19, 20, 21, 23, 24, 25, 26, 27, 35, 36, 37, 38, 39, 40, 41, 42, 43, 44};
    private final int[] CLICKABLE = {10, 11, 12, 14, 15, 16};
    private final int[] REPLACEABLE = {28, 29, 30, 31, 32, 33, 34};

    private final Inventory inv;

    public AddItemGroupRewardMenu() {
        inv = Bukkit.createInventory(null, 45, Component.text("§9Adding an Item Group Reward"));
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
        borderMeta.setLocalizedName(" ");
        border.setItemMeta(borderMeta);
        for (int slot : BORDER_SLOTS) {
            inv.setItem(slot, border);
        }

        ItemStack sub100 = new ItemStack(Material.RED_WOOL);
        ItemMeta sub100Meta = sub100.getItemMeta();
        sub100Meta.setLocalizedName("§cPress to decrease weight by 100");
        sub100.setItemMeta(sub100Meta);
        inv.setItem(10, sub100);

        ItemStack sub10 = new ItemStack(Material.RED_CARPET);
        ItemMeta sub10Meta = sub10.getItemMeta();
        sub10Meta.setLocalizedName("§cPress to decrease weight by 10");
        sub10.setItemMeta(sub10Meta);
        inv.setItem(11, sub10);

        ItemStack sub1 = new ItemStack(Material.RED_DYE);
        ItemMeta sub1Meta = sub1.getItemMeta();
        sub1Meta.setLocalizedName("§cPress to decrease weight by 1");
        sub1.setItemMeta(sub1Meta);
        inv.setItem(12, sub1);

        ItemStack weight = new ItemStack(Material.ANVIL);
        ItemMeta weightMeta = weight.getItemMeta();
        weightMeta.setLocalizedName("§eWeight: §f0");
        weight.setItemMeta(weightMeta);
        inv.setItem(13, weight);

        ItemStack add1 = new ItemStack(Material.GREEN_DYE);
        ItemMeta add1Meta = add1.getItemMeta();
        add1Meta.setLocalizedName("§aPress to increase weight by 1");
        add1.setItemMeta(add1Meta);
        inv.setItem(14, add1);

        ItemStack add10 = new ItemStack(Material.GREEN_CARPET);
        ItemMeta add10Meta = add10.getItemMeta();
        add10Meta.setLocalizedName("§aPress to increase weight by 10");
        add10.setItemMeta(add10Meta);
        inv.setItem(15, add10);

        ItemStack add100 = new ItemStack(Material.GREEN_WOOL);
        ItemMeta add100Meta = add100.getItemMeta();
        add100Meta.setLocalizedName("§aPress to increase weight by 100");
        add100.setItemMeta(add100Meta);
        inv.setItem(16, add100);

        ItemStack help = new ItemStack(Material.LIGHT_BLUE_STAINED_GLASS_PANE);
        ItemMeta helpMeta = help.getItemMeta();
        helpMeta.setLocalizedName("§bPut the item stacks in the empty slots");
        help.setItemMeta(helpMeta);
        inv.setItem(22, help);
    }
}
