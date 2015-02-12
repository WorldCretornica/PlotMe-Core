package com.worldcretornica.plotme_core.sponge.listener;

import com.worldcretornica.plotme_core.*;
import com.worldcretornica.plotme_core.sponge.PlotMe_Sponge;
import com.worldcretornica.plotme_core.sponge.api.SpongeLocation;
import com.worldcretornica.plotme_core.sponge.api.SpongeWorld;
import org.spongepowered.api.entity.player.Player;
import org.spongepowered.api.entity.projectile.source.ProjectileSource;
import org.spongepowered.api.event.block.*;
import org.spongepowered.api.event.entity.ProjectileLaunchEvent;
import org.spongepowered.api.event.entity.living.player.PlayerChangeBlockEvent;
import org.spongepowered.api.event.entity.living.player.PlayerInteractEvent;
import org.spongepowered.api.util.event.Subscribe;

public class SpongePlotListener {

    private final PlotMe_Sponge plugin;
    private final PlotMe_Core api;
    private final PlotMeCoreManager manager;

    public SpongePlotListener(PlotMe_Sponge instance) {
        api = instance.getAPI();
        this.plugin = instance;
        manager = PlotMeCoreManager.getInstance();
    }

    //This event is triggered for both placing and breaking blocks.
    @Subscribe
    public void onBlockChange(PlayerChangeBlockEvent event) {
        SpongeLocation location = new SpongeLocation(event.getBlock().getLocation());
        if (manager.isPlotWorld(location)) {
            PlotId id = manager.getPlotId(location);
            Player player = event.getPlayer();
            boolean cannotBuild = !player.hasPermission(PermissionNames.ADMIN_BUILDANYWHERE);
            if (id == null) {
                event.setCancelled(true);
            } else {
                PlotToClear ptc = api.getPlotLocked(location.getWorld().getName(), id);
                if (ptc != null) {
                    switch (ptc.getReason()) {
                        case Clear:
                            player.sendMessage(api.getUtil().C("MsgPlotLockedClear"));
                            break;
                        case Reset:
                            player.sendMessage(api.getUtil().C("MsgPlotLockedReset"));
                            break;
                        case Expired:
                            player.sendMessage(api.getUtil().C("MsgPlotLockedExpired"));
                            break;
                    }
                    event.setCancelled(true);
                } else {
                    Plot plot = manager.getMap(location).getPlot(id);

                    if (plot == null || !plot.isAllowed(player.getName(), player.getUniqueId())) {
                        if (cannotBuild) {
                            player.sendMessage(api.getUtil().C("ErrCannotBuild"));
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
            if (source != null) {
                if (source instanceof Player) {
                    //noinspection OverlyStrongTypeCast
                    ((Player) source).sendMessage("");
                    event.getLaunchedProjectile().remove();
                }
            }
        }
    }
}
