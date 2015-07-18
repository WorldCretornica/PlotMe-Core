package com.worldcretornica.plotme_core.utils;

public class ChunkCoords {

    private final int x;
    private final int z;

    public ChunkCoords(int x, int z) {
        this.x = x;
        this.z = z;
    }

    public int getZ() {
        return z;
    }

    public int getX() {
        return x;
    }
}
