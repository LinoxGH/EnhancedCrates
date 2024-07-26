package me.linoxgh.enhancedcrates.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.ThreadLocalRandom;

import me.linoxgh.enhancedcrates.data.rewards.Reward;
import me.linoxgh.enhancedcrates.gui.CrateTypeMenu;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class CrateType implements ConfigurationSerializable {
    private final String name;
    private TreeMap<Integer, Reward<?>> rewards;
    private final List<Reward<?>> weights;
    private ItemStack key;
    private final CrateTypeMenu menu;

    public CrateType(@NotNull String name, @NotNull ItemStack key) {
        this.name = name;
        this.key = key;

        rewards = new TreeMap<>();
        weights = new ArrayList<>();

        menu = new CrateTypeMenu(this);
    }

    public CrateType(@NotNull Map<String, Object> data) {
        this.name = (String) data.get("name");
        this.key = (ItemStack) data.get("key");

        this.weights = (List<Reward<?>>) data.get("weights");
        this.rewards = (TreeMap<Integer, Reward<?>>) data.get("rewards");

        menu = new CrateTypeMenu(this);
    }

    public @NotNull String getName() {
        return name;
    }
    public @NotNull List<Reward<?>> getWeights() {
        return weights;
    }
    public @Nullable Reward<?> getRandomReward() {
        if (rewards == null || rewards.isEmpty()) return null;
        int random = ThreadLocalRandom.current().nextInt(rewards.lastKey());
        return rewards.higherEntry(random).getValue();
    }
    public @NotNull CrateTypeMenu getMenu() {
        return menu;
    }

    public void addReward(int weight, @NotNull Reward reward) {
        rewards.put((rewards.isEmpty() ? 0 : rewards.lastKey()) + weight, reward);
        weights.add(reward);
    }
    public void removeReward(@NotNull Reward<?> reward) {
        TreeMap<Integer, Reward<?>> newRewards = new TreeMap<>();
        weights.remove(reward);

        int key = 0;
        for (Reward<?> entry : weights) {
            key += entry.getWeight();
            newRewards.put(key, entry);
        }

        this.rewards = newRewards;
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
        result.put("name", name);
        result.put("key", key);
        result.put("weights", weights);
        result.put("rewards", rewards);
        return result;
    }
}
