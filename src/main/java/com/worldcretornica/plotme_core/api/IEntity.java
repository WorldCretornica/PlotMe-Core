package com.worldcretornica.plotme_core.api;

import java.util.UUID;

public interface IEntity extends IActor {

    /**
     * Get the entity's current position.
     *
     * @return the location of entity
     */
    ILocation getLocation();

    /**
     * Sets the location of the entity
     * "Teleports" or moves the entity
     *
     * @param location new location
     */
    void setLocation(ILocation location);

    /**
     * Get the world the entity is currently in.
     *
     * @return the world the entity is in
     */
    IWorld getWorld();

    /**
     * Mark the entity's removal.
     */
    void remove();

    /**
     * Returns the entity name
     *
     * @return entity name
     */
    @Override
    String getName();

    /**
     * Returns the entity UUID
     *
     * @return entity UUID
     */
    @Override
    UUID getUniqueId();

}
