package com.worldcretornica.plotme_core.api.event;

import com.worldcretornica.plotme_core.PlotMe_Core;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class InternalPlotWorldLoadEvent extends Event
{	
    protected String world;
    protected PlotMe_Core plugin;
    protected int NbPlots;
    protected static final HandlerList handlers = new HandlerList();
    
    public InternalPlotWorldLoadEvent(PlotMe_Core instance, String world, int nbplots)
    {
        this.world = world;
        this.plugin = instance;
        this.NbPlots = nbplots;
    }
    
    /**
     * Returns the world used in the event
     * 
     * @return world
     * 
     */
    public String getWorldName()
    {
        return world;
    }
    
    /**
     * Returns the number of plot in the world loaded
     * 
     * @return number of plots
     */
    public int getNbPlots()
    {
        return NbPlots;
    }

    @Override
    public HandlerList getHandlers() 
    {
        return handlers;
    }
    
    public static HandlerList getHandlerList() 
    {
        return handlers;
    }
}
