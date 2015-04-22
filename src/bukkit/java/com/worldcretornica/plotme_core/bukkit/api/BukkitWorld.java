package com.worldcretornica.plotme_core.bukkit.api;

import com.worldcretornica.plotme_core.api.IBlock;
import com.worldcretornica.plotme_core.api.IChunk;
import com.worldcretornica.plotme_core.api.IEntity;
import com.worldcretornica.plotme_core.api.IWorld;
import com.worldcretornica.plotme_core.api.Vector;
import org.bukkit.World;
import org.bukkit.entity.Entity;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

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
        return getUUID().hashCode();
    }

    @Override
    public UUID getUUID() {
        return world.getUID();
    }

    @Override
    public boolean equals(Object obj) {
        boolean result = false;
        if (obj instanceof IWorld) {
            result = this.hashCode() == obj.hashCode();
        }
        return result;
    }

    @Override
    public IChunk getChunkAt(int x, int z) {
        return new BukkitChunk(world.getChunkAt(x, z));
    }

    @Override
    public void refreshChunk(int x, int z) {
        world.refreshChunk(x, z);
    }

    @Override
    public IBlock getBlockAt(int x, int y, int z) {
        return new BukkitBlock(world.getBlockAt(x, y, z));
    }

    @Override
    public IBlock getBlockAt(Vector add) {
        return getBlockAt(add.getX(), add.getY(), add.getZ());
    }

    @Override
    public List<IEntity> getEntities() {
        List<IEntity> bukkitEntites = new ArrayList<>(world.getEntities().size());
        for (Entity entity : world.getEntities()) {
            bukkitEntites.add(new BukkitEntity(entity));
        }
        return bukkitEntites;
    }

    public IBlock getBlockAt(double x, double y, double z) {
        return getBlockAt((int) x, (int) y, (int) z);
    }
}
