package com.worldcretornica.plotme_core.bukkit.api;

import com.worldcretornica.plotme_core.api.IWorld;
import org.bukkit.World;

import java.io.File;

public class BukkitWorld implements IWorld {

    private final World world;

    public BukkitWorld(World world) {
        this.world = world;
    }

    /**
     * Get the name of the world
     *
     * @return world name
     */
    @Override
    public String getName() {
        return world.getName();
    }

    public World getWorld() {
        return world;
    }

    public File getWorldFolder() {
        return world.getWorldFolder();
    }

    @Override
    public int hashCode() {
        return world.getUID().hashCode();
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
