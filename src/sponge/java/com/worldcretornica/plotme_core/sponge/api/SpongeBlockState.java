package com.worldcretornica.plotme_core.sponge.api;

import org.spongepowered.api.block.BlockState;

import com.worldcretornica.plotme_core.api.IBlockState;
import com.worldcretornica.plotme_core.api.IWorld;

public class SpongeBlockState implements IBlockState {

    private final BlockState blockstate;
    
    public SpongeBlockState(BlockState blockstate) {
        this.blockstate = blockstate;
    }
    
    @Override
    public IWorld getWorld() {
        return new SpongeWorld(null); //TODO
    }
}
