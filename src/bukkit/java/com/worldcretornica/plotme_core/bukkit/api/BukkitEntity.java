package com.worldcretornica.plotme_core.bukkit.api;

import com.worldcretornica.plotme_core.api.IEntity;
import com.worldcretornica.plotme_core.api.IEntityType;
import com.worldcretornica.plotme_core.api.ILocation;
import org.bukkit.entity.Entity;

import java.util.UUID;

public class BukkitEntity implements IEntity {

    Entity entity;
    
    public BukkitEntity(Entity entity) {
        this.entity = entity;
    }

    @Override
    public ILocation getLocation() {
        return new BukkitLocation(entity.getLocation());
    }

    @Override
    public void remove() {
        entity.remove();
    }

    @Override
    public IEntityType getType() {
        return new BukkitEntityType(entity.getType());
    }

    @Override
    public UUID getUniqueId() {
        return entity.getUniqueId();
    }

    @Override
    public void teleport(ILocation newl) {
        entity.teleport(((BukkitLocation) newl).getLocation());
    }

}
