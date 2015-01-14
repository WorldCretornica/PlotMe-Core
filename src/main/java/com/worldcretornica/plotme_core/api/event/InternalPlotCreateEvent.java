package com.worldcretornica.plotme_core.api.event;

import com.worldcretornica.plotme_core.PlotMeCoreManager;
import com.worldcretornica.plotme_core.PlotMe_Core;
import com.worldcretornica.plotme_core.api.Location;
import com.worldcretornica.plotme_core.api.Player;
import com.worldcretornica.plotme_core.api.World;

public class InternalPlotCreateEvent extends InternalPlotEvent implements ICancellable {

    private final String plotId;
    private final Player creator;
    private boolean canceled;

    public InternalPlotCreateEvent(PlotMe_Core instance, World world, String plotId, Player creator) {
        super(instance, null, world);
        this.plotId = plotId;
        this.creator = creator;
    }

    @Override
    public boolean isCancelled() {
        return canceled;
    }

    @Override
    public void setCanceled(boolean cancel) {
        canceled = cancel;
    }

    public String getPlotId() {
        return plotId;
    }

    public Player getPlayer() {
        return creator;
    }

    @Override
    public Location getUpperBound() {
        return PlotMeCoreManager.getPlotTopLoc(world, plotId);
    }

    @Override
    public Location getLowerBound() {
        return PlotMeCoreManager.getPlotBottomLoc(world, plotId);
    }
}
