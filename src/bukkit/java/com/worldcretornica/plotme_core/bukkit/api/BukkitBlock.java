package com.worldcretornica.plotme_core.bukkit.api;

import com.worldcretornica.plotme_core.api.IBlock;
import com.worldcretornica.plotme_core.api.ILocation;
import com.worldcretornica.plotme_core.api.IWorld;
import org.bukkit.block.Block;

public class BukkitBlock implements IBlock {

    private final Block block;

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
    public String getBiome() {
        return block.getBiome().toString();
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

    @Override
    public String toString() {
        return "Bukkit Block: " + getTypeId() + ":" + getData();
    }
}
