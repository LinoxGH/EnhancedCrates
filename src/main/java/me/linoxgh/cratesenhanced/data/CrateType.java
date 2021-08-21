package me.linoxgh.cratesenhanced.data;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.ThreadLocalRandom;

import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class CrateType implements ConfigurationSerializable {
    private TreeMap<Integer, ItemStack> drops;
    private final HashMap<ItemStack, Integer> weights;
    private ItemStack key;

    public CrateType(@NotNull ItemStack key) {
        this.key = key;

        drops = new TreeMap<>();
        weights = new HashMap<>();
    }

    public CrateType(@NotNull Map<String, Object> data) {
        this.key = (ItemStack) data.get("key");
        this.weights = (HashMap<ItemStack, Integer>) data.get("weights");
        this.drops = (TreeMap<Integer, ItemStack>) data.get("drops");
    }

    public @NotNull HashMap<ItemStack, Integer> getWeights() {
        return weights;
    }
    public @Nullable ItemStack getRandomDrop() {
        if (drops.isEmpty()) return null;
        int random = ThreadLocalRandom.current().nextInt(drops.lastKey());
        return drops.higherEntry(random).getValue().clone();
    }

    public void addDrop(int weight, @NotNull ItemStack drop) {
        drops.put((drops.isEmpty() ? 0 : drops.lastKey()) + weight, drop.clone());
        weights.put(drop, weight);
    }
    public void deleteDrop(@NotNull ItemStack drop) {
        TreeMap<Integer, ItemStack> newDrops = new TreeMap<>();
        ItemStack clone = drop.clone();

        int key = 0;
        for (Map.Entry<ItemStack, Integer> entry : weights.entrySet()) {
            if (entry.getKey().equals(clone)) {
                weights.remove(entry.getKey());
                continue;
            }
            key += entry.getValue();
            newDrops.put(key, entry.getKey());
        }

        this.drops = newDrops;
    }

    public @NotNull ItemStack getKey() {
        return key.clone();
    }
    public void setKey(@NotNull ItemStack key) {
        this.key = key.clone();
    }

    @Override
    public @NotNull Map<String, Object> serialize() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("key", key);
        result.put("weights", weights);
        result.put("drops", drops);
        return result;
    }
}
