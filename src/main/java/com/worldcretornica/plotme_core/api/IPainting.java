package com.worldcretornica.plotme_core.api;

public interface IPainting {

    IBlockFace getFacing();

    IArt getArt();

    void teleport(ILocation location);

    void setFacingDirection(IBlockFace blockFace, boolean force);

}
