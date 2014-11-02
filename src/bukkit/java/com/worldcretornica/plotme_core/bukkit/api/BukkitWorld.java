package com.worldcretornica.plotme_core.bukkit.api;

import com.worldcretornica.plotme_core.api.*;
import com.worldcretornica.plotme_core.bukkit.BukkitPlotMe_ChunkGeneratorBridge;
import org.bukkit.Location;
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
