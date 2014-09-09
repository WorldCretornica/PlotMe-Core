package com.worldcretornica.plotme_core.event;

import com.worldcretornica.plotme_core.Plot;
import com.worldcretornica.plotme_core.PlotMe_Core;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;

public class PlotOwnerChangeEvent extends PlotEvent implements Cancellable {

    private boolean _canceled;
    private Player _player;
    private String _newowner;

    public PlotOwnerChangeEvent(PlotMe_Core instance, World world, Plot plot, Player player, String newowner) {
        super(instance, plot, world);
        _player = player;
        _newowner = newowner;
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

    public String getNewOwner() {
        return _newowner;
    }
}
