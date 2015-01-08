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
     * Mark the entity's removal.
     */

    void remove();

    IEntityType getType();

    void teleport(ILocation location);

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

    boolean hasPermission(String node);
}
