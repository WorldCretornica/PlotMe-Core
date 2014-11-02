package com.worldcretornica.plotme_core.bukkit.event;

import com.worldcretornica.plotme_core.Plot;
import com.worldcretornica.plotme_core.PlotMe_Core;
import com.worldcretornica.plotme_core.api.IPlayer;
import com.worldcretornica.plotme_core.api.IWorld;
import com.worldcretornica.plotme_core.api.event.InternalPlotCommentEvent;
import com.worldcretornica.plotme_core.bukkit.api.BukkitPlayer;
import com.worldcretornica.plotme_core.bukkit.api.BukkitWorld;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;

public class PlotCommentEvent extends PlotEvent implements Cancellable {

    private InternalPlotCommentEvent event;

    public PlotCommentEvent(PlotMe_Core instance, World world, Plot plot, Player commenter, String comment) {
        super(instance, plot, world);
        event = new InternalPlotCommentEvent(instance, new BukkitWorld(world), plot, new BukkitPlayer(commenter), comment);
    }
    
    public PlotCommentEvent(PlotMe_Core instance, IWorld world, Plot plot, IPlayer commenter, String comment) {
        super(instance, plot, world);
        event = new InternalPlotCommentEvent(instance, world, plot, commenter, comment);
    }

    @Override
    public boolean isCancelled() {
        return event.isCancelled();
    }

    @Override
    public void setCancelled(boolean cancel) {
        event.setCanceled(cancel);
    }

    public Player getPlayer() {
        return ((BukkitPlayer) event.getPlayer()).getPlayer();
    }

    public String getComment() {
        return event.getComment();
    }
    
    public InternalPlotCommentEvent getInternal() {
        return event;
    }
}
