package com.worldcretornica.plotme_core.bukkit.api;

import com.worldcretornica.plotme_core.api.IBlock;
import com.worldcretornica.plotme_core.api.IEntity;
import com.worldcretornica.plotme_core.api.IWorld;
import com.worldcretornica.plotme_core.api.IWorldBorder;
import com.worldcretornica.plotme_core.api.Vector;
import com.worldcretornica.plotme_core.bukkit.BukkitUtil;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;

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

    /**
     * Get the handle to the world as part of the Bukkit/Spigot API.
     *
     * @return world
     */
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
    public void refreshChunk(int x, int z) {
        world.refreshChunk(x, z);
    }

    @Override
    public IBlock getBlockAt(int x, int y, int z) {
        return new BukkitBlock(world.getBlockAt(x, y, z));
    }

    @Override public IWorldBorder getWorldBorder() {
        return new BukkitWorldBorder(world.getWorldBorder());
    }

    @Override
    public IBlock getBlockAt(Vector add) {
        return getBlockAt(add.getBlockX(), add.getBlockY(), add.getBlockZ());
    }

    @Override public void getBiome(Vector position) {

    }

    @Override
    public List<IEntity> getEntities() {
        List<IEntity> bukkitEntites = new ArrayList<>(world.getEntities().size());
        for (Entity entity : world.getEntities()) {
            bukkitEntites.add(BukkitUtil.adapt(entity));
        }
        return bukkitEntites;
    }

    @Override
    public Entity spawnEntity(Location etloc, EntityType entitytype) {
        return world.spawnEntity(etloc, entitytype);
    }

}
