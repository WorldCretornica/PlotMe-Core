package com.worldcretornica.plotme_core.api;

public interface Player extends CommandSender, IEntity, IOfflinePlayer {

    IItemStack getItemInHand();
}
