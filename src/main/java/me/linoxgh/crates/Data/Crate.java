package me.linoxgh.crates.Data;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.jetbrains.annotations.NotNull;

public class Crate implements ConfigurationSerializable {
    private BlockPosition pos;
    private String crateType;

    public Crate(@NotNull BlockPosition pos, @NotNull String crateType) {
        this.pos = pos;
        this.crateType = crateType;
    }

    public Crate(@NotNull Map<String, Object> data) {
        this.pos = (BlockPosition) data.get("position");
        this.crateType = (String) data.get("crate-type");
    }

    public @NotNull BlockPosition getPos() {
        return pos;
    }
    public @NotNull String getCrateType() {
        return crateType;
    }

    public void setPos(@NotNull BlockPosition pos) {
        this.pos = pos;
    }
    public void setCrateType(@NotNull String crateType) {
        this.crateType = crateType;
    }

    @Override
    public @NotNull Map<String, Object> serialize() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("position", pos);
        result.put("crate-type", crateType);
        return result;
    }
}
