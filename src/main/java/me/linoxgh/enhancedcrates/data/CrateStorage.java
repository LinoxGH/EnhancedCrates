package me.linoxgh.enhancedcrates.data;

import java.util.HashMap;
import java.util.Map;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class CrateStorage {

    private final HashMap<String, Crate> crates = new HashMap<>();
    private final HashMap<String, CrateType> crateTypes = new HashMap<>();

    public @NotNull HashMap<String, Crate> getCrates() {
        return crates;
    }
    public @NotNull HashMap<String, CrateType> getCrateTypes() {
        return crateTypes;
    }

    public @Nullable Crate getCrate(@NotNull String name) {
        return crates.get(name);
    }
    public @Nullable Crate getCrate(@NotNull BlockPosition pos) {
        for (Map.Entry<String, Crate> entry : crates.entrySet()) {
            if (entry.getValue().getPos().equals(pos)) return entry.getValue();
        }
        return null;
    }
    public @Nullable CrateType getCrateType(@NotNull String name) {
        return crateTypes.get(name);
    }

    public void addCrate(@NotNull String name, @NotNull Crate crate) {
        crates.put(name, crate);
    }
    public void removeCrate(@NotNull String name) {
        crates.remove(name);
    }

    public void addCrateType(@NotNull String name, @NotNull CrateType type) {
        crateTypes.put(name, type);
    }
    public void removeCrateType(@NotNull String name) {
        crateTypes.remove(name);
    }
}
