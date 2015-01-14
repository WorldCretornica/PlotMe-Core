package com.worldcretornica.plotme_core.bukkit.api;

import com.worldcretornica.plotme_core.api.IBiome;
import com.worldcretornica.plotme_core.api.IBlock;
import com.worldcretornica.plotme_core.api.Location;
import com.worldcretornica.plotme_core.api.World;
import org.bukkit.block.Block;

public class BukkitBlock implements IBlock {

    private final Block block;

    public BukkitBlock(Block block) {
        this.block = block;
    }

    @Override
    public Location getLocation() {
        return new BukkitLocation(block.getLocation());
    }

    @Override
    public World getWorld() {
        return new BukkitWorld(block.getWorld());
    }

    @Override
    public int getX() {
        return block.getX();
    }

    @Override
    public int getY() {
        return block.getY();
    }

    @Override
    public int getZ() {
        return block.getZ();
    }

    @SuppressWarnings("deprecation")
    @Override
    public int getTypeId() {
        return block.getTypeId();
    }

    @Override
    public IBiome getBiome() {
        return new BukkitBiome(block.getBiome());
    }

    @Override
    public void setBiome(IBiome biome) {
        block.setBiome(((BukkitBiome) biome).getBiome());
    }

    @SuppressWarnings("deprecation")
    @Override
    public boolean setTypeIdAndData(short id, byte data, boolean applyPhysics) {
        return block.setTypeIdAndData(id, data, applyPhysics);
    }

    @SuppressWarnings("deprecation")
    @Override
    public byte getData() {
        return block.getData();
    }

    @SuppressWarnings("deprecation")
    @Override
    public void setTypeId(int id, boolean applyPhysics) {
        block.setTypeId(id, applyPhysics);
    }

}
