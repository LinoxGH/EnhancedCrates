package me.linoxgh.crates;

import me.linoxgh.crates.Commands.MainCommand;
import me.linoxgh.crates.Data.CrateStorage;
import me.linoxgh.crates.IO.IOManager;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class Crates extends JavaPlugin {

    private final CrateStorage crateStorage = new CrateStorage();
    private IOManager ioManager;

    @Override
    public void onEnable() {
        ioManager = new IOManager(this, crateStorage);
        ioManager.checkFiles();
        if (ioManager.loadCrateTypes()) {
            Bukkit.getConsoleSender().sendMessage("§aSuccessfully loaded crate types.");
        } else {
            Bukkit.getConsoleSender().sendMessage("§4Could not load crate types.");
            return;
        }
        if (ioManager.loadCrates()) {
            Bukkit.getConsoleSender().sendMessage("§aSuccessfully loaded crates.");
        } else {
            Bukkit.getConsoleSender().sendMessage("§4Could not load crates.");
            return;
        }

        getCommand("crates").setExecutor(new MainCommand(crateStorage));
    }

    @Override
    public void onDisable() {
        if (ioManager.saveCrateTypes()) {
            Bukkit.getConsoleSender().sendMessage("§aSuccessfully saves crate types.");
        } else {
            Bukkit.getConsoleSender().sendMessage("§4Could not save crate types.");
        }
        if (ioManager.saveCrates()) {
            Bukkit.getConsoleSender().sendMessage("§aSuccessfully saved crates.");
        } else {
            Bukkit.getConsoleSender().sendMessage("§4Could not save crates.");
        }

    }
}
