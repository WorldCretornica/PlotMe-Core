package com.worldcretornica.plotme_core.api;

public interface IBlock {

    ILocation getLocation();

    IWorld getWorld();

    int getX();

    int getY();

    int getZ();

    int getTypeId();

    String getBiome();

    boolean setTypeIdAndData(short id, byte data, boolean applyPhysics);

    byte getData();

    void setTypeId(int id, boolean applyPhysics);


}
