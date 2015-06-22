package com.worldcretornica.plotme_core.api.event;

import com.worldcretornica.plotme_core.Plot;
import com.worldcretornica.plotme_core.api.IPlayer;

public class PlotAddTrustedEvent extends PlotAddAllowedEvent implements ICancellable, Event {

    public PlotAddTrustedEvent(Plot plot, IPlayer player, String trusted) {
        super(plot, player, trusted);
    }

    /**
     * Get the UUID as a string of the player that was allowed to the plot.
     * @return the UUID as a string of the player added
     */
    public String getPlayerTrusted() {
        return super.getAllowedPlayer();
    }
}
