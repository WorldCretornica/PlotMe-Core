package com.worldcretornica.plotme_core.api;

import java.util.UUID;

public interface IOfflinePlayer extends IActor {

    /**
     * Checks if the player is currently online
     *
     * @return true if the user is online
     */
    boolean isOnline();

    /**
     * Returns the player name
     *
     * @return player name or in rare cases, null
     */
    String getName();

    /**
     * Returns the player UUID
     *
     * @return player UUID
     */
    @Override
    UUID getUniqueId();

}
