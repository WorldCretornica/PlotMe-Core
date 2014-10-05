package com.worldcretornica.plotme_core.api;

public interface IPlayer extends IOfflinePlayer, ICommandSender, IEntity {

    public IWorld getWorld()@Override ;

    public ILocation getLoc@Override ation();

    public void teleport(ILocation location);
    
    public IItemStack getItemInHand();
}
