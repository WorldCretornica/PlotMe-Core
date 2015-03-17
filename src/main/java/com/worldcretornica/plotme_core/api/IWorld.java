package com.worldcretornica.plotme_core.api;


import java.io.File;

public interface IWorld {

    /**
     * Get the name of the world
     *
     * @return world name
     */
    String getName();

    File getWorldFolder();
}
