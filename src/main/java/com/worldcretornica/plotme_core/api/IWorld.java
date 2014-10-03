package com.worldcretornica.plotme_core.api;

import com.worldcretornica.plotme_core.api.v0_14b.IPlotMe_ChunkGenerator;

public interface IWorld {

    public boolean isPlotMeGenerator();

    public IPlotMe_ChunkGenerator getGenerator();

    public String getName();

    public IBlock getBlockAt(int i, int j, int k);

}
