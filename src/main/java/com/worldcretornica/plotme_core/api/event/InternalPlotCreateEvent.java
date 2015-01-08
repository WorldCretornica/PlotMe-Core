package com.worldcretornica.plotme_core.api.event;

import com.worldcretornica.plotme_core.PlotMeCoreManager;
import com.worldcretornica.plotme_core.PlotMe_Core;
import com.worldcretornica.plotme_core.api.ILocation;
import com.worldcretornica.plotme_core.api.IPlayer;
import com.worldcretornica.plotme_core.api.World;

public class InternalPlotCreateEvent extends InternalPlotEvent implements ICancellable {

    private final String plotId;
    private final IPlayer creator;
    private boolean canceled;

    public InternalPlotCreateEvent(PlotMe_Core instance, World world, String plotId, IPlayer creator) {
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

    public IPlayer getPlayer() {
        return creator;
    }

    @Override
    public ILocation getUpperBound() {
        return PlotMeCoreManager.getPlotTopLoc(world, plotId);
    }

    @Override
    public ILocation getLowerBound() {
        return PlotMeCoreManager.getPlotBottomLoc(world, plotId);
    }
}
