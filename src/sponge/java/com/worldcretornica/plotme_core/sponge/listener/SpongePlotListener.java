package com.worldcretornica.plotme_core.sponge.listener;

import com.worldcretornica.plotme_core.PermissionNames;
import com.worldcretornica.plotme_core.Plot;
import com.worldcretornica.plotme_core.PlotId;
import com.worldcretornica.plotme_core.PlotMapInfo;
import com.worldcretornica.plotme_core.PlotMeCoreManager;
import com.worldcretornica.plotme_core.PlotMe_Core;
import com.worldcretornica.plotme_core.sponge.PlotMe_Sponge;
import com.worldcretornica.plotme_core.sponge.api.SpongeLocation;
import com.worldcretornica.plotme_core.sponge.api.SpongeWorld;
import org.spongepowered.api.entity.player.Player;
import org.spongepowered.api.entity.projectile.source.ProjectileSource;
import org.spongepowered.api.event.Subscribe;
import org.spongepowered.api.event.block.BlockBurnEvent;
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
import org.spongepowered.api.event.entity.player.PlayerChangeBlockEvent;
import org.spongepowered.api.event.entity.player.PlayerInteractEvent;

public class SpongePlotListener {

    private final PlotMe_Sponge plugin;
    private final PlotMe_Core api;
    private final PlotMeCoreManager manager;

    public SpongePlotListener(PlotMe_Sponge instance) {
        //noinspection ConstantConditions
        api = instance.getAPI();
        this.plugin = instance;
        manager = PlotMeCoreManager.getInstance();
    }

    //This event is triggered for both placing and breaking blocks.
    @Subscribe
    public void onBlockChange(PlayerChangeBlockEvent event) {
        SpongeLocation location = new SpongeLocation(event.getBlock());
        if (manager.isPlotWorld(location)) {
            PlotId id = manager.getPlotId(location);
            Player player = event.getPlayer();
            boolean cannotBuild = !player.hasPermission(PermissionNames.ADMIN_BUILDANYWHERE);
            if (id == null) {
                event.setCancelled(true);
            } else {
                boolean ptc = api.isPlotLocked(id);
                if (ptc) {
                    event.setCancelled(true);
                } else {
                    Plot plot = manager.getMap(location).getPlot(id);

                    if (plot == null || !plot.isAllowed(player.getUniqueId())) {
                        if (cannotBuild) {
                            //player.sendMessage(api.C("ErrCannotBuild"));
                            event.setCancelled(true);
                        }
                    } else {
                        plot.resetExpire(manager.getMap(location).getDaysToExpiration());
                    }
                }
            }
        }
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
            if (source instanceof Player) {
                //noinspection OverlyStrongTypeCast
                //((Player) source).sendMessage("");
                event.getLaunchedProjectile().remove();
            }
        }
    }
}
