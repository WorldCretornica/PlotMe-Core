package com.worldcretornica.plotme_core.api.event;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class InternalPlotWorldLoadEvent extends Event {
    private static final HandlerList handlers = new HandlerList();
    private final String world;
    private final int nbPlots;

    public InternalPlotWorldLoadEvent(String world, int nbplots) {
        this.world = world;
        nbPlots = nbplots;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    /**
     * Returns the world used in the event
     *
     * @return world
     */
    public String getWorldName() {
        return world;
    }

    /**
     * Returns the number of plot in the world loaded
     *
     * @return number of plots
     */
    public int getNbPlots() {
        return nbPlots;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }
}
