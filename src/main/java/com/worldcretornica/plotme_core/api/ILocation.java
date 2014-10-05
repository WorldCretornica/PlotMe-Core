package com.worldcretornica.plotme_core.api;

public interface ILocation {

    IWorld getWorld();

    int getBlockX();

    int getBlockY();

    int getBlockZ();

    double getX();

    double getY();

    double getZ();

    IBlock getBlock();

    ILocation add(int i, int j, int k);

}
