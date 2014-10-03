package com.worldcretornica.plotme_core.api;

import java.util.UUID;

public interface IOfflinePlayer {
    public String getName();
    public boolean isOp();
    public UUID getUniqueId();
}
