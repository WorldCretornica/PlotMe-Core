package com.worldcretornica.plotme_core.api;


public interface PlotWorldEdit {

    void setMask(IPlayer p);

    void setMask(IPlayer p, ILocation l);

    void setMask(IPlayer p, String id);

    void removeMask(IPlayer p);
}
