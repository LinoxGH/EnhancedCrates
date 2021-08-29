package me.linoxgh.cratesenhanced;

import java.util.HashSet;
import java.util.Set;

import me.linoxgh.cratesenhanced.data.BlockPosition;
import me.linoxgh.cratesenhanced.data.Crate;
import me.linoxgh.cratesenhanced.data.CrateStorage;
import me.linoxgh.cratesenhanced.data.CrateType;
import me.linoxgh.cratesenhanced.data.rewards.Reward;
import me.linoxgh.cratesenhanced.gui.ListRewardMenu;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class CrateListeners implements Listener {
    private final CratesEnhanced plugin;
    private final CrateStorage crates;
    private final Set<BlockPosition> cooldowns;

    CrateListeners(@NotNull CratesEnhanced plugin, @NotNull CrateStorage crates) {
        this.plugin = plugin;
        this.crates = crates;
        cooldowns = new HashSet<>();
        Bukkit.getPluginManager().registerEvents(this, plugin);
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
        if (heldItem == null || !heldItem.isSimilar(type.getKey())) {
            ListRewardMenu menu = new ListRewardMenu(type);
            if (menu.getInventories().length == 0) return;
            p.openInventory(menu.getInventories()[0]);

        } else {
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

        if (!p.hasPermission("crates.delete")) return;

        crates.removeCrate(crate.getName());
        p.sendMessage("§aSuccessfully removed the crate.");
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

                    Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> {
                        Location topLoc = loc.set(loc.getX(), loc.getY() + 1D, loc.getZ()).toCenterLocation();
                        loc.getWorld().spawnParticle(Particle.EXPLOSION_LARGE, topLoc, 1);
                        loc.getWorld().playSound(loc, Sound.ENTITY_ARROW_HIT_PLAYER, 1F, 1F);

                        switch (reward.getRewardType()) {
                            case ITEM:
                                loc.getWorld().spawnEntity(
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
                                            item.setItemStack((ItemStack) reward.getReward());
                                        }
                                );
                                break;
                            case ITEM_GROUP:
                                for (ItemStack drop : ((ItemStack[]) reward.getReward())) {
                                    loc.getWorld().spawnEntity(
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
                                                item.setItemStack(drop);
                                            }
                                    );
                                }
                                break;
                            case COMMAND:
                                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), (String) reward.getReward());
                                break;
                            case MONEY:
                                Economy econ = CratesEnhanced.getEcon();
                                if (econ == null) break;
                                econ.depositPlayer(p, (double) reward.getReward());
                                break;
                        }
                        cooldowns.remove(crate.getPos());
                    }, 20L);
                }, 20L);
            }, 20L);
        }, 20L);
    }
}
