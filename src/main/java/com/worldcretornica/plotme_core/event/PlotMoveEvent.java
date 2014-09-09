package com.worldcretornica.plotme_core.event;

import com.worldcretornica.plotme_core.Plot;
import com.worldcretornica.plotme_core.PlotMe_Core;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;

public class PlotMoveEvent extends PlotEvent implements Cancellable {

    private boolean _canceled;
    private String _fromId;
    private String _toId;
    private World _toworld;
    private Player _mover;

    public PlotMoveEvent(PlotMe_Core instance, World fromworld, World toworld, String fromId, String toId, Player mover) {
        super(instance, null, fromworld);
        _fromId = fromId;
        _toId = toId;
        _toworld = toworld;
        _mover = mover;
    }

    @Override
    public boolean isCancelled() {
        return _canceled;
    }

    @Override
    public void setCancelled(boolean cancel) {
        _canceled = cancel;
    }

    @Override
    public Plot getPlot() {
        return plugin.getPlotMeCoreManager().getPlotById(world, _fromId);
    }

    public Plot getPlotTo() {
        return plugin.getPlotMeCoreManager().getPlotById(_toworld, _toId);
    }

    public World getWorldTo() {
        return _toworld;
    }

    public Player getPlayer() {
        return _mover;
    }

    public String getId() {
        return _fromId;
    }

    public String getIdTo() {
        return _toId;
    }

    @Override
    public Location getUpperBound() {
        return plugin.getPlotMeCoreManager().getGenMan(world).getPlotTopLoc(world, _fromId);
    }

    @Override
    public Location getLowerBound() {
        return plugin.getPlotMeCoreManager().getGenMan(world).getPlotBottomLoc(world, _fromId);
    }

    public Location getUpperBoundTo() {
        return plugin.getPlotMeCoreManager().getGenMan(_toworld).getPlotTopLoc(_toworld, _toId);
    }

    public Location getLowerBoundTo() {
        return plugin.getPlotMeCoreManager().getGenMan(_toworld).getPlotBottomLoc(_toworld, _toId);
    }

    public String getOwnerTo() {
        Plot plot = getPlotTo();
        if (plot != null) {
            return plot.getOwner();
        } else {
            return "";
        }
    }
}
