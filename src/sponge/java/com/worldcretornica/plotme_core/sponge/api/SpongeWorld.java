package com.worldcretornica.plotme_core.sponge.api;

import com.worldcretornica.plotme_core.api.IBlock;
import com.worldcretornica.plotme_core.api.IEntity;
import com.worldcretornica.plotme_core.api.IWorld;
import com.worldcretornica.plotme_core.api.Vector;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.spongepowered.api.world.World;

import java.io.File;
import java.util.List;
import java.util.UUID;

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

    @Override
    public int hashCode() {
        return getUUID().hashCode();
    }

    public World getWorld() {
        return world;
    }

    @Override
    public UUID getUUID() {
        return world.getUniqueId();
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
        //Todo not possible yet
    }

    @Override
    public IBlock getBlockAt(int x, int y, int z) {
        //Todo not possible yet
        return null;
    }

    @Override
    public IBlock getBlockAt(Vector add) {
        return null;
    }

    @Override
    public void getBiome(Vector position) {
        //TODO
    }

    @Override
    public List<IEntity> getEntities() {
        return null;
    }

    @Override
    public Entity spawnEntity(Location etloc, EntityType entitytype) {
        return null;
    }
}
