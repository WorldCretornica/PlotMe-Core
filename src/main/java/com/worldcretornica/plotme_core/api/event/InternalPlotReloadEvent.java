package com.worldcretornica.plotme_core.api.event;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class InternalPlotReloadEvent extends Event {

    private static final HandlerList handlers = new HandlerList();

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

}
