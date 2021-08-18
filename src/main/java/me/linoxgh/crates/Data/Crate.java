package me.linoxgh.crates.Data;

import org.jetbrains.annotations.NotNull;

public class Crate {

    private BlockPosition pos;
    private String crateType;

    public Crate(@NotNull BlockPosition pos, @NotNull String crateType) {
        this.pos = pos;
        this.crateType = crateType;
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

}
