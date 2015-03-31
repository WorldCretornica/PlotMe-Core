package com.worldcretornica.plotme_core.api;

public interface ILocation {

    IWorld getWorld();

    int getBlockX();

    int getBlockY();

    int getBlockZ();

    double getX();

    void setX(double x);

    double getY();

    void setY(double y);

    double getZ();

    void setZ(double z);

    ILocation add(double x, double y, double z);

    ILocation subtract(double x, double y, double z);

}
