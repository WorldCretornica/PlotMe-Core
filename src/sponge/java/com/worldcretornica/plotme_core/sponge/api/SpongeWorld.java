package com.worldcretornica.plotme_core.sponge.api;

import com.flowpowered.math.vector.Vector3i;
import com.google.common.base.Optional;
import com.worldcretornica.plotme_core.api.IBlock;
import com.worldcretornica.plotme_core.api.IChunk;
import com.worldcretornica.plotme_core.api.IEntity;
import com.worldcretornica.plotme_core.api.IWorld;
import com.worldcretornica.plotme_core.api.Vector;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.spongepowered.api.world.Chunk;
import org.spongepowered.api.world.World;

import java.io.File;
import java.util.ArrayList;
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
    public IChunk getChunkAt(int x, int z) {
        Optional<Chunk> chunk = world.getChunk(new Vector3i(x, 0, z));
        if (chunk.orNull() != null) {
            return new SpongeChunk(chunk.get());
        }
        return null;
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
        world.getBiome(position.getBlockX(), position.getBlockZ()).
    }

    @Override
    public List<IEntity> getEntities() {
        world.getBiome().
        ArrayList<IEntity> entities = new ArrayList<>();
        for (org.spongepowered.api.entity.Entity entity : world.getEntities()) {
            entities.add(new SpongeEntity(entity));
        }
        return entities;
    }

    @Override
    public Entity spawnEntity(Location etloc, EntityType entitytype) {
        return null;
    }
}
