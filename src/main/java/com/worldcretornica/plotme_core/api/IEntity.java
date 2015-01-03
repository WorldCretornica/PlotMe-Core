package com.worldcretornica.plotme_core.api;

import java.util.UUID;

public interface IEntity {

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

    /**
     * Returns a unique and persistent id for this entity
     *
     * @return unique id
     */
    UUID getUniqueId();

    void teleport(ILocation newl);
}
