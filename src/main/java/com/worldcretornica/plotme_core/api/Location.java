package com.worldcretornica.plotme_core.api;

public interface Location {

    World getWorld();

    int getBlockX();

    int getBlockY();

    int getBlockZ();

    double getX();

    double getY();

    double getZ();

    IBlock getBlock();

    Location add(double x, double y, double z);

}
