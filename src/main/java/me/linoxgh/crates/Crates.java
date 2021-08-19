package me.linoxgh.crates;

import me.linoxgh.crates.Commands.MainCommand;
import me.linoxgh.crates.Data.CrateStorage;
import me.linoxgh.crates.IO.IOManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class Crates extends JavaPlugin {

    private final CrateStorage crateStorage = new CrateStorage();
    private IOManager ioManager;

    @Override
    public void onEnable() {
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
    }

    @Override
    public void onDisable() {
        if (ioManager.saveCrateTypes()) {
            getLogger().info("Successfully saved crate types.");
        } else {
            getLogger().warning("Could not save crate types.");
        }
        if (ioManager.saveCrates()) {
            getLogger().info("Successfully saved crates.");
        } else {
            getLogger().warning("Could not save crates.");
        }

    }
}
