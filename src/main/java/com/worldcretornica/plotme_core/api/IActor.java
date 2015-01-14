package com.worldcretornica.plotme_core.api;

import java.util.UUID;

public interface IActor {

    /**
     * Gets the name of the actor
     *
     * @return name of the actor
     */
    String getName();

    /**
     * Gets the UUID of the actor
     *
     * @return UUID of the actor
     */
    UUID getUniqueId();

}
