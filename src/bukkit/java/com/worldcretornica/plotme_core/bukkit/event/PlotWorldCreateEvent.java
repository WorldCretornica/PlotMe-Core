package com.worldcretornica.plotme_core.bukkit.event;

import com.worldcretornica.plotme_core.api.event.InternalPlotWorldCreateEvent;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import java.util.Map;

public class PlotWorldCreateEvent extends Event implements Cancellable {

    private static final HandlerList handlers = new HandlerList();
    private final InternalPlotWorldCreateEvent event;

    public PlotWorldCreateEvent(String worldName, Map<String, String> parameters) {
        event = new InternalPlotWorldCreateEvent(worldName, parameters);
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    @Override
    public boolean isCancelled() {
        return event.isCancelled();
    }

    @Override
    public void setCancelled(boolean canceled) {
        event.setCanceled(canceled);
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public String getWorldName() {
        return event.getWorldName();
    }

    public Map<String, String> getParameters() {
        return event.getParameters();
    }

    public InternalPlotWorldCreateEvent getInternal() {
        return event;
    }

}
