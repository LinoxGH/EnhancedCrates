package me.linoxgh.enhancedcrates.data;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.jetbrains.annotations.NotNull;

public class Crate implements ConfigurationSerializable {
    private final String name;
    private final BlockPosition pos;
    private final String crateType;

    public Crate(@NotNull String name, @NotNull BlockPosition pos, @NotNull String crateType) {
        this.name = name;
        this.pos = pos;
        this.crateType = crateType;
    }

    public Crate(@NotNull Map<String, Object> data) {
        this.name = (String) data.get("name");
        this.pos = (BlockPosition) data.get("position");
        this.crateType = (String) data.get("crate-type");
    }

    public @NotNull String getName() {
        return name;
    }
    public @NotNull BlockPosition getPos() {
        return pos;
    }
    public @NotNull String getCrateType() {
        return crateType;
    }

    @Override
    public @NotNull Map<String, Object> serialize() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("name", name);
        result.put("position", pos);
        result.put("crate-type", crateType);
        return result;
    }
}
