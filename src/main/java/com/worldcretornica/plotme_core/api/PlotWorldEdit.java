package com.worldcretornica.plotme_core.api;


public interface PlotWorldEdit {

    public void setMask(IPlayer p);

    public void setMask(IPlayer p, ILocation l);

    public void setMask(IPlayer p, String id);

    public void removeMask(IPlayer p);
}
