package com.worldcretornica.plotme_core.api;


import java.io.File;
import java.util.UUID;

public interface IWorld {

    /**
     * Get the name of the world
     *
     * @return world name
     */
    String getName();

    File getWorldFolder();

    @Override
    int hashCode();

    UUID getUUID();

    @Override
    boolean equals(Object obj);

    IChunk getChunkAt(int x, int z);

    void refreshChunk(int x, int z);

    IBlock getBlockAt(int x, int y, int z);
}
