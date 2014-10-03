package com.worldcretornica.plotme_core.api.event;

import com.worldcretornica.plotme_core.Plot;
import com.worldcretornica.plotme_core.PlotMe_Core;
import com.worldcretornica.plotme_core.api.*;

public class InternalPlotCommentEvent extends InternalPlotEvent implements ICancellable {

    private boolean _canceled;
    private IPlayer _commenter;
    private String _comment;

    public InternalPlotCommentEvent(PlotMe_Core instance, IWorld world, Plot plot, IPlayer commenter, String comment) {
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

    public IPlayer getPlayer() {
        return _commenter;
    }

    public String getComment() {
        return _comment;
    }
}
