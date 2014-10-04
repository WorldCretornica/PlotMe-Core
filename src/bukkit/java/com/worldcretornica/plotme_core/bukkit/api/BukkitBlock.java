package com.worldcretornica.plotme_core.bukkit.api;

import org.bukkit.block.Block;

import com.worldcretornica.plotme_core.api.IBiome;
import com.worldcretornica.plotme_core.api.IBlock;
import com.worldcretornica.plotme_core.api.ILocation;
import com.worldcretornica.plotme_core.api.IWorld;

public class BukkitBlock implements IBlock {

    Block block;
    
    public BukkitBlock(Block block) {
        this.block = block;
    }

    @Override
    public ILocation getLocation() {
        return new BukkitLocation(block.getLocation());
    }

    @Override
    public IWorld getWorld() {
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
    public void setBiome(IBiome b) {
        block.setBiome(((BukkitBiome) b).getBiome());
    }

    @Override
    public IBiome getBiome() {
        return new BukkitBiome(block.getBiome());
    }
}
