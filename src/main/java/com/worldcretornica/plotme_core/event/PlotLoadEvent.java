package com.worldcretornica.plotme_core.event;

import com.worldcretornica.plotme_core.Plot;
import com.worldcretornica.plotme_core.PlotMe_Core;
import org.bukkit.World;

public class PlotLoadEvent extends PlotEvent {

    public PlotLoadEvent(PlotMe_Core instance, World world, Plot plot) {
        super(instance, plot, world);
    }
}
