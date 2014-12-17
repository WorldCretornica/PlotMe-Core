package com.worldcretornica.plotme_core.api;

public interface IPlayer extends ICommandSender, IEntity, IOfflinePlayer {

    IWorld getWorld();

    boolean hasPermission(String node);

    IItemStack getItemInHand();
}
