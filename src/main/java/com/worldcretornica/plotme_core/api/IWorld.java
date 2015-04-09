package com.worldcretornica.plotme_core.api;


import java.io.File;
import java.util.UUID;

public abstract class IWorld {

    /**
     * Get the name of the world
     *
     * @return world name
     */
    public abstract String getName();

    public abstract File getWorldFolder();

    @Override
    public int hashCode() {
        return getUUID().hashCode();
    }

    public abstract UUID getUUID();

    @Override
    public boolean equals(Object obj) {
        boolean result = false;
        if (obj instanceof IWorld) {
            result = this.hashCode() == obj.hashCode();
        }
        return result;
    }

}
