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

    ILocation add(double x, double y, double z);
    
    ILocation subtract(double x, double y, double z);
    
    ILocation clone();
    
    void setX(double x);
    
    void setY(double y);
    
    void setZ(double z);

}
