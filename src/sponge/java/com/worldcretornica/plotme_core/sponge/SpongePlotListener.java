package com.worldcretornica.plotme_core.sponge;

import org.spongepowered.api.event.block.*;
import org.spongepowered.api.event.player.PlayerInteractEvent;
import org.spongepowered.api.util.event.Subscribe;

import com.worldcretornica.plotme_core.PlotMe_Core;

/**
 * Created by Matthew on 1/7/2015.
 */
public class SpongePlotListener {

    private final PlotMe_Sponge plugin;
    private final PlotMe_Core api;

    public SpongePlotListener(PlotMe_Sponge instance) {
        api = instance.getAPI();
        this.plugin = instance;
    }

    @Subscribe
    public void onBlockBreak(final BlockChangeEvent event) {
        //TODO
    }
    
    @Subscribe
    public void onBlockBurnEvent(final BlockBurnEvent event) {
        //TODO
    }
    
    @Subscribe
    public void onBlockDispenseEvent(final BlockDispenseEvent event) {
        //TODO
    }
    
    @Subscribe
    public void onBlockIgniteEvent(final BlockIgniteEvent event) {
        //TODO
    }
    
    @Subscribe
    public void onBlockInteractEvent(final BlockInteractEvent event) {
        //TODO
    }
    
    @Subscribe
    public void onBlockMoveEvent(final BlockMoveEvent event) {
        //TODO
    }
    
    @Subscribe
    public void onBlockUpdateEvent(final BlockUpdateEvent event) {
        //TODO
    }
    
    @Subscribe
    public void onBulkBlockEvent(final BulkBlockEvent event) {
        //TODO
    }
    
    @Subscribe
    public void onFloraGrowEvent(final FloraGrowEvent event) {
        //TODO
    }
    
    @Subscribe
    public void onFluidSpreadEvent(final FluidSpreadEvent event) {
        //TODO
    }
    
    @Subscribe
    public void onLeafDecayEvent(final LeafDecayEvent event) {
        //TODO
    }

    @Subscribe
    public void onPlayerInteract(final PlayerInteractEvent event) {
        //TODO
    }
}
