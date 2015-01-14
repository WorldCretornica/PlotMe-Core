package com.worldcretornica.plotme_core.api.event;

import com.worldcretornica.plotme_core.Plot;
import com.worldcretornica.plotme_core.PlotMe_Core;
import com.worldcretornica.plotme_core.api.Location;
import com.worldcretornica.plotme_core.api.Player;
import com.worldcretornica.plotme_core.api.World;

public class InternalPlotTeleportHomeEvent extends InternalPlotEvent implements ICancellable {

    private final Player player;
    private boolean canceled;
    private Location location;

    public InternalPlotTeleportHomeEvent(PlotMe_Core instance, World world, Plot plot, Player player) {
        super(instance, plot, world);
        this.player = player;
        location = null;
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

    @Override
    public Location getHomeLocation() {
        if (location == null) {
            return super.getHomeLocation();
        } else {
            return location;
        }
    }

    public void setHomeLocation(Location homelocation) {
        location = homelocation;
    }
}
