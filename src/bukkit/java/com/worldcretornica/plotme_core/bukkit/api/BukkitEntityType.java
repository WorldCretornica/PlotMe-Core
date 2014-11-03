package com.worldcretornica.plotme_core.bukkit.api;

import com.worldcretornica.plotme_core.api.IEntityType;
import org.bukkit.entity.EntityType;

public class BukkitEntityType implements IEntityType {

    private final EntityType entitytype;
    
    public BukkitEntityType(EntityType type) {
        entitytype = type;
    }

    public EntityType getEntityType() {
        return entitytype;
    }
}
