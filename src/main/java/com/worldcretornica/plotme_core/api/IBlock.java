package com.worldcretornica.plotme_core.api;

import org.bukkit.Material;
import org.bukkit.block.Biome;
import org.bukkit.block.BlockState;

public interface IBlock {

    ILocation getLocation();

    IWorld getWorld();

    int getX();

    int getY();

    int getZ();

    @Deprecated
    int getTypeId();

    @Deprecated
    String getBiome();

    @Deprecated
    void setBiome(Biome plains);

    @Deprecated
    boolean setTypeIdAndData(short id, byte data, boolean applyPhysics);

    byte getData();

    @Deprecated
    void setTypeId(int id, boolean applyPhysics);

    @Deprecated
    void setType(Material material, boolean applyPhyics);

    @Deprecated
    Material getType();

    @Deprecated
    BlockState getState();

    void setData(byte b, boolean b1);
}
