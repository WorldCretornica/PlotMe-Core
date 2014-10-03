package com.worldcretornica.plotme_core.bukkit.api;

import org.bukkit.World;
import com.worldcretornica.plotme_core.api.IBlock;
import com.worldcretornica.plotme_core.api.IWorld;
import com.worldcretornica.plotme_core.api.v0_14b.IPlotMe_ChunkGenerator;

public class BukkitWorld implements IWorld {

    World world;
    
    public BukkitWorld(World world) {
        this.world = world;
    }

    @Override
    public boolean isPlotMeGenerator() {
        return this.world.getGenerator() instanceof IPlotMe_ChunkGenerator;
    }

    @Override
    public IPlotMe_ChunkGenerator getGenerator() {
        return (IPlotMe_ChunkGenerator) world.getGenerator();
    }

    @Override
    public String getName() {
        return world.getName();
    }

    @Override
    public IBlock getBlockAt(int i, int j, int k) {
        return new BukkitBlock(world.getBlockAt(i, j, k));
    }
    
    public World getWorld() {
        return world;
    }
}
