package com.worldcretornica.plotme_core.bukkit.event;

import com.worldcretornica.plotme_core.Biomes;
import com.worldcretornica.plotme_core.Plot;
import com.worldcretornica.plotme_core.PlotMe_Core;
import com.worldcretornica.plotme_core.api.IPlayer;
import com.worldcretornica.plotme_core.api.IWorld;
import com.worldcretornica.plotme_core.api.event.InternalPlotBiomeChangeEvent;
import com.worldcretornica.plotme_core.bukkit.api.BukkitPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;

public class PlotBiomeChangeEvent extends PlotEvent implements Cancellable {

    private final InternalPlotBiomeChangeEvent event;

    public PlotBiomeChangeEvent(PlotMe_Core instance, IWorld world, Plot plot, IPlayer player, Biomes biome) {
        super(instance, plot, world);
        event = new InternalPlotBiomeChangeEvent(instance, world, plot, player, biome);
    }

    @Override
    public boolean isCancelled() {
        return event.isCancelled();
    }

    @Override
    public void setCancelled(boolean cancel) {
        event.setCanceled(cancel);
    }

    public Player getPlayer() {
        return ((BukkitPlayer) event.getPlayer()).getPlayer();
    }

    public Biomes getBiome() {
        return event.getBiome();
    }

    public void setBiome(Biomes biome) {
        event.setBiome(biome);
    }

    public InternalPlotBiomeChangeEvent getInternal() {
        return event;
    }
}
