package me.linoxgh.enhancedcrates;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import com.destroystokyo.paper.event.entity.EntityRemoveFromWorldEvent;
import me.linoxgh.enhancedcrates.data.BlockPosition;
import me.linoxgh.enhancedcrates.data.Crate;
import me.linoxgh.enhancedcrates.data.CrateStorage;
import me.linoxgh.enhancedcrates.data.CrateType;
import me.linoxgh.enhancedcrates.data.MessageStorage;
import me.linoxgh.enhancedcrates.data.rewards.MoneyReward;
import me.linoxgh.enhancedcrates.data.rewards.Reward;
import me.linoxgh.enhancedcrates.gui.MenuType;
import me.linoxgh.enhancedcrates.gui.SimplifiedListRewardMenu;
import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.Lidded;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.ItemMergeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class CrateListeners implements Listener {
    private final EnhancedCrates plugin;
    private final CrateStorage crates;
    private final MessageStorage messages;
    private final Set<BlockPosition> cooldowns;

    CrateListeners(@NotNull EnhancedCrates plugin, @NotNull CrateStorage crates, @NotNull MessageStorage messages) {
        this.plugin = plugin;
        this.crates = crates;
        this.messages = messages;
        cooldowns = new HashSet<>();
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    private final Set<UUID> unmergeableStacks = new HashSet<>();
    public void markUnmergeable(@NotNull Item item) {
        unmergeableStacks.add(item.getUniqueId());
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent e) {
        if (e.useInteractedBlock() == Event.Result.DENY) return;
        if (e.useItemInHand() == Event.Result.DENY) return;

        if (e.getAction() != Action.RIGHT_CLICK_BLOCK) return;
        if (e.getHand() != EquipmentSlot.HAND) return;

        Player p = e.getPlayer();
        Block b = e.getClickedBlock();
        if (b == null) return;

        BlockPosition pos = new BlockPosition(b.getX(), b.getY(), b.getZ(), b.getWorld().getName());
        Crate crate = crates.getCrate(pos);
        if (crate == null) return;

        CrateType type = crates.getCrateType(crate.getCrateType());
        if (type == null) return;

        e.setUseInteractedBlock(Event.Result.DENY);
        e.setUseItemInHand(Event.Result.DENY);

        if (!p.hasPermission("crates.use.*") && !p.hasPermission("crates.use." + crate.getCrateType())) return;

        ItemStack heldItem = e.getItem();
        if ((heldItem == null || !heldItem.isSimilar(type.getKey()))
                && ((p.hasPermission("crates.view.*") || p.hasPermission("crates.view." + crate.getCrateType())))) {
            SimplifiedListRewardMenu menu = new SimplifiedListRewardMenu(type);
            if (menu.getInventories().length == 0) return;
            p.openInventory(menu.getInventories()[0]);
            plugin.getGuiTracker().addToSimplifiedListTracker(p.getUniqueId(), menu);
            plugin.getGuiTracker().addToMenuTracker(p.getUniqueId(), MenuType.SIMPLIFIED_LIST_REWARD);

        } else if (heldItem != null && heldItem.isSimilar(type.getKey())) {
            int newAmount = heldItem.getAmount() - type.getKey().getAmount();
            if (newAmount < 0) return;

            Reward<?> reward = type.getRandomReward();
            if (reward == null) return;

            if (!cooldowns.add(pos)) return;

            playCrateAnimations(crate, p, reward, heldItem, newAmount);
        }
    }

    @EventHandler
    public void onBreak(BlockBreakEvent e) {
        if (e.isCancelled()) return;

        Player p = e.getPlayer();
        Block b = e.getBlock();

        Crate crate = crates.getCrate(new BlockPosition(b.getX(), b.getY(), b.getZ(), b.getWorld().getName()));
        if (crate == null) return;

        if (!p.hasPermission("crates.delete")) {
            e.setCancelled(true);
            return;
        }

        crates.removeCrate(crate.getName());
        p.sendMessage(messages.getMessage("gameplay.crate-interaction.break"));
    }

    @EventHandler
    public void onPlace(BlockPlaceEvent e) {
        for (CrateType type : crates.getCrateTypes().values()) {
            if (e.getItemInHand().isSimilar(type.getKey())) {
                e.setCancelled(true);
                return;
            }
        }
    }

    @EventHandler
    public void onMerge(ItemMergeEvent e) {
        if (
                unmergeableStacks.contains(e.getEntity().getUniqueId())
             || unmergeableStacks.contains(e.getTarget().getUniqueId())
        ) e.setCancelled(true);
    }

    @EventHandler
    public void onItemRemoves(EntityRemoveFromWorldEvent e) {
        if (e.getEntity() instanceof Item) unmergeableStacks.remove(e.getEntity().getUniqueId());
    }

    private void playCrateAnimations(@NotNull Crate crate, @NotNull Player p, @NotNull Reward<?> reward, @NotNull ItemStack heldItem, int amount) {
        heldItem.setAmount(amount);

        BlockPosition pos = crate.getPos();
        Location loc = new Location(Bukkit.getWorld(pos.getWorld()), pos.getX(), pos.getY(), pos.getZ());
        loc.getWorld().playEffect(loc, Effect.MOBSPAWNER_FLAMES, 1);
        loc.getWorld().playSound(loc, Sound.BLOCK_WOODEN_BUTTON_CLICK_ON, 1F, 1F);

        Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> {
            loc.getWorld().playEffect(loc, Effect.MOBSPAWNER_FLAMES, 1);
            loc.getWorld().playSound(loc, Sound.BLOCK_WOODEN_BUTTON_CLICK_ON, 1F, 1F);

            Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> {
                loc.getWorld().playEffect(loc, Effect.MOBSPAWNER_FLAMES, 1);
                loc.getWorld().playSound(loc, Sound.BLOCK_WOODEN_BUTTON_CLICK_ON, 2F, 1F);

                Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> {
                    loc.getWorld().playEffect(loc, Effect.MOBSPAWNER_FLAMES, 1);
                    loc.getWorld().playSound(loc, Sound.BLOCK_WOODEN_BUTTON_CLICK_ON, 3F, 1F);
                    BlockState b = loc.getBlock().getState();

                    boolean isChest = b.getType() == Material.CHEST || b.getType() == Material.TRAPPED_CHEST || b.getType() == Material.ENDER_CHEST;
                    if (isChest) {
                        Lidded chest = (Lidded) b;
                        chest.open();
                    }

                    Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> {
                        Location topLoc = loc.clone().toCenterLocation().add(0,0.75,0);

                        loc.getWorld().spawnParticle(Particle.EXPLOSION_LARGE, topLoc, 1);
                        loc.getWorld().playSound(loc, Sound.ENTITY_ARROW_HIT_PLAYER, 1F, 1F);
                        reward.giveReward(p, loc);
                        if (reward instanceof MoneyReward) {
                            p.sendMessage(messages.getMessage("gameplay.crate-interaction.earn-money").replace("%MONEY%", reward.getReward().toString()));
                        }
                        Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> {
                            if (isChest) {
                                Lidded chest = (Lidded) b;
                                chest.close();
                            }
                            cooldowns.remove(crate.getPos());
                        }, 20L);
                    }, 5L);
                }, 20L);
            }, 20L);
        }, 20L);
    }
}
