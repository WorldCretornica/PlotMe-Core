package com.worldcretornica.plotme_core.api;

public interface IEntity {

    /**
     * Get the Entity location.
     *
     * @return the location of entity
     */
    ILocation getLocation();

    void remove();

    IEntityType getType();

    void teleport(ILocation newl);
}
