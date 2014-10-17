package com.worldcretornica.plotme_core.api;

public interface IPlayer extends IOfflinePlayer, ICommandSender, IEntity {

    IWorld getWorld();

    @Override
    ILocation getLocation();

    @Override
    void teleport(ILocation location);

    IItemStack getItemInHand();
}
