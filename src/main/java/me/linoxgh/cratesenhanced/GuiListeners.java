package me.linoxgh.cratesenhanced;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import me.linoxgh.cratesenhanced.data.CrateStorage;
import me.linoxgh.cratesenhanced.data.CrateType;
import me.linoxgh.cratesenhanced.data.rewards.CommandReward;
import me.linoxgh.cratesenhanced.data.rewards.ItemGroupReward;
import me.linoxgh.cratesenhanced.data.rewards.ItemReward;
import me.linoxgh.cratesenhanced.data.rewards.MoneyReward;
import me.linoxgh.cratesenhanced.data.rewards.Reward;
import me.linoxgh.cratesenhanced.gui.AddCommandRewardMenu;
import me.linoxgh.cratesenhanced.gui.AddItemGroupRewardMenu;
import me.linoxgh.cratesenhanced.gui.AddItemRewardMenu;
import me.linoxgh.cratesenhanced.gui.AddMoneyRewardMenu;
import me.linoxgh.cratesenhanced.gui.CrateTypeMenu;
import me.linoxgh.cratesenhanced.gui.GUITracker;
import me.linoxgh.cratesenhanced.gui.ListRewardMenu;
import me.linoxgh.cratesenhanced.gui.MenuType;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

public class GuiListeners implements Listener {
    private final CrateStorage crates;
    private final GUITracker guiTracker;

