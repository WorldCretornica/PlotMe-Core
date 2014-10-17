package com.worldcretornica.plotme_core.api;

import java.util.UUID;

public interface IOfflinePlayer {
    String getName();

    boolean isOp();

    UUID getUniqueId();
}
