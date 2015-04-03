package com.worldcretornica.plotme_core.api.event;

import com.worldcretornica.plotme_core.Plot;
import com.worldcretornica.plotme_core.api.IPlayer;
import com.worldcretornica.plotme_core.api.IWorld;

public class PlotPlayerAddEvent extends InternalPlotEvent implements ICancellable, Event {

    private final IPlayer player;
    private final String addedPlayer;
    private boolean canceled;

    public PlotPlayerAddEvent(IWorld world, Plot plot, IPlayer player, String addedPlayer) {
        super(plot, world);
        this.player = player;
        this.addedPlayer = addedPlayer;
    }

    @Override
    public boolean isCancelled() {
        return canceled;
    }

    @Override
    public void setCanceled(boolean canceled) {
        this.canceled = canceled;
    }

    public IPlayer getPlayer() {
        return player;
    }

    /**
     * Get the UUID as a string of the player that was added to the plot.
     * By addded, the player can be either added to the trusted, denied, or allowed list.
     * @return the UUID as a string of the player added
     */
    public String getAddedPlayer() {
        return addedPlayer;
    }
}
