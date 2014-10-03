package com.worldcretornica.plotme_core.bukkit.api;

import org.bukkit.entity.Entity;

import com.worldcretornica.plotme_core.api.IEntity;

public class BukkitEntity implements IEntity {

    Entity entity;
    
    public BukkitEntity(Entity entity) {
        this.entity = entity;
    }
}
