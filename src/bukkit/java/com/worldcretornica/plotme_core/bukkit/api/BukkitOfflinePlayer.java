package com.worldcretornica.plotme_core.bukkit.api;

import java.util.UUID;

import org.bukkit.OfflinePlayer;

import com.worldcretornica.plotme_core.api.IOfflinePlayer;

public class BukkitOfflinePlayer implements IOfflinePlayer {

    private OfflinePlayer offlineplayer;

    public BukkitOfflinePlayer(OfflinePlayer op) {
        this.offlineplayer = op;
    }
    
    @Override
    public String getName() {
        return offlineplayer.getName();
    }

    @Override
    public boolean isOp() {
        return offlineplayer.isOp();
    }

    @Override
    public UUID getUniqueId() {
        return offlineplayer.getUniqueId();
    }
    
    public OfflinePlayer getOfflinePlayer() {
        return offlineplayer;
    }
}
