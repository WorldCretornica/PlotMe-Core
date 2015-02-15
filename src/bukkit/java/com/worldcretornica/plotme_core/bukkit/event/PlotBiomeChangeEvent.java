package com.worldcretornica.plotme_core.bukkit.event;

import com.worldcretornica.plotme_core.Plot;
import com.worldcretornica.plotme_core.PlotMe_Core;
import com.worldcretornica.plotme_core.api.IBiome;
import com.worldcretornica.plotme_core.api.IPlayer;
import com.worldcretornica.plotme_core.api.IWorld;
import com.worldcretornica.plotme_core.api.event.InternalPlotBiomeChangeEvent;
import com.worldcretornica.plotme_core.bukkit.api.BukkitBiome;
import com.worldcretornica.plotme_core.bukkit.api.BukkitPlayer;
import com.worldcretornica.plotme_core.bukkit.api.BukkitWorld;
import org.bukkit.World;
import org.bukkit.block.Biome;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;

public class PlotBiomeChangeEvent extends PlotEvent implements Cancellable {

    private final InternalPlotBiomeChangeEvent event;

    public PlotBiomeChangeEvent(PlotMe_Core instance, World world, Plot plot, Player player, Biome biome) {
        super(plot, world);
        event = new InternalPlotBiomeChangeEvent(new BukkitWorld(world), plot, new BukkitPlayer(player), new BukkitBiome(biome));
    }

    public PlotBiomeChangeEvent(PlotMe_Core instance, IWorld world, Plot plot, IPlayer player, IBiome biome) {
        super(plot, world);
        event = new InternalPlotBiomeChangeEvent(world, plot, player, biome);
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

    public Biome getBiome() {
        return ((BukkitBiome) event.getBiome()).getBiome();
    }

    public void setBiome(Biome biome) {
        event.setBiome(new BukkitBiome(biome));
    }

    public InternalPlotBiomeChangeEvent getInternal() {
        return event;
    }
}
