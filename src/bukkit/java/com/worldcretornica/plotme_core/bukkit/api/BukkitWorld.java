package com.worldcretornica.plotme_core.bukkit.api;

import com.worldcretornica.plotme_core.api.IPlotMe_ChunkGenerator;
import com.worldcretornica.plotme_core.api.IWorld;
import com.worldcretornica.plotme_core.bukkit.BukkitPlotMe_ChunkGeneratorBridge;
import org.bukkit.World;

public class BukkitWorld implements IWorld {

    private final World world;

    public BukkitWorld(World world) {
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
        return world.getName();
    }

    public World getWorld() {
        return world;
    }

}
