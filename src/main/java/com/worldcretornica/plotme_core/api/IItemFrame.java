package com.worldcretornica.plotme_core.api;

public interface IItemFrame extends IEntity {

    IBlockFace getFacing();

    IItemStack getItem();

    void setItem(IItemStack item);

    IRotation getRotation();

    void setRotation(IRotation rotation);

    void setFacingDirection(IBlockFace blockFace, boolean force);

}