    GuiListeners(@NotNull CratesEnhanced plugin, @NotNull CrateStorage crates, @NotNull GUITracker guiTracker) {
        this.crates = crates;
        this.guiTracker = guiTracker;
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onClick(@NotNull InventoryClickEvent e) {
        if (e.getClickedInventory() == null) return;

        HumanEntity entity = e.getWhoClicked();
        if (!(entity instanceof Player)) return;
        Player p = (Player) entity;

        MenuType menu = guiTracker.getFromMenuTracker(p.getUniqueId());
        if (menu == null) return;
        e.setCancelled(true);

        switch (menu) {
            case CRATE_TYPE:
                processCrateTypeMenu(e, p);
                return;

            case LIST_REWARD:
                processListMenu(e, p);
                return;

            case ADD_ITEM_REWARD:
            case ADD_ITEM_GROUP_REWARD:
            case ADD_COMMAND_REWARD:
                processGenericRewardMenu(e, p, menu);
                return;

            case ADD_MONEY_REWARD:
                processMoneyRewardMenu(e, p);
        }
    }

    @EventHandler
    public void onDrag(@NotNull InventoryDragEvent e) {
        UUID id = e.getWhoClicked().getUniqueId();
        MenuType menu = guiTracker.getFromMenuTracker(id);
        if (menu == null) return;
        e.setCancelled(true);
    }

    @EventHandler
    public void onClose(@NotNull InventoryCloseEvent e) {
        UUID id = e.getPlayer().getUniqueId();
        MenuType menu = guiTracker.getFromMenuTracker(id);
        if (menu == null) return;
        guiTracker.removeFromMenuTracker(id);

        Inventory inv = e.getInventory();
        switch (menu) {
            case CRATE_TYPE:
                CrateType type = crates.getCrateType(ChatColor.stripColor(PlainTextComponentSerializer.plainText().serialize(e.getView().title())));
                if (type == null) return;

                ItemStack newKey = inv.getItem(24);
                if (newKey == null) return;
                type.setKey(newKey);
                break;

            case ADD_ITEM_REWARD:
                String name1 = guiTracker.getFromCrateTypeTracker(id);
                if (name1 == null) return;

                CrateType type1 = crates.getCrateType(name1);
                if (type1 == null) return;

                ItemStack rewardItem = inv.getItem(31);
                ItemStack weightItem1 = inv.getItem(13);
                if (weightItem1 == null) return;
                ItemMeta weightMeta1 = weightItem1.getItemMeta();
                if (weightMeta1 == null) return;
                String weightName1 = ChatColor.stripColor(PlainTextComponentSerializer.plainText().serialize(weightMeta1.displayName()));
                int weight1 = Integer.parseInt(weightName1.replace("Weight: ", ""));
                if (weight1 == 0) break;

                ItemReward itemReward = new ItemReward(rewardItem, weight1);
                type1.addReward(weight1, itemReward);
                break;

            case ADD_ITEM_GROUP_REWARD:
                String name2 = guiTracker.getFromCrateTypeTracker(id);
                if (name2 == null) return;

                CrateType type2 = crates.getCrateType(name2);
                if (type2 == null) return;

                List<ItemStack> rewardItems = new ArrayList<>();
                for (int slot = 28; slot < 35; slot++) {
                    ItemStack item = inv.getItem(slot);
                    if (item != null) rewardItems.add(item);
                }

                ItemStack weightItem2 = inv.getItem(13);
                if (weightItem2 == null) return;
                ItemMeta weightMeta2 = weightItem2.getItemMeta();
                if (weightMeta2 == null) return;
                String weightName2 = ChatColor.stripColor(PlainTextComponentSerializer.plainText().serialize(weightMeta2.displayName()));
                int weight2 = Integer.parseInt(weightName2.replace("Weight: ", ""));
                if (weight2 == 0) break;

                ItemGroupReward groupReward = new ItemGroupReward(rewardItems.toArray(new ItemStack[0]), weight2);
                type2.addReward(weight2, groupReward);
                break;

            case ADD_COMMAND_REWARD:
                String name3 = guiTracker.getFromCrateTypeTracker(id);
                if (name3 == null) return;

                CrateType type3 = crates.getCrateType(name3);
                if (type3 == null) return;

                ItemStack book = inv.getItem(31);
                if (book == null || (book.getType() != Material.WRITABLE_BOOK && book.getType() != Material.WRITTEN_BOOK)) return;
                BookMeta meta = (BookMeta) book.getItemMeta();
                String command = PlainTextComponentSerializer.plainText().serialize(meta.page(1));

                ItemStack weightItem3 = inv.getItem(13);
                if (weightItem3 == null) return;
                ItemMeta weightMeta3 = weightItem3.getItemMeta();
                if (weightMeta3 == null) return;
                String weightName3 = ChatColor.stripColor(PlainTextComponentSerializer.plainText().serialize(weightMeta3.displayName()));
                int weight3 = Integer.parseInt(weightName3.replace("Weight: ", ""));
                if (weight3 == 0) break;

                CommandReward cmdReward = new CommandReward(command, weight3);
                type3.addReward(weight3, cmdReward);
                break;

            case ADD_MONEY_REWARD:
                String name4 = guiTracker.getFromCrateTypeTracker(id);
                if (name4 == null) return;

                CrateType type4 = crates.getCrateType(name4);
                if (type4 == null) return;

                ItemStack moneyItem = inv.getItem(31);
                if (moneyItem == null) return;
                ItemMeta moneyMeta = moneyItem.getItemMeta();
                if (moneyMeta == null) return;
                String moneyName = ChatColor.stripColor(PlainTextComponentSerializer.plainText().serialize(moneyMeta.displayName()));
                double money = Double.parseDouble(moneyName.replace("Money: ", ""));

                ItemStack weightItem4 = inv.getItem(13);
                if (weightItem4 == null) return;
                ItemMeta weightMeta4 = weightItem4.getItemMeta();
                if (weightMeta4 == null) return;
                String weightName4 = ChatColor.stripColor(PlainTextComponentSerializer.plainText().serialize(weightMeta4.displayName()));
                int weight4 = Integer.parseInt(weightName4.replace("Weight: ", ""));
                if (weight4 == 0) break;

                MoneyReward moneyReward = new MoneyReward(money, weight4);
                type4.addReward(weight4, moneyReward);
                break;
        }
        cleanup(id);
    }

    @EventHandler
    public void onQuit(@NotNull PlayerQuitEvent e) {
        cleanup(e.getPlayer().getUniqueId());
    }

    private void processCrateTypeMenu(@NotNull InventoryClickEvent e, @NotNull Player p) {
        CrateType type = crates.getCrateType(ChatColor.stripColor(PlainTextComponentSerializer.plainText().serialize(e.getView().title())));
        if (type == null) return;

        CrateTypeMenu crateMenu = type.getMenu();
        for (int clickableSlot : crateMenu.getClickableSlots()) {
            if (e.getRawSlot() == clickableSlot) {

                UUID id = p.getUniqueId();
                switch (clickableSlot) {
                    case 11:
                        AddCommandRewardMenu cmd = new AddCommandRewardMenu();
                        p.openInventory(cmd.getInv());
                        guiTracker.removeFromMenuTracker(id);
                        guiTracker.addToMenuTracker(id, MenuType.ADD_COMMAND_REWARD);
                        guiTracker.addToCrateTypeTracker(id, type.getName());
                        return;

                    case 12:
                        AddItemRewardMenu item = new AddItemRewardMenu();
                        p.openInventory(item.getInv());
                        guiTracker.removeFromMenuTracker(id);
                        guiTracker.addToMenuTracker(id, MenuType.ADD_ITEM_REWARD);
                        guiTracker.addToCrateTypeTracker(id, type.getName());
                        return;

                    case 20:
                        AddMoneyRewardMenu money = new AddMoneyRewardMenu();
                        p.openInventory(money.getInv());
                        guiTracker.removeFromMenuTracker(id);
                        guiTracker.addToMenuTracker(id, MenuType.ADD_MONEY_REWARD);
                        guiTracker.addToCrateTypeTracker(id, type.getName());
                        return;

                    case 21:
                        AddItemGroupRewardMenu itemGroup = new AddItemGroupRewardMenu();
                        p.openInventory(itemGroup.getInv());
                        guiTracker.removeFromMenuTracker(id);
                        guiTracker.addToMenuTracker(id, MenuType.ADD_ITEM_GROUP_REWARD);
                        guiTracker.addToCrateTypeTracker(id, type.getName());
                        return;

                    case 15:
                        ListRewardMenu list = new ListRewardMenu(type);
                        if (list.getInventories().length == 0) return;
                        p.openInventory(list.getInventories()[0]);
                        guiTracker.removeFromMenuTracker(id);
                        guiTracker.addToMenuTracker(id, MenuType.LIST_REWARD);
                        guiTracker.addToListTracker(id, list);
                        return;

                    default:
                        return;
                }
            }
        }
        for (int replaceableSlot : crateMenu.getReplaceableSlots()) {
            if (e.getRawSlot() == replaceableSlot || e.getClickedInventory().equals(p.getInventory())) e.setCancelled(false);
        }
    }

    private void processListMenu(@NotNull InventoryClickEvent e, @NotNull Player p) {
        ListRewardMenu list = guiTracker.getFromListTracker(p.getUniqueId());
        if (list == null) return;

        String title = ChatColor.stripColor(PlainTextComponentSerializer.plainText().serialize(e.getView().title())).replace("Reward List - ", "");
        int page;
        try {
            page = Integer.parseInt(title.split("/")[0]);
        } catch (NumberFormatException ignored) {
            return;
        }

        for (int clickableSlot : list.getClickableSlots()) {
            if (e.getRawSlot() == clickableSlot) {
                switch (clickableSlot) {
                    case 46:
                        if (page == 1) return;
                        p.openInventory(list.getInventories()[page - 1]);
                        return;

                    case 52:
                        if (page == list.getInventories().length) return;
                        p.openInventory(list.getInventories()[page + 1]);
                        return;

                    default:
                        CrateType type = list.getType();
                        Reward<?> reward = type.getWeights().get((page - 1) * 45 + e.getRawSlot());
                        type.removeReward(reward);
                        list.populate();
                        p.openInventory(list.getInventories()[page - 1]);
                        return;
                }
            }
        }
    }

    private void processGenericRewardMenu(@NotNull InventoryClickEvent e, @NotNull Player p, @NotNull MenuType menu) {
        int[] clickableSlots;
        int[] replaceableSlots;
        switch (menu) {
            case ADD_COMMAND_REWARD:
                clickableSlots = AddCommandRewardMenu.getClickableSlots();
                replaceableSlots = AddCommandRewardMenu.getReplaceableSlots();
                break;
            case ADD_ITEM_GROUP_REWARD:
                clickableSlots = AddItemGroupRewardMenu.getClickableSlots();
                replaceableSlots = AddItemGroupRewardMenu.getReplaceableSlots();
                break;
            case ADD_ITEM_REWARD:
                clickableSlots = AddItemRewardMenu.getClickableSlots();
                replaceableSlots = AddItemRewardMenu.getReplaceableSlots();
                break;
            default:
                return;
        }

        for (int clickableSlot : clickableSlots) {
            if (e.getRawSlot() == clickableSlot) {
                Inventory inv = e.getInventory();

                ItemStack weightItem = inv.getItem(13);
                if (weightItem == null) return;
                ItemMeta weightMeta = weightItem.getItemMeta();
                if (weightMeta == null) return;
                String weightName = ChatColor.stripColor(PlainTextComponentSerializer.plainText().serialize(weightMeta.displayName()));
                int weight = Integer.parseInt(ChatColor.stripColor(weightName).replace("Weight: ", ""));

                switch (clickableSlot) {
                    case 10:
                        weight -= weight == 0 ? 0 : 100;
                        break;
                    case 11:
                        weight -= weight == 0 ? 0 : 10;
                        break;
                    case 12:
                        weight -= weight == 0 ? 0 : 1;
                        break;
                    case 14:
                        weight += 1;
                        break;
                    case 15:
                        weight += 10;
                        break;
                    case 16:
                        weight += 100;
                        break;
                }

                weightMeta.displayName(Component.text("§eWeight: §f" + weight));
                weightItem.setItemMeta(weightMeta);
            }
        }
        for (int replaceableSlot : replaceableSlots) {
            if (e.getRawSlot() == replaceableSlot || e.getClickedInventory().equals(p.getInventory())) e.setCancelled(false);
        }
    }

    private void processMoneyRewardMenu(@NotNull InventoryClickEvent e, @NotNull Player p) {
        for (int clickableSlot : AddMoneyRewardMenu.getClickableSlots()) {
            if (e.getRawSlot() == clickableSlot) {
                Inventory inv = e.getInventory();

                ItemStack weightItem = inv.getItem(13);
                if (weightItem == null) return;
                ItemMeta weightMeta = weightItem.getItemMeta();
                if (weightMeta == null) return;
                String weightName = ChatColor.stripColor(PlainTextComponentSerializer.plainText().serialize(weightMeta.displayName()));
                int weight = Integer.parseInt(weightName.replace("Weight: ", ""));

                switch (clickableSlot) {
                    case 10:
                        weight -= weight == 0 ? 0 : 100;
                        break;
                    case 11:
                        weight -= weight == 0 ? 0 : 10;
                        break;
                    case 12:
                        weight -= weight == 0 ? 0 : 1;
                        break;
                    case 14:
                        weight += 1;
                        break;
                    case 15:
                        weight += 10;
                        break;
                    case 16:
                        weight += 100;
                        break;
                }
                weightMeta.displayName(Component.text("§eWeight: §f" + weight));
                weightItem.setItemMeta(weightMeta);

                ItemStack moneyItem = inv.getItem(31);
                if (moneyItem == null) return;
                ItemMeta moneyMeta = moneyItem.getItemMeta();
                if (moneyMeta == null) return;
                String moneyName = ChatColor.stripColor(PlainTextComponentSerializer.plainText().serialize(moneyMeta.displayName()));
                double money = Double.parseDouble(moneyName.replace("Money: ", ""));

                switch (clickableSlot) {
                    case 28:
                        money -= e.isShiftClick() ? 1000 : 100;
                        break;
                    case 29:
                        money -= e.isShiftClick() ? 10 : 1;
                        break;
                    case 30:
                        money -= e.isShiftClick() ? 0.1 : 0.01;
                        break;
                    case 32:
                        money += e.isShiftClick() ? 0.1 : 0.01;
                        break;
                    case 33:
                        money += e.isShiftClick() ? 10 : 1;
                        break;
                    case 34:
                        money += e.isShiftClick() ? 1000 : 100;
                        break;
                }

                moneyMeta.displayName(Component.text("§6Money: §f" + money));
                moneyItem.setItemMeta(moneyMeta);
            }
        }
        for (int replaceableSlot : AddMoneyRewardMenu.getReplaceableSlots()) {
            if (e.getRawSlot() == replaceableSlot || e.getClickedInventory().equals(p.getInventory())) e.setCancelled(false);
        }
    }

    private void cleanup(@NotNull UUID id) {
        guiTracker.removeFromCrateTypeTracker(id);
        guiTracker.removeFromMenuTracker(id);
        guiTracker.removeFromListTracker(id);
    }
}
