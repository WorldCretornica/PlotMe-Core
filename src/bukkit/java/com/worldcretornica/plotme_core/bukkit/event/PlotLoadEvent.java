package com.worldcretornica.plotme_core.bukkit.event;

import com.worldcretornica.plotme_core.Plot;
import com.worldcretornica.plotme_core.PlotMe_Core;
import com.worldcretornica.plotme_core.api.IWorld;
import com.worldcretornica.plotme_core.api.event.InternalPlotLoadEvent;
import com.worldcretornica.plotme_core.bukkit.api.BukkitWorld;
import org.bukkit.World;

public class PlotLoadEvent extends PlotEvent {

    private final InternalPlotLoadEvent event;

    public PlotLoadEvent(PlotMe_Core instance, World world, Plot plot) {
        super(plot, world);
        event = new InternalPlotLoadEvent(instance, new BukkitWorld(world), plot);
    }

    public PlotLoadEvent(PlotMe_Core instance, IWorld world, Plot plot) {
        super(plot, world);
        event = new InternalPlotLoadEvent(instance, world, plot);
    }

    public InternalPlotLoadEvent getInternal() {
        return event;
    }
}
