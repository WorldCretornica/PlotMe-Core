package com.worldcretornica.plotme_core.api;

import java.util.UUID;

public interface IEntity extends IActor {

    /**
     * Get the entity's current position.
     *
     * @return the location of entity
     */
    Location getLocation();

    /**
     * Sets the location of the entity
     * "Teleports" or moves the entity
     *
     * @param location new location
     */
    void setLocation(Location location);

    /**
     * Uses the code that allows a delay while
     * "Teleporting" or moving the entity
     *
     * @param location new location
     */
    void teleport(Location location);

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
     * Returns the entity UUID
     *
     * @return entity UUID
     */
    @Override
    UUID getUniqueId();

    String getName();

    Vector getPosition();
}
