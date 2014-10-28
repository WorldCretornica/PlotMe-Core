package com.worldcretornica.plotme_core.api;

public interface IPlayer extends IOfflinePlayer, ICommandSender, IEntity, IActor {

    IWorld getWorld();

    @Override
    boolean hasPermission(String node);

    @Override
    ILocation getLocation();

    @Override
    void teleport(ILocation location);

    IItemStack getItemInHand();
}
