package com.worldcretornica.plotme_core.api;

public interface IBlock {

    ILocation getLocation();

    IWorld getWorld();

    int getX();

    int getY();

    int getZ();

    int getTypeId();

    void setBiome(IBiome b);

    IBiome getBiome();

    boolean setTypeIdAndData(Short id, byte data, boolean applyPhysics);

    byte getData();

    void setTypeId(int i, boolean b);
}
