package me.linoxgh.cratesenhanced;

import java.util.ArrayList;
import java.util.List;

import me.linoxgh.cratesenhanced.Data.BlockPosition;
import me.linoxgh.cratesenhanced.Data.Crate;
import me.linoxgh.cratesenhanced.Data.CrateStorage;
import me.linoxgh.cratesenhanced.Data.CrateType;
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
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class Listeners implements Listener {
    private final CratesEnhanced plugin;
    private final CrateStorage crates;
    private final List<BlockPosition> cooldowns;

    Listeners(@NotNull CratesEnhanced plugin, @NotNull CrateStorage crates) {
        this.plugin = plugin;
        this.crates = crates;
        cooldowns = new ArrayList<>();
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
        if (heldItem == null) return;

        if (heldItem.isSimilar(type.getKey())) {
            int newAmount = heldItem.getAmount() - type.getKey().getAmount();
            if (newAmount < 0) return;

            ItemStack drop = type.getRandomDrop();
            if (drop == null) return;

            if (cooldowns.contains(pos)) return;

            cooldowns.add(pos);
            playCrateAnimations(crate, p, drop, heldItem, newAmount);
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

    private void playCrateAnimations(@NotNull Crate crate, @NotNull Player p, @NotNull ItemStack drop, @NotNull ItemStack heldItem, int amount) {
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
                        cooldowns.remove(crate.getPos());
                    }, 20L);
                }, 20L);
            }, 20L);
        }, 20L);
    }
}
