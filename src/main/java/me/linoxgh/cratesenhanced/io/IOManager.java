package me.linoxgh.cratesenhanced.io;

import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import me.linoxgh.cratesenhanced.CratesEnhanced;
import me.linoxgh.cratesenhanced.data.Crate;
import me.linoxgh.cratesenhanced.data.CrateStorage;
import me.linoxgh.cratesenhanced.data.CrateType;
import me.linoxgh.cratesenhanced.data.MessageStorage;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;
import org.jetbrains.annotations.NotNull;

public class IOManager {
    private final CratesEnhanced plugin;
    private final CrateStorage crates;
    private final MessageStorage messages;

    private final FileConfiguration cfg;
    private final File configFile;
    private final File cratesFile;
    private final File crateTypesFile;

    public IOManager(@NotNull CratesEnhanced plugin, @NotNull CrateStorage crates, @NotNull MessageStorage messages) {
        this.plugin = plugin;
        this.crates = crates;
        this.messages = messages;

        this.cfg = plugin.getConfig();
        configFile = new File(plugin.getDataFolder().getPath() + File.separator + ".config.yml");
        cratesFile = new File(plugin.getDataFolder().getPath() + File.separator + "crates.dat");
        crateTypesFile = new File(plugin.getDataFolder().getPath() + File.separator + "crate-types.dat");
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    public void checkFiles() {
        try {
            cratesFile.getParentFile().mkdirs();
            cratesFile.createNewFile();
        } catch (IOException e) {
            plugin.getLogger().warning("Failed to create the crates.dat file.");
            e.printStackTrace();
        }

        try {
            crateTypesFile.getParentFile().mkdirs();
            crateTypesFile.createNewFile();
        } catch (IOException e) {
            plugin.getLogger().warning("§4Failed to create the crate-types.dat file.");
            e.printStackTrace();
        }
    }

    public void loadMessages() {
        ConfigurationSection messages = cfg.getConfigurationSection("messages");
        if (messages == null) messages = cfg.createSection("messages");

        StringBuilder path = new StringBuilder();
        for (String masterKey : messages.getKeys(false)) {
            path.append(masterKey).append(".");

            ConfigurationSection masterMessages = messages.getConfigurationSection(masterKey);
            if (masterMessages == null) masterMessages = messages.createSection(masterKey);

            for (String subKey : masterMessages.getKeys(false)) {
                path.append(subKey).append(".");

                ConfigurationSection subMessages = masterMessages.getConfigurationSection(subKey);
                if (subMessages == null) subMessages = masterMessages.createSection(subKey);

                for (String key : subMessages.getKeys(false)) {
                    path.append(key);

                    String message = subMessages.getString(key);
                    this.messages.addMessage(path.toString(), message == null ? "ERROR" : message);
                    path = new StringBuilder();
                }
            }
        }
    }

    public boolean loadCrates() {
        if (!cratesFile.exists()) return false;
        try {
            FileInputStream fileInputStream = new FileInputStream(cratesFile);
            BukkitObjectInputStream inputStream = new BukkitObjectInputStream(fileInputStream);
            Object o = inputStream.readObject();

            if (!(o instanceof HashMap<?, ?>)) return false;
            HashMap<?, ?> map = (HashMap<?, ?>) o;
            for (Map.Entry<?, ?> entry : map.entrySet()) {
                crates.addCrate((String) entry.getKey(), (Crate) entry.getValue());
            }

            inputStream.close();
            fileInputStream.close();
            return true;
        } catch (EOFException ignored) {
            return true;
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean loadCrateTypes() {
        if (!crateTypesFile.exists()) return false;
        try {
            FileInputStream fileInputStream = new FileInputStream(crateTypesFile);
            BukkitObjectInputStream inputStream = new BukkitObjectInputStream(fileInputStream);
            Object o = inputStream.readObject();

            if (!(o instanceof HashMap<?, ?>)) return false;
            HashMap<?, ?> map = (HashMap<?, ?>) o;
            for (Map.Entry<?, ?> entry : map.entrySet()) {
                crates.addCrateType((String) entry.getKey(), (CrateType) entry.getValue());
            }

            inputStream.close();
            fileInputStream.close();
            return true;
        } catch (EOFException ignored) {
            return true;
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean saveCrates() {
        if (!cratesFile.exists()) return false;
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(cratesFile);
            BukkitObjectOutputStream outputStream = new BukkitObjectOutputStream(fileOutputStream);
            outputStream.writeObject(crates.getCrates());
            outputStream.flush();
            outputStream.close();
            fileOutputStream.close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean saveCrateTypes() {
        if (!crateTypesFile.exists()) return false;
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(crateTypesFile);
            BukkitObjectOutputStream outputStream = new BukkitObjectOutputStream(fileOutputStream);
            outputStream.writeObject(crates.getCrateTypes());
            outputStream.flush();
            outputStream.close();
            fileOutputStream.close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
}
