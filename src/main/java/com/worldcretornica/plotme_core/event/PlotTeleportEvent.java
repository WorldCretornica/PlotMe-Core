package com.worldcretornica.plotme_core.event;

import com.worldcretornica.plotme_core.Plot;
import com.worldcretornica.plotme_core.PlotMe_Core;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;

public class PlotTeleportEvent extends PlotEvent implements Cancellable {

    private boolean _canceled;
    private Player _player;
    private String _plotid;
    private Location _loc;

    public PlotTeleportEvent(PlotMe_Core instance, World world, Plot plot, Player player, Location loc, String plotId) {
        super(instance, plot, world);
        _player = player;
        _loc = loc;
        _plotid = plotId;
    }

    @Override
    public boolean isCancelled() {
        return _canceled;
    }

    @Override
    public void setCancelled(boolean cancel) {
        _canceled = cancel;
    }

    public Player getPlayer() {
        return _player;
    }

    public Location getLocation() {
        return _loc;
    }

    public String getPlotId() {
        return _plotid;
    }

    public boolean getIsPlotClaimed() {
        return (plot != null);
    }
}
