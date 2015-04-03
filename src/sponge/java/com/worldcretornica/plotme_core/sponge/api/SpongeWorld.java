package com.worldcretornica.plotme_core.sponge.api;

import com.worldcretornica.plotme_core.api.IWorld;
import org.spongepowered.api.world.World;

import java.io.File;

public class SpongeWorld implements IWorld {

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
    public int hashCode() {
        return world.getUniqueId().hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        boolean result = false;
        if (obj instanceof IWorld) {
            result = this.hashCode() == obj.hashCode();
        }
        return result;
    }
}
