package com.worldcretornica.plotme_core.sponge.api;

import org.spongepowered.api.entity.Entity;

/**
 * Created by Matthew on 1/15/2015.
 */
public class SpongeEntity {

    public Entity entity;

    public SpongeEntity(Entity entity) {
        this.entity = entity;
    }

    public void setEntity(Entity entity) {
        this.entity.getLocation().getPosition().getX();
    }
}
