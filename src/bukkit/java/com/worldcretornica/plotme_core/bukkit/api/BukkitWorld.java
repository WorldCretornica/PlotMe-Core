package com.worldcretornica.plotme_core.bukkit.api;

import org.bukkit.Location;
import org.bukkit.World;

import com.worldcretornica.plotme_core.api.IBlock;
import com.worldcretornica.plotme_core.api.IChunk;
import com.worldcretornica.plotme_core.api.ILocation;
import com.worldcretornica.plotme_core.api.IWorld;
import com.worldcretornica.plotme_core.api.IPlotMe_ChunkGenerator;

public class BukkitWorld implements IWorld {

    private World world;
    
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

    @Override
    public void refreshChunk(int x, int z) {
        world.refreshChunk(x, z);
    }

    @Override
    public IChunk getChunkAt(int cx, int cz) {
        return new BukkitChunk(world.getChunkAt(cx, cx));
    }

    @Override
    public int getMaxHeight() {
        return world.getMaxHeight();
    }

    @Override
    public ILocation createLocation(double x, double y, double z) {
        return new BukkitLocation(new Location(world, x, y, z));
    }
}
