package com.worldcretornica.plotme_core.api;

public interface ILocation {

    public IWorld getWorld();

    public int getBlockX();
    
    public int getBlockY();
    
    public int getBlockZ();

    public int getX();

    public int getY();

    public int getZ();

    public IBlock getBlock();

    public ILocation add(int i, int j, int k);

}
