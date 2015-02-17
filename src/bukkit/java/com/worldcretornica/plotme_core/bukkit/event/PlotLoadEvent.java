package com.worldcretornica.plotme_core.bukkit.event;

import com.worldcretornica.plotme_core.Plot;
import com.worldcretornica.plotme_core.api.IWorld;
import com.worldcretornica.plotme_core.api.event.InternalPlotLoadEvent;
import com.worldcretornica.plotme_core.bukkit.api.BukkitWorld;
import org.bukkit.World;

public class PlotLoadEvent extends PlotEvent {

    private final InternalPlotLoadEvent event;

    public PlotLoadEvent(World world, Plot plot) {
        super(plot, world);
        event = new InternalPlotLoadEvent(new BukkitWorld(world), plot);
    }

    public PlotLoadEvent(IWorld world, Plot plot) {
        super(plot, world);
        event = new InternalPlotLoadEvent(world, plot);
    }

    public InternalPlotLoadEvent getInternal() {
        return event;
    }
}
