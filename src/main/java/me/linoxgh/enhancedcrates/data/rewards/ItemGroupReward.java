package me.linoxgh.enhancedcrates.data.rewards;

import java.util.HashMap;
import java.util.Map;

import me.linoxgh.enhancedcrates.EnhancedCrates;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

public class ItemGroupReward implements Reward<ItemStack[]> {
    private ItemStack[] reward;
    private int weight;

    public ItemGroupReward(@NotNull ItemStack[] reward, int weight) {
        this.reward = reward;
        this.weight = weight;
    }

    public ItemGroupReward(@NotNull Map<String, Object> data) {
        this.reward = (ItemStack[]) data.get("reward");
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
        Location center = crateLocation.clone().toCenterLocation();
        Location topLoc = center.clone().add(0,0.5,0);
        Vector direction = p.getLocation().subtract(center).toVector().setY(0).normalize().multiply(0.12);
        direction.setY(direction.getY() + 0.1);

        for (ItemStack reward : this.reward) {

            int iterations = Math.min(reward.getAmount(), 16);
            int itemsPerIteration = (int)Math.ceil((double)reward.getAmount() / iterations);
            int toDrop = reward.getAmount();
            for (int i = 0;i < reward.getAmount();i++) {
                int now = Math.min(toDrop, itemsPerIteration);
                if (now <= 0) break;
                toDrop -= now;
                Bukkit.getScheduler().runTaskLater(EnhancedCrates.getPlugin(EnhancedCrates.class), () -> {
                    crateLocation.getWorld().playSound(crateLocation, Sound.ENTITY_ITEM_PICKUP, SoundCategory.BLOCKS, 1, 1);
                    crateLocation.getWorld().spawnEntity(
                            topLoc,
                            EntityType.DROPPED_ITEM,
                            CreatureSpawnEvent.SpawnReason.CUSTOM,
                            (entity) -> {
                                Item item = (Item) entity;
                                item.setOwner(p.getUniqueId());
                                item.setCanMobPickup(false);
                                item.setCanPlayerPickup(true);
                                item.setWillAge(true);
                                item.setPickupDelay(20);
                                item.setItemStack(reward.clone().asQuantity(now));
                                EnhancedCrates.getCrateListeners().markUnmergeable(item);
                                item.setVelocity(item.getVelocity().multiply(0.5).add(direction));
                            });
                }, i);
            }
        }

        return true;
    }

    @Override
    public boolean giveReward(@NotNull Player p) {
        HashMap<Integer, ItemStack> unfits = p.getInventory().addItem(reward.clone());
        if (!(unfits.isEmpty())) {
            for (Map.Entry<Integer, ItemStack> entry : unfits.entrySet()) {
                p.getLocation().getWorld().dropItem(p.getLocation(), entry.getValue());
            }
        }
        return true;
    }

    @Override
    public ItemStack @NotNull [] getReward() {
        return reward;
    }

    @Override
    public void setReward(ItemStack @NotNull [] reward) {
        this.reward = reward;
    }

    @Override
    public @NotNull RewardType getRewardType() {
        return RewardType.ITEM_GROUP;
    }
}
