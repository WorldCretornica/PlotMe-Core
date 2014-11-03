package com.worldcretornica.plotme_core.api;


public interface PlotWorldEdit {

    void setMask(IPlayer player);

    void setMask(IPlayer player, String id);

    void removeMask(IPlayer player);
}
