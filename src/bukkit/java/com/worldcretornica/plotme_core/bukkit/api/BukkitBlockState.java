package com.worldcretornica.plotme_core.bukkit.api;

import com.worldcretornica.plotme_core.api.IBlockState;
import com.worldcretornica.plotme_core.api.IWorld;
import org.bukkit.block.BlockState;

public class BukkitBlockState implements IBlockState {

    private final BlockState blockstate;

    public BukkitBlockState(BlockState blockstate) {
        this.blockstate = blockstate;
    }

    @Override
    public IWorld getWorld() {
        return new BukkitWorld(blockstate.getWorld());
    }
}
