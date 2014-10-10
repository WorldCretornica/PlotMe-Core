package com.worldcretornica.plotme_core.api;


public interface IWorld {

    boolean isPlotMeGenerator();

    IPlotMe_ChunkGenerator getGenerator();

    String getName();

    IBlock getBlockAt(int x, int y, int z);

    void refreshChunk(int x, int z);

    IChunk getChunkAt(int cx, int cz);

    int getMaxHeight();

    ILocation createLocation(double x, double y, double z);

}
