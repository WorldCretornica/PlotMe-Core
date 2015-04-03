package com.worldcretornica.plotme_core.api.event;

import com.worldcretornica.plotme_core.Plot;
import com.worldcretornica.plotme_core.api.IPlayer;
import com.worldcretornica.plotme_core.api.IWorld;

public class InternalPlotAddTrustedEvent extends InternalPlotAddAllowedEvent implements ICancellable, Event {

    public InternalPlotAddTrustedEvent(IWorld world, Plot plot, IPlayer player, String trusted) {
        super(world, plot, player, trusted);
    }

    /**
     * Get the UUID as a string of the player that was allowed to the plot.
     * @return the UUID as a string of the player added
     */
    public String getPlayerTrusted() {
        return super.getAddedPlayer();
    }
}
