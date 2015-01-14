package com.worldcretornica.plotme_core.api.event;

import com.worldcretornica.plotme_core.Plot;
import com.worldcretornica.plotme_core.PlotMe_Core;
import com.worldcretornica.plotme_core.api.Player;
import com.worldcretornica.plotme_core.api.World;

public class InternalPlotAddAllowedEvent extends InternalPlotEvent implements ICancellable {

    private final Player player;
    private final String allowed;
    private boolean canceled;

    public InternalPlotAddAllowedEvent(PlotMe_Core instance, World world, Plot plot, Player player, String allowed) {
        super(instance, plot, world);
        this.player = player;
        this.allowed = allowed;
    }

    @Override
    public boolean isCancelled() {
        return canceled;
    }

    @Override
    public void setCanceled(boolean canceled) {
        this.canceled = canceled;
    }

    public Player getPlayer() {
        return player;
    }

    public String getNewAllowed() {
        return allowed;
    }
}
