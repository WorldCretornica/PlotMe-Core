package com.worldcretornica.plotme_core.sponge;

import com.worldcretornica.plotme_core.PlotMapInfo;
import com.worldcretornica.plotme_core.PlotMeCoreManager;
import com.worldcretornica.plotme_core.PlotMe_Core;
import com.worldcretornica.plotme_core.sponge.api.*;
import org.spongepowered.api.entity.player.Player;
import org.spongepowered.api.entity.projectile.source.ProjectileSource;
import org.spongepowered.api.event.block.BlockBurnEvent;
import org.spongepowered.api.event.block.BlockChangeEvent;
import org.spongepowered.api.event.block.BlockDispenseEvent;
import org.spongepowered.api.event.block.BlockIgniteEvent;
import org.spongepowered.api.event.block.BlockInteractEvent;
import org.spongepowered.api.event.block.BlockMoveEvent;
import org.spongepowered.api.event.block.BlockUpdateEvent;
import org.spongepowered.api.event.block.BulkBlockEvent;
import org.spongepowered.api.event.block.FloraGrowEvent;
import org.spongepowered.api.event.block.FluidSpreadEvent;
import org.spongepowered.api.event.block.LeafDecayEvent;
import org.spongepowered.api.event.entity.ProjectileLaunchEvent;
import org.spongepowered.api.event.entity.living.player.PlayerInteractEvent;
import org.spongepowered.api.util.event.Subscribe;

public class SpongePlotListener {

    private final PlotMe_Sponge plugin;
    private final PlotMe_Core api;

    public SpongePlotListener(PlotMe_Sponge instance) {
        api = instance.getAPI();
        this.plugin = instance;
    }

    @Subscribe
    public void onBlockBreak(BlockChangeEvent event) {
        //TODO
    }

    @Subscribe
    public void onBlockBurnEvent(BlockBurnEvent event) {
        //TODO
    }

    @Subscribe
    public void onBlockDispenseEvent(BlockDispenseEvent event) {
        //TODO
    }

    @Subscribe
    public void onBlockIgniteEvent(BlockIgniteEvent event) {
        //TODO
    }

    @Subscribe
    public void onBlockInteractEvent(BlockInteractEvent event) {
        //TODO
    }

    @Subscribe
    public void onBlockMoveEvent(BlockMoveEvent event) {
        //TODO
    }

    @Subscribe
    public void onBlockUpdateEvent(BlockUpdateEvent event) {
        //TODO
    }

    @Subscribe
    public void onBulkBlockEvent(BulkBlockEvent event) {
        //TODO
    }

    @Subscribe
    public void onFloraGrowEvent(FloraGrowEvent event) {
        //TODO
    }

    @Subscribe
    public void onFluidSpreadEvent(FluidSpreadEvent event) {
        //TODO
    }

    @Subscribe
    public void onLeafDecayEvent(LeafDecayEvent event) {
        //TODO
    }

    @Subscribe
    public void onPlayerInteract(PlayerInteractEvent event) {
        //TODO
    }

    @Subscribe
    public void onProjectileEvent(ProjectileLaunchEvent event) {
        SpongeWorld world = new SpongeWorld(event.getEntity().getWorld());
        PlotMapInfo pmi = PlotMeCoreManager.getInstance().getMap(world);
        if (!pmi.canUseProjectiles()) {
            ProjectileSource source = event.getSource().orNull();
            if (source != null) {
                if (source instanceof Player) {
                    //TODO Send message to player
                }
            }
        } else {
            return;
        }
    }
}
