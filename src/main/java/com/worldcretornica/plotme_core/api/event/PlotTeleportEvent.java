package com.worldcretornica.plotme_core.api.event;

import com.worldcretornica.plotme_core.Plot;
import com.worldcretornica.plotme_core.PlotId;
import com.worldcretornica.plotme_core.api.IPlayer;
import com.worldcretornica.plotme_core.api.Location;

public class PlotTeleportEvent extends PlotEvent implements ICancellable, Event {

    private final IPlayer player;
    private final PlotId plotId;
    private final Location location;
    private boolean canceled;

    public PlotTeleportEvent(Plot plot, IPlayer player, Location location, PlotId plotId) {
        super(plot, location.getWorld());
        this.player = player;
        this.location = location;
        this.plotId = plotId;
    }

    @Override
    public boolean isCancelled() {
        return canceled;
    }

    @Override
    public void setCanceled(boolean cancel) {
        canceled = cancel;
    }


    /**
     * Get the {@link IPlayer} that executed the command.
     * @return internal player that executed the event
     */
    public IPlayer getPlayer() {
        return player;
    }

    /**
     * Get the home {@link Location} of the plot
     * @return internal home location of the plot
     */
    public Location getLocation() {
        return location;
    }

    /**
     * The {@link PlotId} of the plot teleported to
     * @return plot id of the plot
     */
    public PlotId getPlotId() {
        return plotId;
    }

    /**
     * Checks if the plot is claimed. This will always return true.
     * @return true
     */
    public boolean isPlotClaimed() {
        return getPlot() != null;
    }
}
