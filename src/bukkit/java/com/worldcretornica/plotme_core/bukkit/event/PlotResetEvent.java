package com.worldcretornica.plotme_core.bukkit.event;

import com.worldcretornica.plotme_core.Plot;
import com.worldcretornica.plotme_core.PlotMe_Core;
import com.worldcretornica.plotme_core.api.ICommandSender;
import com.worldcretornica.plotme_core.api.IWorld;
import com.worldcretornica.plotme_core.api.event.InternalPlotResetEvent;
import com.worldcretornica.plotme_core.bukkit.api.BukkitCommandSender;
import com.worldcretornica.plotme_core.bukkit.api.BukkitWorld;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.event.Cancellable;

public class PlotResetEvent extends PlotEvent implements Cancellable {

    private InternalPlotResetEvent event;

    public PlotResetEvent(PlotMe_Core instance, World world, Plot plot, CommandSender reseter) {
        super(instance, plot, world);
        event = new InternalPlotResetEvent(instance, new BukkitWorld(world), plot, new BukkitCommandSender(reseter));
    }
    
    public PlotResetEvent(PlotMe_Core instance, IWorld world, Plot plot, ICommandSender reseter) {
        super(instance, plot, world);
        event = new InternalPlotResetEvent(instance, world, plot, reseter);
    }

    @Override
    public boolean isCancelled() {
        return event.isCancelled();
    }

    @Override
    public void setCancelled(boolean cancel) {
        event.setCancelled(cancel);
    }

    public CommandSender getReseter() {
        return ((BukkitCommandSender) event.getReseter()).getCommandSender();
    }
    
    public InternalPlotResetEvent getInternal() {
        return event;
    }
}
