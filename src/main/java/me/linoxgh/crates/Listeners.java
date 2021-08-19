package me.linoxgh.crates;

import java.util.Map;

import me.linoxgh.crates.Data.BlockPosition;
import me.linoxgh.crates.Data.Crate;
import me.linoxgh.crates.Data.CrateStorage;
import me.linoxgh.crates.Data.CrateType;
import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class Listeners implements Listener {
    private final CrateStorage crates;

    Listeners(@NotNull Crates plugin, @NotNull CrateStorage crates) {
        this.crates = crates;
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent e) {
        if (e.getAction() != Action.RIGHT_CLICK_BLOCK) return;
        if (e.getHand() != EquipmentSlot.HAND) return;

        Player p = e.getPlayer();
        Block b = e.getClickedBlock();
        if (b == null) return;

        Crate crate = crates.getCrate(new BlockPosition(b.getX(), b.getY(), b.getZ(), b.getWorld().getName()));
        if (crate == null) return;

        CrateType type = crates.getCrateType(crate.getCrateType());
        if (type == null) return;

        if (!p.hasPermission("crates.use.*") && !p.hasPermission("crates.use." + crate.getCrateType())) return;

        ItemStack heldItem = e.getItem();
        if (heldItem == null) return;

        if (heldItem.isSimilar(type.getKey())) {
            int newAmount = heldItem.getAmount() - type.getKey().getAmount();
            if (newAmount < 0) return;

            ItemStack drop = type.getRandomDrop();
            if (drop == null) return;

            heldItem.setAmount(newAmount);
            for (Map.Entry<Integer, ItemStack> entry : p.getInventory().addItem(drop).entrySet()) {
                p.getWorld().dropItem(p.getLocation(), entry.getValue());
            }

            e.setCancelled(true);
        }
    }
}
