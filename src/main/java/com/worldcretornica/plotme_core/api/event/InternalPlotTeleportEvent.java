package com.worldcretornica.plotme_core.api.event;

import com.worldcretornica.plotme_core.Plot;
import com.worldcretornica.plotme_core.PlotMe_Core;
import com.worldcretornica.plotme_core.api.Location;
import com.worldcretornica.plotme_core.api.Player;
import com.worldcretornica.plotme_core.api.World;

public class InternalPlotTeleportEvent extends InternalPlotEvent implements ICancellable {

    private final Player player;
    private final String plotId;
    private final Location location;
    private boolean canceled;

    public InternalPlotTeleportEvent(PlotMe_Core instance, World world, Plot plot, Player player, Location location, String plotId) {
        super(instance, plot, world);
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

    public Player getPlayer() {
        return player;
    }

    public Location getLocation() {
        return location;
    }

    public String getPlotId() {
        return plotId;
    }

    public boolean getIsPlotClaimed() {
        return (getPlot() != null);
    }
}
