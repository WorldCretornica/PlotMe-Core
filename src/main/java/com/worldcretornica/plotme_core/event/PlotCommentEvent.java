package com.worldcretornica.plotme_core.event;

import com.worldcretornica.plotme_core.Plot;
import com.worldcretornica.plotme_core.PlotMe_Core;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;

public class PlotCommentEvent extends PlotEvent implements Cancellable {

    private boolean _canceled;
    private Player _commenter;
    private String _comment;

    public PlotCommentEvent(PlotMe_Core instance, World world, Plot plot, Player commenter, String comment) {
        super(instance, plot, world);
        _commenter = commenter;
        _comment = comment;
    }

    @Override
    public boolean isCancelled() {
        return _canceled;
    }

    @Override
    public void setCancelled(boolean cancel) {
        _canceled = cancel;
    }

    public Player getPlayer() {
        return _commenter;
    }

    public String getComment() {
        return _comment;
    }
}
