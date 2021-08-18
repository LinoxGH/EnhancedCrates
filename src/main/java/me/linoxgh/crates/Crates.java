package me.linoxgh.crates;

import me.linoxgh.crates.Commands.MainCommand;
import me.linoxgh.crates.Data.CrateStorage;
import org.bukkit.plugin.java.JavaPlugin;

public final class Crates extends JavaPlugin {

    private final CrateStorage crateStorage = new CrateStorage();

    @Override
    public void onEnable() {
        getCommand("crates").setExecutor(new MainCommand(crateStorage));
    }

    @Override
    public void onDisable() {

    }
}
