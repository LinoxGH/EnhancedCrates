package me.linoxgh.crates.Data;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.ThreadLocalRandom;

import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class CrateType {
    private TreeMap<Integer, ItemStack> drops;
    private final HashMap<ItemStack, Integer> weights;
    private Key key;
    private int totalWeight;

    public CrateType(@NotNull Key key) {
        this.key = key;

        drops = new TreeMap<>();
        weights = new HashMap<>();
    }

    public @Nullable ItemStack getRandomDrop() {
        if (drops.isEmpty()) return null;
        int random = ThreadLocalRandom.current().nextInt(totalWeight);
        return drops.lowerEntry(random).getValue();
    }

    public void addDrop(int weight, @NotNull ItemStack drop) {
        drops.put((drops.isEmpty() ? 0 : drops.lastKey()) + weight, drop);
        totalWeight += weight;
    }
    public void deleteDrop(@NotNull ItemStack drop) {
        TreeMap<Integer, ItemStack> newDrops = new TreeMap<>();
        totalWeight -= weights.get(drop);
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
