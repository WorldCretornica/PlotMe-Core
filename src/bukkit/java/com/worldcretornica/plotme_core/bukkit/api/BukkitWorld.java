package com.worldcretornica.plotme_core.bukkit.api;

import com.worldcretornica.plotme_core.api.IPlotMe_ChunkGenerator;
import com.worldcretornica.plotme_core.api.World;
import com.worldcretornica.plotme_core.bukkit.BukkitPlotMe_ChunkGeneratorBridge;

public class BukkitWorld implements World {

    private final org.bukkit.World world;

    public BukkitWorld(org.bukkit.World world) {
        this.world = world;
    }

    @Override
    public boolean isPlotMeGenerator() {
        return world.getGenerator() instanceof IBukkitPlotMe_ChunkGenerator;
    }

    @Override
    public IPlotMe_ChunkGenerator getGenerator() {
        return new BukkitPlotMe_ChunkGeneratorBridge((IBukkitPlotMe_ChunkGenerator) world.getGenerator());
    }

    /**
     * Get the name of the world
     *
     * @return world name
     */
    @Override
    public String getName() {
        return null;
    }

    public org.bukkit.World getWorld() {
        return world;
    }

}
