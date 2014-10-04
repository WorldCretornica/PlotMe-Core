package com.worldcretornica.plotme_core.api;

import java.util.Random;


public interface IPlotMe_ChunkGenerator {

    public IPlotMe_GeneratorManager getManager();

    ILocation getFixedSpawnLocation(IWorld world, Random random);
}
