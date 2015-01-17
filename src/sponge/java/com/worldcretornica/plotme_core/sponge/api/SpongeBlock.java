package com.worldcretornica.plotme_core.sponge.api;

import com.worldcretornica.plotme_core.api.IBiome;
import com.worldcretornica.plotme_core.api.IBlock;
import com.worldcretornica.plotme_core.api.ILocation;
import com.worldcretornica.plotme_core.api.IWorld;
import org.spongepowered.api.block.BlockLoc;

/**
 * Created by Matthew on 1/15/2015.
 */
public class SpongeBlock implements IBlock {

    private final BlockLoc block;

    public SpongeBlock(BlockLoc block) {
        this.block = block;
    }

    @Override
    public ILocation getLocation() {
        return new SpongeLocation(block.getLocation());
    }

    @Override
    public IWorld getWorld() {
        return null;
    }

    @Override
    public int getX() {
        return block.getX();
    }

    @Override
    public int getY() {
        return block.getY()
                ;
    }

    @Override
    public int getZ() {
        return block.getZ();
    }

    @Override
    public int getTypeId() {
        return 0;
    }

    @Override
    public IBiome getBiome() {
        return null;
    }

    @Override
    public void setBiome(IBiome biome) {

    }

    @Override
    public boolean setTypeIdAndData(short id, byte data, boolean applyPhysics) {
        return false;
    }

    @Override
    public byte getData() {
        return 0;
    }

    @Override
    public void setTypeId(int id, boolean applyPhysics) {

    }
}
