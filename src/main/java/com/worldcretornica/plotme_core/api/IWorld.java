package com.worldcretornica.plotme_core.api;


public interface IWorld {

    boolean isPlotMeGenerator();

    IPlotMe_ChunkGenerator getGenerator();

    /**
     * Get the name of the world
     *
     * @return world name
     */
    String getName();

}
