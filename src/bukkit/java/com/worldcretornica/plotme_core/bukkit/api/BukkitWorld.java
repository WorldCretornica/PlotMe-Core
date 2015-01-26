package com.worldcretornica.plotme_core.bukkit.api;

import com.worldcretornica.plotme_core.api.IWorld;
import org.bukkit.World;

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

}
