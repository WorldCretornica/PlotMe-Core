package com.worldcretornica.plotme_core.api;

public interface IPlayer extends ICommandSender, IEntity, IOfflinePlayer {

    World getWorld();

    IItemStack getItemInHand();
}
