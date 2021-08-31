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
    private final File cratesFile;
    private final File crateTypesFile;

    public IOManager(@NotNull CratesEnhanced plugin, @NotNull CrateStorage crates, @NotNull MessageStorage messages) {
        this.plugin = plugin;
        this.crates = crates;
        this.messages = messages;

        this.cfg = plugin.getConfig();
        cratesFile = new File(plugin.getDataFolder().getPath() + File.separator + "crates.dat");
        crateTypesFile = new File(plugin.getDataFolder().getPath() + File.separator + "crate-types.dat");
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    public void checkFiles() {
        try {
            crateTypesFile.getParentFile().mkdirs();
            crateTypesFile.createNewFile();
        } catch (IOException e) {
            plugin.getLogger().warning(messages.getMessage("general.loading.fail-createcratetype"));
            e.printStackTrace();
        }

        try {
            cratesFile.getParentFile().mkdirs();
            cratesFile.createNewFile();
        } catch (IOException e) {
            plugin.getLogger().warning(messages.getMessage("general.loading.fail-createcrate"));
            e.printStackTrace();
        }
    }

    public void loadMessages() {
        ConfigurationSection messages = cfg.getConfigurationSection("messages");
        if (messages == null) messages = cfg.createSection("messages");

        for (String masterKey : messages.getKeys(false)) {
            ConfigurationSection masterMessages = messages.getConfigurationSection(masterKey);
            if (masterMessages == null) continue;

            for (String subKey : masterMessages.getKeys(false)) {
                ConfigurationSection subMessages = masterMessages.getConfigurationSection(subKey);
                if (subMessages == null) continue;

                for (String key : subMessages.getKeys(false)) {
                    String message = subMessages.getString(key);
                    this.messages.addMessage(masterKey + "." + subKey + "." + key, message == null ? "ERROR" : message);
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
