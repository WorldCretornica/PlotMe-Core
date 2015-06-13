package com.worldcretornica.plotme_core.api.event;

import com.worldcretornica.plotme_core.Plot;
import com.worldcretornica.plotme_core.api.IPlayer;
import com.worldcretornica.plotme_core.api.IWorld;

public class PlotAddDeniedEvent extends PlotEvent implements ICancellable, Event {

    private final String denied;
    private final IPlayer player;
    private boolean canceled;

    public PlotAddDeniedEvent(IWorld world, Plot plot, IPlayer player, String denied) {
        super(plot, world);
        this.player = player;
        this.denied = denied;
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
     * Get the UUID as a string of the player that was denied to the plot.
     * @return the UUID as a string of the player denied
     */
    public String getDeniedPlayer() {
        return denied;
    }
}
