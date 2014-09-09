package com.worldcretornica.plotme_core.event;

import com.worldcretornica.plotme_core.Plot;
import com.worldcretornica.plotme_core.PlotMe_Core;
import org.bukkit.World;
import org.bukkit.block.Biome;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;

public class PlotBiomeChangeEvent extends PlotEvent implements Cancellable {

    private boolean _canceled;
    private Player _player;
    private Biome _biome;

    public PlotBiomeChangeEvent(PlotMe_Core instance, World world, Plot plot, Player player, Biome biome) {
        super(instance, plot, world);
        _player = player;
        _biome = biome;
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

    public Biome getBiome() {
        return _biome;
    }
}
