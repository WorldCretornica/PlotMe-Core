package com.worldcretornica.plotme_core.bukkit.api;

import org.bukkit.entity.EntityType;

import com.worldcretornica.plotme_core.api.IEntityType;

public class BukkitEntityType implements IEntityType {

    private EntityType entitytype;
    
    public BukkitEntityType(EntityType type) {
        this.entitytype = type;
    }

    public EntityType getEntityType() {
        return entitytype;
    }
}
