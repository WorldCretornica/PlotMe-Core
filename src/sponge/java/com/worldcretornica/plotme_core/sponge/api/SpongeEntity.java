package com.worldcretornica.plotme_core.sponge.api;

import java.util.UUID;

import org.spongepowered.api.entity.Entity;

import com.worldcretornica.plotme_core.api.IEntity;
import com.worldcretornica.plotme_core.api.ILocation;
import com.worldcretornica.plotme_core.api.IWorld;

/**
 * Created by Matthew on 1/15/2015.
 */
public class SpongeEntity implements IEntity {

    public final Entity entity;

    public SpongeEntity(Entity entity) {
        this.entity = entity;
    }

    @Override
    public ILocation getLocation() {
        return new SpongeLocation(entity.getLocation());
    }

    @Override
    public void setLocation(ILocation location) {
        SpongeLocation loc = null;
        if (location instanceof SpongeLocation) {
            loc = (SpongeLocation) location;
        }
        if (loc != null) {
            entity.teleport(loc.getLocation());
        }
    }

    @Override
    public IWorld getWorld() {
        return new SpongeWorld(entity.getWorld());
    }

    @Override
    public void remove() {
        entity.remove();
    }

    @Override
    public String getName() {
        return ""; //TODO
    }

    @Override
    public UUID getUniqueId() {
        return entity.getUniqueId();
    }
}
