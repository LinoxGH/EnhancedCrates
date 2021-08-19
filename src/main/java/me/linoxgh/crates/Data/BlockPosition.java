package me.linoxgh.crates.Data;

import java.util.Objects;

import org.jetbrains.annotations.NotNull;

public class BlockPosition {

    private int x;
    private int y;
    private int z;
    private String world;

    public BlockPosition(int x, int y, int z, @NotNull String world) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.world = world;
    }

    public int getX() {
        return x;
    }
    public int getY() {
        return y;
    }
    public int getZ() {
        return z;
    }
    public @NotNull String getWorld() {
        return world;
    }

    public void setX(int x) {
        this.x = x;
    }
    public void setY(int y) {
        this.y = y;
    }
    public void setZ(int z) {
        this.z = z;
    }
    public void setWorld(@NotNull String world) {
        this.world = world;
    }

    @Override
    public boolean equals(@NotNull Object o) {
        if (this == o) return true;
        if (!(o instanceof BlockPosition)) return false;
        BlockPosition pos = (BlockPosition) o;
        return x == pos.x && y == pos.y && z == pos.z && world.equals(pos.world);
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y, z, world);
    }
}
