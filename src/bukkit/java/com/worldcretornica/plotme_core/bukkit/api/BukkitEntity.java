package com.worldcretornica.plotme_core.bukkit.api;

import org.bukkit.entity.Entity;

import com.worldcretornica.plotme_core.api.IEntity;
import com.worldcretornica.plotme_core.api.ILocation;

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
}
