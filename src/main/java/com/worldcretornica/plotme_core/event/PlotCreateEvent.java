package com.worldcretornica.plotme_core.event;

import com.worldcretornica.plotme_core.PlotMe_Core;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;

public class PlotCreateEvent extends PlotEvent implements Cancellable {

    private boolean _canceled;
    private String _plotId;
    private Player _creator;

    public PlotCreateEvent(PlotMe_Core instance, World world, String plotId, Player creator) {
        super(instance, null, world);
        _plotId = plotId;
        _creator = creator;
    }

    @Override
    public boolean isCancelled() {
        return _canceled;
    }

    @Override
    public void setCancelled(boolean cancel) {
        _canceled = cancel;
    }

    public String getPlotId() {
        return _plotId;
    }

    public Player getPlayer() {
        return _creator;
    }

    @Override
    public Location getUpperBound() {
        return plugin.getPlotMeCoreManager().getGenMan(world).getPlotTopLoc(world, _plotId);
    }

    @Override
    public Location getLowerBound() {
        return plugin.getPlotMeCoreManager().getGenMan(world).getPlotBottomLoc(world, _plotId);
    }
}
