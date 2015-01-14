package com.worldcretornica.plotme_core.api;

public interface IBlock {

    Location getLocation();

    World getWorld();

    int getX();

    int getY();

    int getZ();

    int getTypeId();

    IBiome getBiome();

    void setBiome(IBiome biome);

    boolean setTypeIdAndData(short id, byte data, boolean applyPhysics);

    byte getData();

    void setTypeId(int id, boolean applyPhysics);


}
