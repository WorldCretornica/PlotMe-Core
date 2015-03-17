package com.worldcretornica.plotme_core.sponge.event;

import com.worldcretornica.plotme_core.api.event.InternalPlotWorldCreateEvent;
import org.bukkit.event.Cancellable;
import org.spongepowered.api.event.AbstractEvent;

import java.util.Map;

public class PlotWorldCreateEvent extends AbstractEvent implements Cancellable {

    private final InternalPlotWorldCreateEvent event;

    public PlotWorldCreateEvent(String worldName, Map<String, String> parameters) {
        event = new InternalPlotWorldCreateEvent(worldName, parameters);
    }

    @Override
    public boolean isCancelled() {
        return event.isCancelled();
    }

    @Override
    public void setCancelled(boolean canceled) {
        event.setCanceled(canceled);
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
