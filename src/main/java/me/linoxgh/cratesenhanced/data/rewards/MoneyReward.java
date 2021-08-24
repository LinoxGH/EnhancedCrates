package me.linoxgh.cratesenhanced.data.rewards;

import java.util.Map;

import me.linoxgh.cratesenhanced.CratesEnhanced;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class MoneyReward implements Reward<Double> {
    private double reward;
    private int weight;

    public MoneyReward(double reward, int weight) {
        this.reward = reward;
        this.weight = weight;
    }

    public MoneyReward(@NotNull Map<String, Object> data) {
        this.reward = (double) data.get("reward");
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
        if (!CratesEnhanced.isVaultEnabled() || CratesEnhanced.getEcon() == null) return false;
        CratesEnhanced.getEcon().depositPlayer(p, reward);
        return true;
    }

    @Override
    public @NotNull Double getReward() {
        return reward;
    }

    @Override
    public void setReward(@NotNull Double reward) {
        this.reward = reward;
    }

    @Override
    public @NotNull RewardType getRewardType() {
        return RewardType.MONEY;
    }
}
