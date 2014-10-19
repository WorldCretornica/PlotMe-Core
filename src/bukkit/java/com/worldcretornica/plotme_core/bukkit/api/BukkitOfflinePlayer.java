package com.worldcretornica.plotme_core.bukkit.api;

import com.worldcretornica.plotme_core.api.IOfflinePlayer;
import org.bukkit.OfflinePlayer;

import java.util.UUID;

public class BukkitOfflinePlayer implements IOfflinePlayer {

    private OfflinePlayer offlineplayer;

    public BukkitOfflinePlayer(OfflinePlayer player) {
        offlineplayer = player;
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
