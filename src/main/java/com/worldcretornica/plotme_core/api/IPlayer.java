package com.worldcretornica.plotme_core.api;

public interface IPlayer extends IActor, ICommandSender, IEntity, IOfflinePlayer {

    IWorld getWorld();

    boolean hasPermission(String node);

    IItemStack getItemInHand();
}
