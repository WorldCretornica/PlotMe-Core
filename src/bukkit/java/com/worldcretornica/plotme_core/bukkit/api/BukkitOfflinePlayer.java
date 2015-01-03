package com.worldcretornica.plotme_core.bukkit.api;

import com.worldcretornica.plotme_core.api.IOfflinePlayer;
import org.bukkit.OfflinePlayer;

import java.util.UUID;

public class BukkitOfflinePlayer implements IOfflinePlayer {

    private final OfflinePlayer offlineplayer;

    public BukkitOfflinePlayer(OfflinePlayer player) {
        offlineplayer = player;
    }

    /**
     * Returns the player name
     *
     * @return player name or in rare cases, null
     */
    @Override
    public String getName() {
        return offlineplayer.getName();
    }

    /**
     * Returns the player UUID
     *
     * @return player UUID
     */
    @Override
    public UUID getUniqueId() {
        return offlineplayer.getUniqueId();
    }


    public OfflinePlayer getOfflinePlayer() {
        return offlineplayer;
    }

    /**
     * Checks if the player is currently online
     *
     * @return true if the user is online
     */
    @Override
    public boolean isOnline() {
        return offlineplayer.isOnline();
    }
}
