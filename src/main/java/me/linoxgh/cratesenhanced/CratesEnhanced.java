package me.linoxgh.cratesenhanced;

import me.linoxgh.cratesenhanced.Commands.MainCommand;
import me.linoxgh.cratesenhanced.Data.BlockPosition;
import me.linoxgh.cratesenhanced.Data.Crate;
import me.linoxgh.cratesenhanced.Data.CrateStorage;
import me.linoxgh.cratesenhanced.Data.CrateType;
import me.linoxgh.cratesenhanced.IO.IOManager;
import org.bstats.bukkit.Metrics;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.plugin.java.JavaPlugin;

public class CratesEnhanced extends JavaPlugin {

    private final CrateStorage crateStorage = new CrateStorage();
    private IOManager ioManager;

    @Override
    public void onEnable() {
        saveDefaultConfig();

        ConfigurationSerialization.registerClass(Crate.class);
        ConfigurationSerialization.registerClass(CrateType.class);
        ConfigurationSerialization.registerClass(BlockPosition.class);

        ioManager = new IOManager(this, crateStorage);
        ioManager.checkFiles();
        if (ioManager.loadCrateTypes()) {
            getLogger().info("Successfully loaded crate types.");
        } else {
            getLogger().warning("Could not load crate types.");
        }
        if (ioManager.loadCrates()) {
            getLogger().info("Successfully loaded crates.");
        } else {
            getLogger().warning("Could not load crates.");
        }

        getCommand("crates").setExecutor(new MainCommand(crateStorage));
        new Listeners(this, crateStorage);

        if (getConfig().getBoolean("metrics-enabled")) enableMetrics();
    }

    @Override
    public void onDisable() {
        if (!crateStorage.getCrateTypes().isEmpty()) {
            if (ioManager.saveCrateTypes()) {
                getLogger().info("Successfully saved crate types.");
            } else {
                getLogger().warning("Could not save crate types.");
            }
        } else {
            getLogger().info("There was no crate types to save.");
        }

        if (!crateStorage.getCrates().isEmpty()) {
            if (ioManager.saveCrates()) {
                getLogger().info("Successfully saved crates.");
            } else {
                getLogger().warning("Could not save crates.");
            }
        } else {
            getLogger().info("There was no crates to save.");
        }
    }

    private void enableMetrics() {
        new Metrics(this, 12500);
    }
}
