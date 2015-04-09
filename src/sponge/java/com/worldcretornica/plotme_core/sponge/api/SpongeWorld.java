package com.worldcretornica.plotme_core.sponge.api;

import com.worldcretornica.plotme_core.api.IWorld;
import org.spongepowered.api.world.World;

import java.io.File;
import java.util.UUID;

public class SpongeWorld extends IWorld {

    private final World world;

    public SpongeWorld(World world) {
        this.world = world;
    }

    @Override
    public String getName() {
        return world.getName();
    }

    @Override
    public File getWorldFolder() {
        return null;
    }

    public World getWorld() {
        return world;
    }

    @Override
    public UUID getUUID() {
        return world.getUniqueId();
    }
}
