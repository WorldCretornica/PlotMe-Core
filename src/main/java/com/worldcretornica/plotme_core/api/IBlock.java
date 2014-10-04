package com.worldcretornica.plotme_core.api;

public interface IBlock {

    public ILocation getLocation();
    
    public IWorld getWorld();

    public int getX();
    
    public int getY();
    
    public int getZ();
    
    public int getTypeId();

    public void setBiome(IBiome b);

    public IBiome getBiome();

    public boolean setTypeIdAndData(Short id, Byte data, boolean applyPhysics);

    public byte getData();

    public void setTypeId(int i, boolean b);
}
