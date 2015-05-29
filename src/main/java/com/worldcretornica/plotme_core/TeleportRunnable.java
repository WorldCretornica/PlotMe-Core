package com.worldcretornica.plotme_core;

import com.worldcretornica.plotme_core.api.IEntity;
import com.worldcretornica.plotme_core.api.ILocation;

public class TeleportRunnable implements Runnable {

    private final IEntity entity;
    private final ILocation location;

    public TeleportRunnable(IEntity entity, ILocation location) {
        this.entity = entity;
        this.location = location;
    }

    @Override public void run() {
        entity.setLocation(location);
    }
}
