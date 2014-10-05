package com.worldcretornica.plotme_core.api;


public interface IWorld {

    public boolean isPlotMeGenerator();

    public IPlotMe_ChunkGenerator getGenerator();

    public String getName();

    public IBlock getBlockAt(int x, int y, int z);

    public void refreshChunk(int x, int z);

    public IChunk getChunkAt(int cx, int cz);

    public int getMaxHeight();

    public ILocation createLocation(double x, double y, double z);

}
