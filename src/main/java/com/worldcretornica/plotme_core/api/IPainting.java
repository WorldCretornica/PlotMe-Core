package com.worldcretornica.plotme_core.api;

public interface IPainting {

    IBlockFace getFacing();

    IArt getArt();

    void teleport(ILocation newl);

    void setFacingDirection(IBlockFace bf, boolean b);

}
