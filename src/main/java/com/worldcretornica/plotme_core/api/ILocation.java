package com.worldcretornica.plotme_core.api;

public interface ILocation {

    World getWorld();

    int getBlockX();

    int getBlockY();

    int getBlockZ();

    double getX();

    double getY();

    double getZ();

    IBlock getBlock();

    ILocation add(double x, double y, double z);

}
