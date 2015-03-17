package com.worldcretornica.plotme_core.sponge.event;

import com.worldcretornica.plotme_core.Plot;
import com.worldcretornica.plotme_core.api.ICommandSender;
import com.worldcretornica.plotme_core.api.IWorld;
import com.worldcretornica.plotme_core.api.event.InternalPlotResetEvent;
import com.worldcretornica.plotme_core.bukkit.api.BukkitCommandSender;
import org.bukkit.command.CommandSender;
import org.bukkit.event.Cancellable;

public class PlotResetEvent extends PlotEvent implements Cancellable {

    private final InternalPlotResetEvent event;

    public PlotResetEvent(IWorld world, Plot plot, ICommandSender reseter) {
        super(plot, world);
        event = new InternalPlotResetEvent(world, plot, reseter);
    }

    @Override
    public boolean isCancelled() {
        return event.isCancelled();
    }

    @Override
    public void setCancelled(boolean cancel) {
        event.setCanceled(cancel);
    }

    public CommandSender getReseter() {
        return ((BukkitCommandSender) event.getReseter()).getCommandSender();
    }

    public InternalPlotResetEvent getInternal() {
        return event;
    }
}
