package com.worldcretornica.plotme_core.bukkit.api;

import org.bukkit.block.BlockState;

import com.worldcretornica.plotme_core.api.IBlockState;
import com.worldcretornica.plotme_core.api.IWorld;

public class BukkitBlockState implements IBlockState {

    BlockState blockstate;

    public BukkitBlockState(BlockState blockstate) {
        this.blockstate = blockstate;
    }
    
    @Override
    public IWorld getWorld() {
        return new BukkitWorld(blockstate.getWorld());
    }
}
