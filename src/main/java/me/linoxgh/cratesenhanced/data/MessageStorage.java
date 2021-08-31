package me.linoxgh.cratesenhanced.data;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.ChatColor;
import org.jetbrains.annotations.NotNull;

public class MessageStorage {
    private final Map<String, String> messages = new HashMap<>();

    public @NotNull Map<String, String> getMessages() {
        return messages;
    }
    public @NotNull String getMessage(@NotNull String key) {
        return messages.get(key) == null ? "ERROR" : messages.get(key);
    }
    public void addMessage(@NotNull String key, @NotNull String message) {
        messages.put(key, ChatColor.translateAlternateColorCodes('&', message));
    }
}
