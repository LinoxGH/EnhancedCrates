package me.linoxgh.cratesenhanced.data.rewards;

import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class CommandReward implements Reward<String> {
    private String reward;
    private int weight;

    public CommandReward(@NotNull String reward, int weight) {
        this.reward = reward;
        this.weight = weight;
    }

    public CommandReward(@NotNull Map<String, Object> data) {
        this.reward = (String) data.get("reward");
        this.weight = (int) data.get("weight");
    }

    @Override
    public int getWeight() {
        return weight;
    }
    @Override
    public void setWeight(int weight) {
        this.weight = weight;
    }

    @Override
    public boolean giveReward(@NotNull Player p, @NotNull Location crateLocation) {
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), reward);
        return true;
    }

    @Override
    public @NotNull String getReward() {
        return reward;
    }

    @Override
    public void setReward(@NotNull String reward) {
        this.reward = reward;
    }

    @Override
    public @NotNull RewardType getRewardType() {
        return RewardType.COMMAND;
    }
}
