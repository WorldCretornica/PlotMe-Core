package com.worldcretornica.plotme_core.api;

public interface IBlock {

    public ILocation getLocation();
    
    public IWorld getWorld();

    public int getX();
    
    public int getY();
    
    public int getZ();
    
    public int getTypeId();
}
