package com.worldcretornica.plotme_core.api.event;

import com.worldcretornica.plotme_core.Plot;
import com.worldcretornica.plotme_core.PlotMe_Core;
import com.worldcretornica.plotme_core.api.Player;
import com.worldcretornica.plotme_core.api.World;

public class InternalPlotRemoveDeniedEvent extends InternalPlotEvent implements ICancellable {

    private final Player player;
    private final String denied;
    private boolean canceled;

    public InternalPlotRemoveDeniedEvent(PlotMe_Core instance, World world, Plot plot, Player player, String denied) {
        super(instance, plot, world);
        this.player = player;
        this.denied = denied;
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

    public String getRemovedDenied() {
        return denied;
    }
}
