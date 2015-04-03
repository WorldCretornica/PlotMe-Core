package com.worldcretornica.plotme_core.api.event;

import com.worldcretornica.plotme_core.Plot;
import com.worldcretornica.plotme_core.api.IPlayer;
import com.worldcretornica.plotme_core.api.IWorld;

public class InternalPlotBiomeChangeEvent extends InternalPlotEvent implements ICancellable, Event {

    private final IPlayer player;
    private boolean canceled;
    private String biome;

    public InternalPlotBiomeChangeEvent(IWorld world, Plot plot, IPlayer player, String biome) {
        super(plot, world);
        this.player = player;
        this.biome = biome;
    }

    @Override
    public boolean isCancelled() {
        return canceled;
    }

    @Override
    public void setCanceled(boolean cancel) {
        canceled = cancel;
    }

    public IPlayer getPlayer() {
        return player;
    }

    public String getBiome() {
        return biome;
    }

    public void setBiome(String biome) {
        this.biome = biome;
    }
}
