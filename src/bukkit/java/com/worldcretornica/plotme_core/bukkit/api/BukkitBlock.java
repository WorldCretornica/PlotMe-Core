package com.worldcretornica.plotme_core.bukkit.api;

import com.worldcretornica.plotme_core.api.IBiome;
import com.worldcretornica.plotme_core.api.IBlock;
import com.worldcretornica.plotme_core.api.ILocation;
import com.worldcretornica.plotme_core.api.IWorld;
import org.bukkit.block.Block;

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

    @Override
    public boolean setTypeIdAndData(Short id, byte data, boolean applyPhysics) {
        return block.setTypeIdAndData(id, data, applyPhysics);
    }

    @Override
    public byte getData() {
        return block.getData();
    }

    @Override
    public void setTypeId(int i, boolean b) {
        block.setTypeId(i, b);
    }
}
