package com.worldcretornica.plotme_core.api.event;

import com.worldcretornica.plotme_core.Plot;
import com.worldcretornica.plotme_core.api.IPlayer;

public class PlotAddAllowedEvent extends PlotEvent implements ICancellable, Event {

    private final IPlayer player;
    private final String allowedPlayer;
    private boolean canceled;

    public PlotAddAllowedEvent(Plot plot, IPlayer player, String allowed) {
        super(plot);
        this.player = player;
        this.allowedPlayer = allowed;
    }


    public IPlayer getPlayer() {
        return player;
    }

    @Override
    public boolean isCancelled() {
        return canceled;
    }

    @Override
    public void setCanceled(boolean canceled) {
        this.canceled = canceled;
    }

    /**
     * Get the UUID as a string of the player that was allowed to the plot.
     * This can also return the character '*' if everyone is added to the plot.
     * @return the UUID as a string of the player added
     */
    public String getAllowedPlayer() {
        return allowedPlayer;
    }
}
