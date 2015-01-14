package com.worldcretornica.plotme_core.api.event;

import com.worldcretornica.plotme_core.Plot;
import com.worldcretornica.plotme_core.PlotMeCoreManager;
import com.worldcretornica.plotme_core.PlotMe_Core;
import com.worldcretornica.plotme_core.api.Location;
import com.worldcretornica.plotme_core.api.Player;
import com.worldcretornica.plotme_core.api.World;

public class InternalPlotMoveEvent extends InternalPlotEvent implements ICancellable {

    private final String fromId;
    private final String toId;
    private final World world;
    private final Player mover;
    private boolean canceled;

    public InternalPlotMoveEvent(PlotMe_Core instance, World world, String fromId, String toId, Player mover) {
        super(instance, null, world);
        this.fromId = fromId;
        this.toId = toId;
        this.world = world;
        this.mover = mover;
    }

    @Override
    public boolean isCancelled() {
        return canceled;
    }

    @Override
    public void setCanceled(boolean cancel) {
        canceled = cancel;
    }

    @Override
    public Plot getPlot() {
        return plugin.getPlotMeCoreManager().getPlotById(fromId, world);
    }

    public Plot getPlotTo() {
        return plugin.getPlotMeCoreManager().getPlotById(toId, world);
    }

    public Player getPlayer() {
        return mover;
    }

    public String getId() {
        return fromId;
    }

    public String getIdTo() {
        return toId;
    }

    @Override
    public Location getUpperBound() {
        return PlotMeCoreManager.getPlotTopLoc(world, fromId);
    }

    @Override
    public Location getLowerBound() {
        return PlotMeCoreManager.getPlotBottomLoc(world, fromId);
    }

    public Location getUpperBoundTo() {
        return PlotMeCoreManager.getPlotTopLoc(world, toId);
    }

    public Location getLowerBoundTo() {
        return PlotMeCoreManager.getPlotBottomLoc(world, toId);
    }

    public String getOwnerTo() {
        Plot plot = getPlotTo();
        if (plot != null) {
            return plot.getOwner();
        } else {
            return "";
        }
    }
}
