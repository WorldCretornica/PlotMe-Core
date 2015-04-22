package com.worldcretornica.plotme_core.sponge.api;

import com.worldcretornica.plotme_core.api.IEntity;
import com.worldcretornica.plotme_core.api.ILocation;
import com.worldcretornica.plotme_core.api.IWorld;
import com.worldcretornica.plotme_core.api.Vector;
import org.spongepowered.api.entity.Entity;

import java.util.UUID;

public class SpongeEntity implements IEntity {

    private final Entity entity;
    private final Vector pos;
    private final ILocation location;
    private final SpongeWorld world;

    public SpongeEntity(Entity entity) {
        this.entity = entity;
        pos = new Vector(entity.getLocation().getX(), entity.getLocation().getY(), entity.getLocation().getZ());
        world = new SpongeWorld(entity.getWorld());
        location = new ILocation(world, pos);
    }

    public Entity getEntity() {
        return entity;
    }

    @Override
    public ILocation getLocation() {
        return location;
    }

    @Override
    public void setLocation(ILocation location) {

    }

    @Override
    public IWorld getWorld() {
        return world;
    }

    @Override
    public void remove() {
        entity.remove();
    }

    @Override
    public UUID getUniqueId() {
        return entity.getUniqueId();
    }

    @Override
    public String getName() {
        return null; //Todo: Sponge doesn't have a get name for entities (yet).
    }

    @Override
    public Vector getPosition() {
        return pos;
    }
}
