package me.linoxgh.cratesenhanced.gui;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class GUITracker {

    private final Map<UUID, MenuType> menuTracker = new HashMap<>();
    private final Map<UUID, ListRewardMenu> listTracker = new HashMap<>();
    private final Map<UUID, String> crateTypeTracker = new HashMap<>();

    public @Nullable MenuType getFromMenuTracker(@NotNull UUID id) {
        return menuTracker.get(id);
    }
    public void addToMenuTracker(@NotNull UUID id, @NotNull MenuType menu) {
        menuTracker.put(id, menu);
    }
    public void removeFromMenuTracker(@NotNull UUID id) {
        menuTracker.remove(id);
    }

    public @Nullable ListRewardMenu getFromListTracker(@NotNull UUID id) {
        return listTracker.get(id);
    }
    public void addToListTracker(@NotNull UUID id, @NotNull ListRewardMenu menu) {
        listTracker.put(id, menu);
    }
    public void removeFromListTracker(@NotNull UUID id) {
        listTracker.remove(id);
    }

    public @Nullable String getFromCrateTypeTracker(@NotNull UUID id) {
        return crateTypeTracker.get(id);
    }
    public void addToCrateTypeTracker(@NotNull UUID id, @NotNull String type) {
        crateTypeTracker.put(id, type);
    }
    public void removeFromCrateTypeTracker(@NotNull UUID id) {
        crateTypeTracker.remove(id);
    }
}
