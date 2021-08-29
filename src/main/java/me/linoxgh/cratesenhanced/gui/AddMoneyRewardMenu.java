package me.linoxgh.cratesenhanced.gui;

import java.util.ArrayList;
import java.util.List;

import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

public class AddMoneyRewardMenu {
    private final int[] BORDER_SLOTS = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 17, 18, 19, 20, 21, 23, 24, 25, 26, 27, 35, 36, 37, 38, 39, 40, 41, 42, 43, 44};
    private static final int[] CLICKABLE = {10, 11, 12, 14, 15, 16, 28, 29, 30, 32, 33, 34};
    private static final int[] REPLACEABLE = { };

    private final Inventory inv;

    public AddMoneyRewardMenu() {
        inv = Bukkit.createInventory(null, 45, Component.text("§9Adding a Money Reward"));
        populate();
    }

    public @NotNull Inventory getInv() {
        return inv;
    }
    public static int[] getClickableSlots() {
        return CLICKABLE;
    }
    public static int[] getReplaceableSlots() {
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

        ItemStack sub100 = new ItemStack(Material.RED_WOOL);
        ItemMeta sub100Meta = sub100.getItemMeta();
        sub100Meta.displayName(Component.text("§cPress to decrease weight by 100"));
        sub100.setItemMeta(sub100Meta);
        inv.setItem(10, sub100);

        ItemStack sub10 = new ItemStack(Material.RED_CARPET);
        ItemMeta sub10Meta = sub10.getItemMeta();
        sub10Meta.displayName(Component.text("§cPress to decrease weight by 10"));
        sub10.setItemMeta(sub10Meta);
        inv.setItem(11, sub10);

        ItemStack sub1 = new ItemStack(Material.RED_DYE);
        ItemMeta sub1Meta = sub1.getItemMeta();
        sub1Meta.displayName(Component.text("§cPress to decrease weight by 1"));
        sub1.setItemMeta(sub1Meta);
        inv.setItem(12, sub1);

        ItemStack weight = new ItemStack(Material.ANVIL);
        ItemMeta weightMeta = weight.getItemMeta();
        weightMeta.displayName(Component.text("§eWeight: §f0"));
        weight.setItemMeta(weightMeta);
        inv.setItem(13, weight);

        ItemStack add1 = new ItemStack(Material.GREEN_DYE);
        ItemMeta add1Meta = add1.getItemMeta();
        add1Meta.displayName(Component.text("§aPress to increase weight by 1"));
        add1.setItemMeta(add1Meta);
        inv.setItem(14, add1);

        ItemStack add10 = new ItemStack(Material.GREEN_CARPET);
        ItemMeta add10Meta = add10.getItemMeta();
        add10Meta.displayName(Component.text("§aPress to increase weight by 10"));
        add10.setItemMeta(add10Meta);
        inv.setItem(15, add10);

        ItemStack add100 = new ItemStack(Material.GREEN_WOOL);
        ItemMeta add100Meta = add100.getItemMeta();
        add100Meta.displayName(Component.text("§aPress to increase weight by 100"));
        add100.setItemMeta(add100Meta);
        inv.setItem(16, add100);

        ItemStack help = new ItemStack(Material.LIGHT_BLUE_STAINED_GLASS_PANE);
        ItemMeta helpMeta = help.getItemMeta();
        helpMeta.displayName(Component.text("§bAlter the amount of money to give"));
        help.setItemMeta(helpMeta);
        inv.setItem(22, help);

        ItemStack add100M = new ItemStack(Material.GOLD_BLOCK);
        ItemMeta add100MMeta = add100M.getItemMeta();
        add100MMeta.displayName(Component.text("§6Press to increase money by 100"));
        List<Component> lore100M = new ArrayList<>();
        lore100M.add(Component.text("§6Shift Click to increase money by 1000"));
        add100MMeta.lore(lore100M);
        add100M.setItemMeta(add100MMeta);
        inv.setItem(34, add100M);

        ItemStack add1M = new ItemStack(Material.GOLD_INGOT);
        ItemMeta add1MMeta = add1M.getItemMeta();
        add1MMeta.displayName(Component.text("§6Press to increase money by 1"));
        List<Component> lore1M = new ArrayList<>();
        lore1M.add(Component.text("§6Shift Click to increase money by 10"));
        add1MMeta.lore(lore1M);
        add1M.setItemMeta(add1MMeta);
        inv.setItem(33, add1M);

        ItemStack add01M = new ItemStack(Material.GOLD_NUGGET);
        ItemMeta add01MMeta = add01M.getItemMeta();
        add01MMeta.displayName(Component.text("§6Press to increase money by 0.01"));
        List<Component> lore01M = new ArrayList<>();
        lore01M.add(Component.text("§6Shift Click to increase money by 0.1"));
        add01MMeta.lore(lore01M);
        add01M.setItemMeta(add01MMeta);
        inv.setItem(32, add01M);

        ItemStack money = new ItemStack(Material.EMERALD);
        ItemMeta moneyMeta = money.getItemMeta();
        moneyMeta.displayName(Component.text("§6Money: §f0"));
        money.setItemMeta(moneyMeta);
        inv.setItem(31, money);

        ItemStack sub01M = new ItemStack(Material.GOLD_NUGGET);
        ItemMeta sub01MMeta = sub01M.getItemMeta();
        sub01MMeta.displayName(Component.text("§6Press to decrease money by 0.01"));
        List<Component> loreSub01M = new ArrayList<>();
        loreSub01M.add(Component.text("§6Shift Click to decrease money by 0.1"));
        sub01MMeta.lore(loreSub01M);
        sub01M.setItemMeta(sub01MMeta);
        inv.setItem(30, sub01M);

        ItemStack sub1M = new ItemStack(Material.GOLD_INGOT);
        ItemMeta sub1MMeta = sub1M.getItemMeta();
        sub1MMeta.displayName(Component.text("§6Press to decrease money by 1"));
        List<Component> loreSub1M = new ArrayList<>();
        loreSub1M.add(Component.text("§6Shift Click to decrease money by 10"));
        sub1MMeta.lore(loreSub1M);
        sub1M.setItemMeta(sub1MMeta);
        inv.setItem(29, sub1M);

        ItemStack sub100M = new ItemStack(Material.GOLD_BLOCK);
        ItemMeta sub100MMeta = sub100M.getItemMeta();
        sub100MMeta.displayName(Component.text("§6Press to decrease money by 100"));
        List<Component> loreSub100M = new ArrayList<>();
        loreSub100M.add(Component.text("§6Shift Click to decrease money by 1000"));
        sub100MMeta.lore(loreSub100M);
        sub100M.setItemMeta(sub100MMeta);
        inv.setItem(28, sub100M);
    }
}
