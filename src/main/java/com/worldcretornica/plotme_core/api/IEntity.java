package com.worldcretornica.plotme_core.api;

public interface IEntity {

    ILocation getLocation();

    void remove();

    IEntityType getType();

    void teleport(ILocation newl);
}
