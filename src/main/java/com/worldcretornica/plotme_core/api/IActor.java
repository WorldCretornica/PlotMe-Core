package com.worldcretornica.plotme_core.api;

import java.util.UUID;

/**
 * Created by Matthew on 10/24/2014.
 */
public interface IActor {
    boolean isOp();

    String getName();

    UUID getUniqueId();

}
