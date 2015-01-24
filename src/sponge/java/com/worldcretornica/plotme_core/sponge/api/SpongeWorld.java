package com.worldcretornica.plotme_core.sponge.api;

import org.spongepowered.api.world.World;

import com.worldcretornica.plotme_core.api.IWorld;

public class SpongeWorld implements IWorld {

    private final World world;
    
    public SpongeWorld(World world) {
        this.world = world;
    }
    
    @Override
    public String getName() {
        return world.getName();
    }
    
    public World getWorld() {
        return world;
    }
}
