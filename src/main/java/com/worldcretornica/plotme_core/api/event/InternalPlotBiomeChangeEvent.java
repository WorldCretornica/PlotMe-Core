package com.worldcretornica.plotme_core.api.event;

import com.worldcretornica.plotme_core.Biomes;
import com.worldcretornica.plotme_core.Plot;
import com.worldcretornica.plotme_core.PlotMe_Core;
import com.worldcretornica.plotme_core.api.IPlayer;
import com.worldcretornica.plotme_core.api.IWorld;

public class InternalPlotBiomeChangeEvent extends InternalPlotEvent implements ICancellable {

    private final IPlayer player;
    private boolean canceled;
    private Biomes biome;

    public InternalPlotBiomeChangeEvent(PlotMe_Core instance, IWorld world, Plot plot, IPlayer player, Biomes biome) {
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

    public Biomes getBiome() {
        return biome;
    }

    public void setBiome(Biomes biome) {
        this.biome = biome;
    }
}
