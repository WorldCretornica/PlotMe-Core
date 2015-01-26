package com.worldcretornica.plotme_core.sponge.api;

import com.worldcretornica.plotme_core.api.IWorld;
import org.spongepowered.api.world.World;

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
