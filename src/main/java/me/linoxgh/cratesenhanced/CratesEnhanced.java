package me.linoxgh.cratesenhanced;

import me.linoxgh.cratesenhanced.commands.MainCommand;
import me.linoxgh.cratesenhanced.data.BlockPosition;
import me.linoxgh.cratesenhanced.data.Crate;
import me.linoxgh.cratesenhanced.data.CrateStorage;
import me.linoxgh.cratesenhanced.data.CrateType;
import me.linoxgh.cratesenhanced.data.MessageStorage;
import me.linoxgh.cratesenhanced.data.rewards.CommandReward;
import me.linoxgh.cratesenhanced.data.rewards.ItemGroupReward;
import me.linoxgh.cratesenhanced.data.rewards.ItemReward;
import me.linoxgh.cratesenhanced.data.rewards.MoneyReward;
import me.linoxgh.cratesenhanced.data.rewards.Reward;
import me.linoxgh.cratesenhanced.gui.GUITracker;
import me.linoxgh.cratesenhanced.io.IOManager;
import net.milkbowl.vault.economy.Economy;
import org.bstats.bukkit.Metrics;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.Nullable;

public class CratesEnhanced extends JavaPlugin {
    private final CrateStorage crateStorage = new CrateStorage();
    private final MessageStorage messageStorage = new MessageStorage();
    private final GUITracker guiTracker = new GUITracker();
    private IOManager ioManager;

    public GUITracker getGuiTracker() { return guiTracker; }

    private static CrateListeners crateListeners;
    private static GuiListeners guiListeners;
    private static boolean isVaultEnabled = false;
    private static Economy econ;

    @Override
    public void onEnable() {
        saveDefaultConfig();

        ConfigurationSerialization.registerClass(Crate.class);
        ConfigurationSerialization.registerClass(CrateType.class);
        ConfigurationSerialization.registerClass(BlockPosition.class);
        ConfigurationSerialization.registerClass(Reward.class);
        ConfigurationSerialization.registerClass(ItemReward.class);
        ConfigurationSerialization.registerClass(ItemGroupReward.class);
        ConfigurationSerialization.registerClass(CommandReward.class);
        ConfigurationSerialization.registerClass(MoneyReward.class);

        ioManager = new IOManager(this, crateStorage, messageStorage);
        ioManager.loadMessages();
        ioManager.checkFiles();
        if (ioManager.loadCrateTypes()) {
            getLogger().info(messageStorage.getMessage("general.loading.success-cratetype"));
        } else {
            getLogger().warning(messageStorage.getMessage("general.loading.fail-cratetype"));
        }
        if (ioManager.loadCrates()) {
            getLogger().info(messageStorage.getMessage("general.loading.success-crate"));
        } else {
            getLogger().warning(messageStorage.getMessage("general.loading.fail-crate"));
        }

        if (!setupEconomy()) {
            isVaultEnabled = false;
            getLogger().warning(messageStorage.getMessage("general.loading.fail-vault1"));
            getLogger().warning(messageStorage.getMessage("general.loading.fail-vault2"));
        } else {
            isVaultEnabled = true;
            getLogger().info(messageStorage.getMessage("general.loading.success-vault"));
        }

        getCommand("crates").setExecutor(new MainCommand(crateStorage, guiTracker, messageStorage));

        crateListeners = new CrateListeners(this, crateStorage, messageStorage);
        guiListeners = new GuiListeners(this, crateStorage, guiTracker);

        if (getConfig().getBoolean("metrics-enabled")) enableMetrics();
    }

    @Override
    public void onDisable() {
        if (!crateStorage.getCrateTypes().isEmpty()) {
            if (ioManager.saveCrateTypes()) {
                getLogger().info(messageStorage.getMessage("general.saving.success-cratetype"));
            } else {
                getLogger().warning(messageStorage.getMessage("general.saving.fail-cratetype"));
            }
        } else {
            getLogger().info(messageStorage.getMessage("general.saving.none-cratetype"));
        }

        if (!crateStorage.getCrates().isEmpty()) {
            if (ioManager.saveCrates()) {
                getLogger().info(messageStorage.getMessage("general.saving.success-crate"));
            } else {
                getLogger().warning(messageStorage.getMessage("general.saving.fail-crate"));
            }
        } else {
            getLogger().info(messageStorage.getMessage("general.saving.none-crate"));
        }
    }

    private boolean setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        econ = rsp.getProvider();
        return true;
    }

    public static boolean isVaultEnabled() {
        return isVaultEnabled;
    }
    public static @Nullable Economy getEcon() {
        return econ;
    }
    public static CrateListeners getCrateListeners() { return crateListeners; }

    private void enableMetrics() {
        new Metrics(this, 12500);
        getLogger().info(messageStorage.getMessage("general.loading.metrics"));
    }
}
