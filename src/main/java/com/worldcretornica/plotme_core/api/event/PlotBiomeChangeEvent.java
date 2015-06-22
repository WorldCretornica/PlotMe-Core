package com.worldcretornica.plotme_core.api.event;

import com.worldcretornica.plotme_core.Plot;
import com.worldcretornica.plotme_core.api.IPlayer;

public class PlotBiomeChangeEvent extends PlotEvent implements ICancellable, Event {

    private final IPlayer player;
    private boolean canceled;
    private String biome;

    public PlotBiomeChangeEvent(Plot plot, IPlayer player, String biome) {
        super(plot);
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
