package com.worldcretornica.plotme_core.api;

import java.util.UUID;

public interface IEntity {

    /**
     * Get the Entity location.
     *
     * @return the location of entity
     */
    ILocation getLocation();

    void remove();

    IEntityType getType();

    UUID getUniqueId();

    void teleport(ILocation newl);
}
