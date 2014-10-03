package com.worldcretornica.plotme_core.bukkit.event;

import com.worldcretornica.plotme_core.PlotMe_Core;
import com.worldcretornica.plotme_core.api.event.InternalPlotWorldLoadEvent;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class PlotWorldLoadEvent extends Event
{	
    protected static final HandlerList handlers = new HandlerList();
    private InternalPlotWorldLoadEvent event;
    
    public PlotWorldLoadEvent(PlotMe_Core instance, String world, int nbplots) {
        event = new InternalPlotWorldLoadEvent(instance, world, nbplots);
    }
    
    /**
     * Returns the world used in the event
     * 
     * @return world
     * 
     */
    public String getWorldName()
    {
        return event.getWorldName();
    }
    
    /**
     * Returns the number of plot in the world loaded
     * 
     * @return number of plots
     */
    public int getNbPlots()
    {
        return event.getNbPlots();
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
    
    public InternalPlotWorldLoadEvent getInternal() {
        return event;
    }
}
