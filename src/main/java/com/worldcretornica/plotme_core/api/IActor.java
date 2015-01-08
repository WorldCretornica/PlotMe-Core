package com.worldcretornica.plotme_core.api;

import java.util.UUID;

/**
 * Created by Matthew on 10/24/2014.
 */
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
