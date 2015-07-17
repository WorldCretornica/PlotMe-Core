package com.worldcretornica.plotme_core.bukkit.listener;

import com.google.common.base.Optional;
import com.worldcretornica.plotme_core.PermissionNames;
import com.worldcretornica.plotme_core.Plot;
import com.worldcretornica.plotme_core.PlotId;
import com.worldcretornica.plotme_core.PlotMapInfo;
import com.worldcretornica.plotme_core.PlotMeCoreManager;
import com.worldcretornica.plotme_core.PlotMe_Core;
import com.worldcretornica.plotme_core.api.IEntity;
import com.worldcretornica.plotme_core.api.IPlayer;
import com.worldcretornica.plotme_core.api.Location;
import com.worldcretornica.plotme_core.api.event.PlotCreateEvent;
import com.worldcretornica.plotme_core.api.event.PlotWorldLoadEvent;
import com.worldcretornica.plotme_core.api.event.eventbus.Order;
import com.worldcretornica.plotme_core.api.event.eventbus.Subscribe;
import com.worldcretornica.plotme_core.bukkit.BukkitUtil;
import com.worldcretornica.plotme_core.bukkit.PlotMe_CorePlugin;
import com.worldcretornica.plotme_core.bukkit.api.BukkitEntity;
import com.worldcretornica.plotme_core.bukkit.api.BukkitPlayer;
import com.worldcretornica.plotme_core.bukkit.api.BukkitWorld;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockDamageEvent;
import org.bukkit.event.block.BlockExplodeEvent;
import org.bukkit.event.block.BlockFadeEvent;
import org.bukkit.event.block.BlockFormEvent;
import org.bukkit.event.block.BlockFromToEvent;
import org.bukkit.event.block.BlockGrowEvent;
import org.bukkit.event.block.BlockIgniteEvent;
import org.bukkit.event.block.BlockPistonExtendEvent;
import org.bukkit.event.block.BlockPistonRetractEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.block.BlockSpreadEvent;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.event.hanging.HangingBreakByEntityEvent;
import org.bukkit.event.hanging.HangingPlaceEvent;
import org.bukkit.event.player.PlayerArmorStandManipulateEvent;
import org.bukkit.event.player.PlayerBucketEmptyEvent;
import org.bukkit.event.player.PlayerBucketFillEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.server.ServerCommandEvent;
import org.bukkit.event.world.StructureGrowEvent;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;

import java.util.Calendar;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;

public class BukkitPlotListener implements Listener {

    private final PlotMe_Core api;
    private final PlotMe_CorePlugin plugin;
    private final PlotMeCoreManager manager;

