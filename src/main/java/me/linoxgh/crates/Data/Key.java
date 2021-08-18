package me.linoxgh.crates.Data;

import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class Key {

    private ItemStack item;

    public Key(@NotNull ItemStack item) {
        this.item = item;
    }

    public @NotNull ItemStack getItem() {
        return item;
    }
    public void setItem(@NotNull ItemStack item) {
        this.item = item;
    }
}
