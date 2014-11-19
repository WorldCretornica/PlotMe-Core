package com.worldcretornica.plotme_core.bukkit.event;

import com.worldcretornica.plotme_core.api.event.InternalPlotReloadEvent;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class PlotReloadEvent extends Event {

    private static final HandlerList handlers = new HandlerList();
    private final InternalPlotReloadEvent event;

    public PlotReloadEvent() {
        event = new InternalPlotReloadEvent();
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    public InternalPlotReloadEvent getInternal() {
        return event;
    }
}