    public BukkitPlotListener(PlotMe_CorePlugin plotMeCorePlugin) {
        api = plotMeCorePlugin.getAPI();
        this.plugin = plotMeCorePlugin;
        manager = PlotMeCoreManager.getInstance();
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onBlockBreak(BlockBreakEvent event) {
        IPlayer player = plugin.wrapPlayer(event.getPlayer());
        org.bukkit.Location bloc = event.getBlock().getLocation();
        Location location = BukkitUtil.adapt(bloc);

        if (manager.isPlotWorld(location)) {
            if (player.hasPermission(PermissionNames.ADMIN_BUILDANYWHERE)) {
                return;
            }
            Plot plot = manager.getPlot(location);
            if (plot == null) {
                player.sendMessage(api.C("CannotBuild"));
                event.setCancelled(true);
            } else {
                if (plot.getOwnerId().equals(player.getUniqueId())) {
                    return;
                }
                Optional<Plot.AccessLevel> member = plot.isMember(player.getUniqueId());
                if (member.isPresent()) {
                    if (member.get().equals(Plot.AccessLevel.TRUSTED) && !api.getServerBridge().getOfflinePlayer(plot.getOwnerId()).isOnline()) {
                        player.sendMessage(api.C("CannotBuild"));
                        event.setCancelled(true);
                        return;
                    } else if (api.isPlotLocked(plot.getId())) {
                        player.sendMessage(api.C("PlotLocked"));
                        event.setCancelled(true);
                    }
                    if (plot.getExpiredDate() != null) {
                        Calendar instance = Calendar.getInstance();
                        instance.add(Calendar.DAY_OF_YEAR, manager.getMap(player).getDaysToExpiration());
                        if (!plot.getExpiredDate().equals(instance.getTime())) {
                            plot.resetExpire(manager.getMap(player).getDaysToExpiration());
                            plugin.getAPI().getSqlManager().savePlot(plot);
                        }
                    }
                } else {
                    player.sendMessage(api.C("CannotBuild"));
                    event.setCancelled(true);
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onBlockPlace(BlockPlaceEvent event) {
        IPlayer player = plugin.wrapPlayer(event.getPlayer());
        Location location = BukkitUtil.adapt(event.getBlockPlaced().getLocation());

        if (player.hasPermission(PermissionNames.ADMIN_BUILDANYWHERE)) {
            return;
        }
        if (manager.isPlotWorld(location)) {
            Plot plot = manager.getPlot(location);
            if (plot == null) {
                player.sendMessage(api.C("CannotBuild"));
                event.setCancelled(true);
            } else {
                if (plot.getOwnerId().equals(player.getUniqueId())) {
                    return;
                }
                Optional<Plot.AccessLevel> member = plot.isMember(player.getUniqueId());
                if (member.isPresent()) {
                    if (member.get().equals(Plot.AccessLevel.TRUSTED) && !api.getServerBridge().getOfflinePlayer(plot.getOwnerId()).isOnline()) {
                        player.sendMessage(api.C("CannotBuild"));
                        event.setCancelled(true);
                        return;
                    } else if (api.isPlotLocked(plot.getId())) {
                        player.sendMessage(api.C("PlotLocked"));
                        event.setCancelled(true);
                    }
                    if (plot.getExpiredDate() != null) {
                        Calendar instance = Calendar.getInstance();
                        instance.add(Calendar.DAY_OF_YEAR, manager.getMap(player).getDaysToExpiration());
                        if (!plot.getExpiredDate().equals(instance.getTime())) {
                            plot.resetExpire(manager.getMap(player).getDaysToExpiration());
                            plugin.getAPI().getSqlManager().savePlot(plot);
                        }
                    }
                } else {
                    player.sendMessage(api.C("CannotBuild"));
                    event.setCancelled(true);
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onBucketEmptyEvent(PlayerBucketEmptyEvent event) {
        IPlayer player = plugin.wrapPlayer(event.getPlayer());
        Location location = BukkitUtil.adapt(event.getBlockClicked().getLocation());

        if (player.hasPermission(PermissionNames.ADMIN_BUILDANYWHERE)) {
            return;
        }
        if (manager.isPlotWorld(location)) {
            Plot plot = manager.getPlot(location.add(event.getBlockFace().getModX(), event.getBlockFace().getModY(), event.getBlockFace().getModZ()));
            if (plot == null) {
                player.sendMessage(api.C("CannotBuild"));
                event.setCancelled(true);
            } else {
                if (plot.getOwnerId().equals(event.getPlayer().getUniqueId())) {
                    return;
                }
                Optional<Plot.AccessLevel> member = plot.isMember(player.getUniqueId());
                if (member.isPresent()) {
                    if (member.get().equals(Plot.AccessLevel.TRUSTED) && !api.getServerBridge().getOfflinePlayer(plot.getOwnerId()).isOnline()) {
                        player.sendMessage(api.C("CannotBuild"));
                        event.setCancelled(true);
                    } else if (api.isPlotLocked(plot.getId())) {
                        player.sendMessage(api.C("PlotLocked"));
                        event.setCancelled(true);
                    }
                } else {
                    player.sendMessage(api.C("CannotBuild"));
                    event.setCancelled(true);
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onBucketFillEvent(PlayerBucketFillEvent event) {
        IPlayer player = plugin.wrapPlayer(event.getPlayer());
        Location location = BukkitUtil.adapt(event.getBlockClicked().getLocation());

        if (player.hasPermission(PermissionNames.ADMIN_BUILDANYWHERE)) {
            return;
        }
        if (manager.isPlotWorld(location)) {
            Plot plot = manager.getPlot(location);

            if (plot == null) {
                player.sendMessage(api.C("CannotBuild"));
                event.setCancelled(true);
            } else {
                if (plot.getOwnerId().equals(event.getPlayer().getUniqueId())) {
                    return;
                }
                Optional<Plot.AccessLevel> member = plot.isMember(player.getUniqueId());
                if (member.isPresent()) {
                    if (member.get().equals(Plot.AccessLevel.TRUSTED) && !api.getServerBridge().getOfflinePlayer(plot.getOwnerId()).isOnline()) {
                        player.sendMessage(api.C("CannotBuild"));
                        event.setCancelled(true);
                    } else if (api.isPlotLocked(plot.getId())) {
                        player.sendMessage(api.C("PlotLocked"));
                        event.setCancelled(true);
                    }
                } else {
                    player.sendMessage(api.C("CannotBuild"));
                    event.setCancelled(true);
                }
            }
        }
    }

    @SuppressWarnings("deprecation")
    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onPlayerInteract(PlayerInteractEvent event) {
        IPlayer player = plugin.wrapPlayer(event.getPlayer());
        Location location = BukkitUtil.adapt(event.getClickedBlock().getLocation());
        PlotMapInfo pmi = manager.getMap(location);
        if (player.hasPermission(PermissionNames.ADMIN_BUILDANYWHERE)) {
            return;
        }
        if (manager.isPlotWorld(location)) {
            Plot plot = manager.getPlot(location);
            if (plot == null) {
                player.sendMessage(api.C("CannotBuild"));
                event.setCancelled(true);
            } else if (!plot.getOwnerId().equals(event.getPlayer().getUniqueId())) {
                Optional<Plot.AccessLevel> member = plot.isMember(player.getUniqueId());
                if (member.isPresent()) {
                    if (member.get().equals(Plot.AccessLevel.TRUSTED)) {
                        if (!api.getServerBridge().getOfflinePlayer(plot.getOwnerId())
                                .isOnline()) {
                            if (event.hasBlock() && pmi.isProtectedBlock(event.getClickedBlock().getTypeId())) {
                                if (!player.hasPermission("plotme.unblock." + event.getClickedBlock().getTypeId())) {
                                    player.sendMessage(api.C("CannotBuild"));
                                    event.setCancelled(true);
                                    return;
                                } else {
                                    return;
                                }
                            }
                            if (event.hasItem() && (pmi.isPreventedItem(String.valueOf(event.getItem().getTypeId())) || pmi
                                    .isPreventedItem(event.getItem().getTypeId() + ":" + event.getItem().getData()))) {
                                if (!player.hasPermission("plotme.unblock." + event.getClickedBlock().getTypeId())) {
                                    player.sendMessage(api.C("CannotBuild"));
                                    event.setCancelled(true);
                                }

                            }
                        }
                    }
                } else {
                    if (event.hasBlock() && pmi.isProtectedBlock(event.getClickedBlock().getTypeId())) {
                        if (player.hasPermission("plotme.unblock." + event.getClickedBlock().getTypeId())) {
                            return;
                        } else {
                            player.sendMessage(api.C("CannotBuild"));
                            event.setCancelled(true);
                            return;
                        }
                    }
                    if (event.hasItem() && (pmi.isPreventedItem(String.valueOf(event.getItem().getTypeId())) || pmi.isPreventedItem(
                            event.getItem().getTypeId() + ":" + event.getItem().getData()))) {
                        if (!player.hasPermission("plotme.unblock." + event.getClickedBlock().getTypeId())) {
                            player.sendMessage(api.C("CannotBuild"));
                            event.setCancelled(true);
                        }

                    }
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onBlockSpread(BlockSpreadEvent event) {
        Location location = BukkitUtil.adapt(event.getBlock().getLocation());

        if (manager.isPlotWorld(location)) {
            PlotId id = manager.getPlotId(location);

            if (id == null) {
                event.setCancelled(true);
            } else {
                event.setCancelled(api.isPlotLocked(id));

            }
        }
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onBlockForm(BlockFormEvent event) {
        Location location = BukkitUtil.adapt(event.getBlock().getLocation());

        if (manager.isPlotWorld(location)) {
            PlotId id = manager.getPlotId(location);

            if (id == null) {
                event.setCancelled(true);
            } else {
                event.setCancelled(api.isPlotLocked(id));

            }
        }
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onBlockDamage(BlockDamageEvent event) {
        Location location = BukkitUtil.adapt(event.getBlock().getLocation());

        if (manager.isPlotWorld(location)) {
            PlotId id = manager.getPlotId(location);

            if (id == null) {
                event.setCancelled(true);
            } else {
                event.setCancelled(api.isPlotLocked(id));

            }
        }
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onBlockFade(BlockFadeEvent event) {
        Location location = BukkitUtil.adapt(event.getBlock().getLocation());

        if (manager.isPlotWorld(location)) {
            PlotId id = manager.getPlotId(location);

            if (id == null) {
                event.setCancelled(true);
            } else {
                event.setCancelled(api.isPlotLocked(id));

            }
        }
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onBlockFromTo(BlockFromToEvent event) {
        Location location = BukkitUtil.adapt(event.getBlock().getLocation());
        if (manager.isPlotWorld(location)) {
            PlotId id = manager.getPlotId(location);
            if (id == null) {
                event.setCancelled(true);
            } else {
                event.setCancelled(api.isPlotLocked(id));
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onBlockGrow(BlockGrowEvent event) {
        Location location = BukkitUtil.adapt(event.getBlock().getLocation());

        if (manager.isPlotWorld(location)) {
            PlotId id = manager.getPlotId(location);

            if (id == null) {
                event.setCancelled(true);
            } else {
                event.setCancelled(api.isPlotLocked(id));
            }
        }
    }

    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    public void onBlockPistonExtend(BlockPistonExtendEvent event) {
        BukkitWorld world = BukkitUtil.adapt(event.getBlock().getWorld());
        if (manager.isPlotWorld(world)) {
            BlockFace face = event.getDirection();

            for (Block block : event.getBlocks()) {
                PlotId id = manager.getPlotId(new Location(world, BukkitUtil.locationToVector(
                        block.getLocation().add(face.getModX(), face.getModY(),
                                face.getModZ()))));
                if (id == null) {
                    event.setCancelled(true);
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    public void onBlockPistonRetract(BlockPistonRetractEvent event) {
        BukkitWorld world = BukkitUtil.adapt(event.getBlock().getWorld());
        if (manager.isPlotWorld(world)) {
            List<Block> blocks = event.getBlocks();
            for (Block moved : blocks) {
                PlotId id = manager.getPlotId(new Location(world, BukkitUtil.locationToVector(moved.getLocation())));
                if (id == null) {
                    event.setCancelled(true);
                } else {
                    event.setCancelled(api.isPlotLocked(id));

                }
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onStructureGrow(StructureGrowEvent event) {
        BukkitWorld world = new BukkitWorld(event.getWorld());
        if (manager.isPlotWorld(world)) {
            for (int i = 0; i < event.getBlocks().size(); i++) {
                PlotId id = manager.getPlotId(BukkitUtil.adapt(event.getBlocks().get(i).getLocation()));
                if (id == null) {
                    event.getBlocks().remove(i);
                    i--;
                } else {
                    event.setCancelled(api.isPlotLocked(id));
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onEntityExplode(EntityExplodeEvent event) {
        IEntity entity = plugin.wrapEntity(event.getEntity());
        if (manager.isPlotWorld(entity)) {
            PlotMapInfo pmi = manager.getMap(entity.getLocation());

            if (pmi != null && pmi.isDisableExplosion()) {
                event.setCancelled(true);
            } else {
                PlotId id = manager.getPlotId(entity.getLocation());
                if (id == null) {
                    event.setCancelled(true);
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onBlockExplode(BlockExplodeEvent event) {
        Location location = BukkitUtil.adapt(event.getBlock().getLocation());
        PlotMapInfo pmi = manager.getMap(location);

        if (pmi != null && pmi.isDisableExplosion()) {
            event.setCancelled(true);
        } else {
            PlotId id = manager.getPlotId(location);
            if (id == null) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onBlockIgnite(BlockIgniteEvent event) {
        if (event.getIgnitingEntity() == null) {
            return;
        }
        Location location = BukkitUtil.adapt(event.getBlock().getLocation());

        PlotMapInfo pmi = manager.getMap(location);

        if (pmi != null) {
            if (pmi.isDisableIgnition()) {
                event.setCancelled(true);
            } else {
                Plot plot = manager.getPlot(location);
                if (plot == null) {
                    event.setCancelled(true);
                } else {
                    if (plot.getOwnerId().equals(event.getPlayer().getUniqueId())) {
                        return;
                    }
                    Optional<Plot.AccessLevel> member = plot.isMember(event.getIgnitingEntity().getUniqueId());
                    if (member.isPresent()) {
                        if (member.get().equals(Plot.AccessLevel.TRUSTED) && !api.getServerBridge().getOfflinePlayer(plot.getOwnerId()).isOnline()) {
                            event.setCancelled(true);
                        } else if (api.isPlotLocked(plot.getId())) {
                            event.setCancelled(true);
                        }
                    } else {
                        event.setCancelled(true);
                    }
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    public void onHangingPlace(HangingPlaceEvent event) {
        IPlayer player = plugin.wrapPlayer(event.getPlayer());
        Location location = BukkitUtil.adapt(event.getBlock().getLocation());

        if (manager.isPlotWorld(location.getWorld())) {
            if (player.hasPermission(PermissionNames.ADMIN_BUILDANYWHERE)) {
                return;
            }

            Plot plot = manager.getPlot(location);

            if (plot == null) {
                player.sendMessage(api.C("CannotBuild"));
                event.setCancelled(true);
            } else {
                if (plot.getOwnerId().equals(event.getPlayer().getUniqueId())) {
                    return;
                }
                Optional<Plot.AccessLevel> member = plot.isMember(player.getUniqueId());
                if (member.isPresent()) {
                    if (member.get().equals(Plot.AccessLevel.TRUSTED) && !api.getServerBridge().getOfflinePlayer(plot.getOwnerId()).isOnline()) {
                        player.sendMessage(api.C("CannotBuild"));
                        event.setCancelled(true);
                    } else if (api.isPlotLocked(plot.getId())) {
                        player.sendMessage(api.C("PlotLocked"));
                        event.setCancelled(true);
                    }
                } else {
                    player.sendMessage(api.C("CannotBuild"));
                    event.setCancelled(true);
                }
            }
        }

    }

    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    public void onHangingBreakByEntity(HangingBreakByEntityEvent event) {

        if (event.getRemover() instanceof Player) {
            BukkitPlayer player = (BukkitPlayer) plugin.wrapPlayer((Player) event.getRemover());

            if (manager.isPlotWorld(player)) {
                if (player.hasPermission(PermissionNames.ADMIN_BUILDANYWHERE)) {
                    return;
                }

                Plot plot = manager.getPlot(player);

                if (plot == null) {
                    player.sendMessage(api.C("CannotBuild"));
                    event.setCancelled(true);
                } else {
                    if (plot.getOwnerId().equals(player.getUniqueId())) {
                        return;
                    }
                    Optional<Plot.AccessLevel> member = plot.isMember(player.getUniqueId());
                    if (member.isPresent()) {
                        if (member.get().equals(Plot.AccessLevel.TRUSTED) && !api.getServerBridge().getOfflinePlayer(plot.getOwnerId()).isOnline()) {
                            player.sendMessage(api.C("CannotBuild"));
                            event.setCancelled(true);
                        } else if (api.isPlotLocked(plot.getId())) {
                            player.sendMessage(api.C("PlotLocked"));
                            event.setCancelled(true);
                        }
                    } else {
                        player.sendMessage(api.C("CannotBuild"));
                        event.setCancelled(true);
                    }
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onPlayerInteractEntity(PlayerInteractEntityEvent event) {
        Location location = BukkitUtil.adapt(event.getRightClicked().getLocation());
        if (manager.isPlotWorld(location)) {
            //Citizens Support
            if (event.getRightClicked().hasMetadata("NPC") && event.getRightClicked().getMetadata("NPC").get(0).asBoolean()) {
                return;
            }

            Plot plot = manager.getPlot(location);

            if (plot == null) {
                if (!event.getPlayer().hasPermission(PermissionNames.ADMIN_BUILDANYWHERE)) {
                    event.getPlayer().sendMessage(api.C("CannotBuild"));
                    event.setCancelled(true);
                }
            } else {
                if (plot.getOwnerId().equals(event.getPlayer().getUniqueId())) {
                    return;
                }
                if (!event.getPlayer().hasPermission(PermissionNames.ADMIN_BUILDANYWHERE)) {
                    Optional<Plot.AccessLevel> member = plot.isMember(event.getPlayer().getUniqueId());
                    if (plot.getOwnerId().equals(event.getPlayer().getUniqueId())) {
                        return;
                    }
                    if (member.isPresent()) {
                        if (member.get().equals(Plot.AccessLevel.TRUSTED) && !api.getServerBridge().getOfflinePlayer(plot.getOwnerId()).isOnline()) {
                            event.getPlayer().sendMessage(api.C("CannotBuild"));
                            event.setCancelled(true);
                        } else if (api.isPlotLocked(plot.getId())) {
                            event.getPlayer().sendMessage(api.C("PlotLocked"));
                            event.setCancelled(true);
                        }
                    } else {
                        event.getPlayer().sendMessage(api.C("CannotBuild"));
                        event.setCancelled(true);
                    }
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onProjectileLaunchEvent(ProjectileLaunchEvent event) {
        if (event.getEntity() instanceof Player) {
            PlotMapInfo pmi = manager.getMap(BukkitUtil.adapt(event.getEntity().getWorld()));
            if (pmi != null && !pmi.canUseProjectiles()) {
                event.getEntity().sendMessage(api.C("ErrCannotUseEggs"));
                event.setCancelled(true);
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onCreatureSpawn(CreatureSpawnEvent event) {

        Location location = BukkitUtil.adapt(event.getLocation());

        if (manager.isPlotWorld(location)) {
            PlotId id = manager.getPlotId(location);
            if (id != null) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onEntityDamage(EntityDamageEvent event) {
        Location loc = BukkitUtil.adapt(event.getEntity().getLocation());
        if (manager.isPlotWorld(loc)) {
            //Don't protect Monsters!
            if (event.getEntity() instanceof Monster) {
                return;
            }
            //This includes everything except for Monsters which were excluded above.
            if (event.getCause().equals(EntityDamageEvent.DamageCause.ENTITY_ATTACK) && event instanceof EntityDamageByEntityEvent) {
                EntityDamageByEntityEvent damageByEntityEvent = (EntityDamageByEntityEvent) event;
                //Specific to Players to allow PVP. event.getEntity() is the damaged entity
                if (event.getEntity() instanceof Player) {
                    //Don't allow PVP on the roads. Only allow pvp if both entities are in the plot.
                    Plot plot = manager.getPlot(loc);
                    Plot plot2 = manager.getPlot(BukkitUtil.adapt(damageByEntityEvent.getDamager().getLocation()));
                    if (plot == null) {
                        event.setCancelled(true);
                    }
                    if (plot2 == null) {
                        event.setCancelled(true);
                    }
                } else {
                    Plot plot = manager.getPlot(loc);
                    if (plot == null) {
                        event.setCancelled(true);
                    } else {
                        if (plot.getOwnerId().equals(((EntityDamageByEntityEvent) event).getDamager().getUniqueId())) {
                            return;
                        }
                        Optional<Plot.AccessLevel> member = plot.isMember(((EntityDamageByEntityEvent) event).getDamager().getUniqueId());
                        if (member.isPresent() && member.get().equals(Plot.AccessLevel.TRUSTED) && !api.getServerBridge()
                                .getOfflinePlayer(plot.getOwnerId()).isOnline()) {
                            event.setCancelled(true);
                        } else {
                            return;
                        }
                    }
                    event.setCancelled(true);
                }
            }
        }
    }

    @EventHandler
    public void onArmorStand(PlayerArmorStandManipulateEvent event) {
        Location location = BukkitUtil.adapt(event.getRightClicked().getLocation());

        if (manager.isPlotWorld(location)) {
            if (event.getPlayer().hasPermission(PermissionNames.ADMIN_BUILDANYWHERE)) {
                return;
            }

            Plot plot = manager.getPlot(location);

            if (plot == null) {
                event.getPlayer().sendMessage(api.C("CannotBuild"));
                event.setCancelled(true);
            } else {
                if (plot.getOwnerId().equals(event.getPlayer().getUniqueId())) {
                    return;
                }
                Optional<Plot.AccessLevel> member = plot.isMember(event.getPlayer().getUniqueId());
                if (member.isPresent()) {
                    if (member.get().equals(Plot.AccessLevel.TRUSTED) && !api.getServerBridge().getOfflinePlayer(plot.getOwnerId()).isOnline()) {
                        event.getPlayer().sendMessage(api.C("CannotBuild"));
                        event.setCancelled(true);
                    } else if (api.isPlotLocked(plot.getId())) {
                        event.getPlayer().sendMessage(api.C("PlotLocked"));
                        event.setCancelled(true);
                    }
                } else {
                    event.getPlayer().sendMessage(api.C("CannotBuild"));
                    event.setCancelled(true);
                }
            }
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onSandCannon(EntityChangeBlockEvent event) {
        BukkitEntity entity = new BukkitEntity(event.getEntity());
        if (manager.isPlotWorld(entity) && event.getEntityType().equals(EntityType.FALLING_BLOCK)) {
            if (event.getTo().equals(Material.AIR)) {
                entity.setMetadata("plotFallBlock", new FixedMetadataValue(plugin, event.getBlock().getLocation()));
            } else {
                List<MetadataValue> values = entity.getMetadata("plotFallBlock");

                if (!values.isEmpty()) {

                    org.bukkit.Location spawn = (org.bukkit.Location) (values.get(0).value());
                    org.bukkit.Location createdNew = event.getBlock().getLocation();

                    if (spawn.getBlockX() != createdNew.getBlockX() || spawn.getBlockZ() != createdNew.getBlockZ()) {
                        event.setCancelled(true);
                    }
                }
            }
        }
    }

    @Subscribe(order = Order.FIRST)
    public void onPlotCreateFirst(PlotCreateEvent event) {
        api.getLogger().info("First Plot Create Event");
    }

    @Subscribe(order = Order.EARLY)
    public void onPlotCreateEarly(PlotCreateEvent event) {
        api.getLogger().info("Early Plot Create Event");
    }

    @Subscribe(order = Order.LATE)
    public void onPlotWorldLoad(PlotCreateEvent event) {
        api.getLogger().info("Late Plot Create Event");
    }

    @Subscribe
    public void onPlotWorldLoad(PlotWorldLoadEvent event) {
        api.getLogger().log(Level.INFO, "Done loading {0} plots for world {1}", new Object[]{event.getNbPlots(), event
                .getWorldName()});
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerQuit(PlayerQuitEvent event) {
        UUID playerUUID = event.getPlayer().getUniqueId();
        plugin.removePlayer(playerUUID);
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player p = event.getPlayer();

        if (p != null) {
            manager.UpdatePlayerNameFromId(p.getUniqueId(), p.getName());
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onSignEdit(SignChangeEvent event) {
        IPlayer player = plugin.wrapPlayer(event.getPlayer());
        Location location = BukkitUtil.adapt(event.getBlock().getLocation());

        if (manager.isPlotWorld(location)) {
            if (event.getPlayer().hasPermission(PermissionNames.ADMIN_BUILDANYWHERE)) {
                return;
            }
            Plot plot = manager.getPlot(location);
            if (plot == null) {
                player.sendMessage(api.C("CannotBuild"));
                event.setCancelled(true);
            } else {
                Optional<Plot.AccessLevel> member = plot.isMember(event.getPlayer().getUniqueId());
                if (plot.getOwnerId().equals(event.getPlayer().getUniqueId())) {
                    return;
                }
                if (member.isPresent()) {
                    if (member.get().equals(Plot.AccessLevel.TRUSTED) && !api.getServerBridge().getOfflinePlayer(plot.getOwnerId()).isOnline()) {
                        player.sendMessage(api.C("CannotBuild"));
                        event.setCancelled(true);
                    } else if (api.isPlotLocked(plot.getId())) {
                        player.sendMessage(api.C("PlotLocked"));
                        event.setCancelled(true);
                    }
                } else {
                    player.sendMessage(api.C("CannotBuild"));
                    event.setCancelled(true);
                }
            }
        }
    }

    @EventHandler
    public void onCommand(PlayerCommandPreprocessEvent event) {
        if (event.getMessage().equalsIgnoreCase("/reload") || event.getMessage().equalsIgnoreCase("reload")) {
            event.setCancelled(true);
            event.getPlayer().sendMessage("PlotMe disabled /reload to prevent errors from occuring.");
        }
    }

    @EventHandler
    public void onCommand(ServerCommandEvent event) {
        if (event.getCommand().equalsIgnoreCase("/reload") || event.getCommand().equalsIgnoreCase("reload")) {
            event.setCommand("");
            event.getSender().sendMessage("PlotMe disabled /reload to prevent errors from occuring.");
        }
    }
}
