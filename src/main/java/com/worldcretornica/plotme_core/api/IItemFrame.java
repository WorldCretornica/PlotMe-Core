package com.worldcretornica.plotme_core.api;

public interface IItemFrame extends IEntity {

    IBlockFace getFacing();

    IItemStack getItem();

    IRotation getRotation();

    void setItem(IItemStack is);

    void setRotation(IRotation rot);

    void setFacingDirection(IBlockFace bf, boolean b);

}
