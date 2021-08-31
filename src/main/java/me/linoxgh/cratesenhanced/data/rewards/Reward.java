package me.linoxgh.cratesenhanced.data.rewards;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Location;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public interface Reward<T> extends ConfigurationSerializable {

    /**
     * Gets the weight of the reward.
     *
     * @return Weight
     */
    int getWeight();

    /**
     * Sets the weight of the reward.
     *
     * @param weight New weight
     */
    void setWeight(int weight);

    /**
     * Gives this reward to a player.
     *
     * @param p The player to give
     * @param crateLocation The location of the opened crate
     * @return Success
     */
    boolean giveReward(@NotNull Player p, @NotNull Location crateLocation);

    /**
     * Gives this reward to a player.
     *
     * @param p The player to give
     * @return Success
     */
    boolean giveReward(@NotNull Player p);

    /**
     * Gets the reward.
     *
     * @return The reward
     */
    @NotNull T getReward();

    /**
     * Sets the reward.
     *
     * @param reward The reward
     */
    void setReward(@NotNull T reward);

    /**
     * Gives which type this reward is.
     *
     * @return Reward type
     */
    @NotNull RewardType getRewardType();

    default @NotNull Map<String, Object> serialize() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("reward", getReward());
        result.put("weight", getWeight());
        return result;
    }
}
