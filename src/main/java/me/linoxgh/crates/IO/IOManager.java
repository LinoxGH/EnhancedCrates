package me.linoxgh.crates.IO;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.google.gson.Gson;
import me.linoxgh.crates.Crates;
import me.linoxgh.crates.Data.Crate;
import me.linoxgh.crates.Data.CrateStorage;
import me.linoxgh.crates.Data.CrateType;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.jetbrains.annotations.NotNull;

public class IOManager {
    private final CrateStorage crates;

    private final File cratesFile;
    private final File crateTypesFile;

    public IOManager(@NotNull Crates plugin, @NotNull CrateStorage crates) {
        this.crates = crates;

        cratesFile = new File(plugin.getDataFolder().getPath() + File.separator + "crates.json");
        crateTypesFile = new File(plugin.getDataFolder().getPath() + File.separator + "crate-types.json");
    }

    public void checkFiles() {
        if (!cratesFile.exists()) {
            try {
                cratesFile.mkdirs();
                cratesFile.createNewFile();
            } catch (IOException e) {
                Bukkit.getConsoleSender().sendMessage("§4Failed to create the crates.json file.");
                e.printStackTrace();
                return;
            }
        }

        if (!crateTypesFile.exists()) {
            try {
                crateTypesFile.mkdirs();
                crateTypesFile.createNewFile();
            } catch (IOException e) {
                Bukkit.getConsoleSender().sendMessage("§4Failed to create the crate-types.json file.");
                e.printStackTrace();
            }
        }
    }

    public boolean loadCrates() {
        try {
            Map crates =  new Gson().fromJson(new FileReader(cratesFile), Map.class);
            for (Object entry : crates.entrySet()) {
                if (!(entry instanceof Map.Entry)) return false;
                this.crates.addCrate(((Map.Entry<String, Crate>) entry).getKey(), ((Map.Entry<String, Crate>) entry).getValue());
            }
        } catch (FileNotFoundException e) {
            Bukkit.getConsoleSender().sendMessage("§4Failed to find crates.json file.");
            e.printStackTrace();
            return false;
        }
        return !crates.getCrateTypes().isEmpty();
    }

    public boolean loadCrateTypes() {
        try {
            Map crates =  new Gson().fromJson(new FileReader(crateTypesFile), Map.class);
            for (Object entry : crates.entrySet()) {
                if (!(entry instanceof Map.Entry)) return false;
                this.crates.addCrateType(((Map.Entry<String, CrateType>) entry).getKey(), ((Map.Entry<String, CrateType>) entry).getValue());
            }
        } catch (FileNotFoundException e) {
            Bukkit.getConsoleSender().sendMessage("§4Failed to find crate-types.json file.");
            e.printStackTrace();
            return false;
        }
        return !crates.getCrateTypes().isEmpty();
    }

    public boolean saveCrates() {
        try {
            String json = new Gson().toJson(crates.getCrates());
            new FileWriter(cratesFile).write(json);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean saveCrateTypes() {
        try {
            String json = new Gson().toJson(crates.getCrateTypes());
            new FileWriter(crateTypesFile).write(json);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
}