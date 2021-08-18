package me.linoxgh.crates.Data;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class CrateType {

    private int totalWeight;

    private TreeMap<Integer, ItemStack> drops;
    private HashMap<ItemStack, Integer> weights;
    private Key key;

    public CrateType(@NotNull Key key) {
        this.key = key;

        drops = new TreeMap<>();
        weights = new HashMap<>();
    }

    public int getTotalWeight() { return totalWeight; }
    public @NotNull TreeMap<Integer, ItemStack> getDrops() {
        return drops;
    }

    public void addDrop(int weight, @NotNull ItemStack drop) {
        drops.put(drops.lastKey() + weight, drop);
    }
    public void deleteDrop(@NotNull ItemStack drop) {
        TreeMap<Integer, ItemStack> newDrops = new TreeMap<>();
        weights.remove(drop);

        int key = 0;
        for (Map.Entry<ItemStack, Integer> entry : weights.entrySet()) {
            newDrops.put(key, entry.getKey());
            key += entry.getValue();
        }

        this.drops = newDrops;
    }

    public @NotNull Key getKey() {
        return key;
    }
    public void setKey(@NotNull Key key) {
        this.key = key;
    }

}
