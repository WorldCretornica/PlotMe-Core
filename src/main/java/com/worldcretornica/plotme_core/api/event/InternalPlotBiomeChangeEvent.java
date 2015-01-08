package com.worldcretornica.plotme_core.api.event;

import com.worldcretornica.plotme_core.Plot;
import com.worldcretornica.plotme_core.PlotMe_Core;
import com.worldcretornica.plotme_core.api.IBiome;
import com.worldcretornica.plotme_core.api.IPlayer;
import com.worldcretornica.plotme_core.api.World;

public class InternalPlotBiomeChangeEvent extends InternalPlotEvent implements ICancellable {

    private final IPlayer player;
    private boolean canceled;
    private IBiome biome;

    public InternalPlotBiomeChangeEvent(PlotMe_Core instance, World world, Plot plot, IPlayer player, IBiome biome) {
        super(instance, plot, world);
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

    public IBiome getBiome() {
        return biome;
    }

    public void setBiome(IBiome biome) {
        this.biome = biome;
    }
}
