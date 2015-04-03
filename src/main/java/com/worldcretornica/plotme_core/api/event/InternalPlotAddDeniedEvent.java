package com.worldcretornica.plotme_core.api.event;

import com.worldcretornica.plotme_core.Plot;
import com.worldcretornica.plotme_core.api.IPlayer;
import com.worldcretornica.plotme_core.api.IWorld;

public class InternalPlotAddDeniedEvent extends PlotPlayerAddEvent implements ICancellable, Event {

    public InternalPlotAddDeniedEvent(IWorld world, Plot plot, IPlayer player, String denied) {
        super(world, plot, player, denied);
    }

    public String getDeniedPlayer() {
        return super.getAddedPlayer();
    }
}
