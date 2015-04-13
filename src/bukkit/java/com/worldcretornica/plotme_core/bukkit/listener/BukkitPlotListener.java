package com.worldcretornica.plotme_core.bukkit.listener;

import com.worldcretornica.plotme_core.PermissionNames;
import com.worldcretornica.plotme_core.Plot;
import com.worldcretornica.plotme_core.PlotId;
import com.worldcretornica.plotme_core.PlotMapInfo;
import com.worldcretornica.plotme_core.PlotMeCoreManager;
import com.worldcretornica.plotme_core.PlotMe_Core;
import com.worldcretornica.plotme_core.api.IEntity;
import com.worldcretornica.plotme_core.api.ILocation;
import com.worldcretornica.plotme_core.api.IPlayer;
import com.worldcretornica.plotme_core.api.event.PlotCreateEvent;
import com.worldcretornica.plotme_core.api.event.PlotWorldLoadEvent;
import com.worldcretornica.plotme_core.api.event.eventbus.Order;
import com.worldcretornica.plotme_core.api.event.eventbus.Subscribe;
import com.worldcretornica.plotme_core.bukkit.PlotMe_CorePlugin;
import com.worldcretornica.plotme_core.bukkit.api.BukkitBlock;
import com.worldcretornica.plotme_core.bukkit.api.BukkitConverter;
import com.worldcretornica.plotme_core.bukkit.api.BukkitEntity;
import com.worldcretornica.plotme_core.bukkit.api.BukkitPlayer;
import com.worldcretornica.plotme_core.bukkit.api.BukkitWorld;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
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
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.event.hanging.HangingBreakByEntityEvent;
import org.bukkit.event.hanging.HangingPlaceEvent;
import org.bukkit.event.player.PlayerArmorStandManipulateEvent;
import org.bukkit.event.player.PlayerBucketEmptyEvent;
import org.bukkit.event.player.PlayerBucketFillEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.world.StructureGrowEvent;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.UUID;

public class BukkitPlotListener implements Listener {

    private final PlotMe_Core api;
    private final PlotMe_CorePlugin plugin;
    private final PlotMeCoreManager manager;

    public BukkitPlotListener(PlotMe_CorePlugin instance) {
        api = instance.getAPI();
        this.plugin = instance;
        manager = PlotMeCoreManager.getInstance();
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onBlockBreak(BlockBreakEvent event) {
        IPlayer player = plugin.wrapPlayer(event.getPlayer());
        Location bloc = event.getBlock().getLocation();

        ILocation location = new ILocation(player.getWorld(), bloc.getX(), bloc.getY(), bloc.getZ());

        if (manager.isPlotWorld(player.getWorld())) {
            boolean canBuild = !player.hasPermission(PermissionNames.ADMIN_BUILDANYWHERE);
            PlotId id = manager.getPlotId(location);

            if (id == null) {
                if (canBuild) {
                    player.sendMessage(api.C("ErrCannotBuild"));
                    event.setCancelled(true);
                }
            } else {
                boolean ptc = api.isPlotLocked(id);

                if (ptc) {
                    player.sendMessage(api.C("PlotLocked"));
                    event.setCancelled(true);
                } else {
                    Plot plot = manager.getPlotById(id);
                    if (plot == null || !plot.isAllowed(player.getUniqueId())) {
                        if (canBuild) {
                            player.sendMessage(api.C("ErrCannotBuild"));
                            event.setCancelled(true);
                        }
                    } else {
                        plot.resetExpire(manager.getMap(location).getDaysToExpiration());
                    }
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onBlockPlace(BlockPlaceEvent event) {
        IPlayer player = plugin.wrapPlayer(event.getPlayer());
        Location bloc = event.getBlockPlaced().getLocation();
        ILocation location = new ILocation(player.getWorld(), bloc.getX(), bloc.getY(), bloc.getZ());

        if (manager.isPlotWorld(location)) {
            boolean canBuild = !player.hasPermission(PermissionNames.ADMIN_BUILDANYWHERE);
            PlotId id = manager.getPlotId(location);

            if (id == null) {
                if (canBuild) {
                    player.sendMessage(api.C("ErrCannotBuild"));
                    event.setCancelled(true);
                }
            } else {
                boolean ptc = api.isPlotLocked(id);

                if (ptc) {
                    player.sendMessage(api.C("PlotLocked"));
                    event.setCancelled(true);
                } else {
                    Plot plot = manager.getMap(location).getPlot(id);

                    if (plot == null || !plot.isAllowed(player.getUniqueId())) {
                        if (canBuild) {
                            player.sendMessage(api.C("ErrCannotBuild"));
                            event.setCancelled(true);
                        }
                    } else {
                        plot.resetExpire(manager.getMap(location).getDaysToExpiration());
                    }
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onBucketEmptyEvent(PlayerBucketEmptyEvent event) {
        IPlayer player = plugin.wrapPlayer(event.getPlayer());
        ILocation location = new ILocation(player.getWorld(), BukkitConverter.locationToVector(event.getBlockClicked().getLocation()));

        if (!player.hasPermission(PermissionNames.ADMIN_BUILDANYWHERE) && manager.isPlotWorld(location)) {
            PlotId id =
                    manager.getPlotId(location.add(event.getBlockFace().getModX(), event.getBlockFace().getModY(), event.getBlockFace().getModZ()));
            if (id == null) {
                player.sendMessage(api.C("ErrCannotBuild"));
                event.setCancelled(true);
            } else {
                boolean ptc = api.isPlotLocked(id);

                if (ptc) {
                    player.sendMessage(api.C("PlotLocked"));
                    event.setCancelled(true);
                } else {
                    Plot plot = manager.getPlotById(id);

                    if (plot == null || !plot.isAllowed(player.getUniqueId())) {
                        player.sendMessage(api.C("ErrCannotBuild"));
                        event.setCancelled(true);
                    }
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onBucketFillEvent(PlayerBucketFillEvent event) {
        IPlayer player = plugin.wrapPlayer(event.getPlayer());
        Location bloc = event.getBlockClicked().getLocation();
        ILocation location = new ILocation(player.getWorld(), bloc.getX(), bloc.getY(), bloc.getZ());

        if (manager.isPlotWorld(location)) {
            if (!player.hasPermission(PermissionNames.ADMIN_BUILDANYWHERE)) {
                PlotId id = manager.getPlotId(location);

                if (id == null) {
                    player.sendMessage(api.C("ErrCannotBuild"));
                    event.setCancelled(true);
                } else {
                    boolean ptc = api.isPlotLocked(id);

                    if (ptc) {
                        player.sendMessage(api.C("PlotLocked"));
                        event.setCancelled(true);
                    } else {
                        Plot plot = manager.getPlotById(id);

                        if (plot == null || !plot.isAllowed(player.getUniqueId())) {
                            player.sendMessage(api.C("ErrCannotBuild"));
                            event.setCancelled(true);
                        }
                    }
                }
            }
        }
    }

    @SuppressWarnings("deprecation")
    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onPlayerInteract(PlayerInteractEvent event) {
        IPlayer player = plugin.wrapPlayer(event.getPlayer());
        ILocation location = new ILocation(player.getWorld(), BukkitConverter.locationToVector(event.getClickedBlock().getLocation()));
        if (manager.isPlotWorld(location)) {
            PlotId plotId = manager.getPlotId(location);
            if (plotId == null) {
                event.setCancelled(true);
                return;
            }
            boolean ptc = api.isPlotLocked(plotId);

            if (ptc) {
                player.sendMessage(api.C("PlotLocked"));
                event.setCancelled(true);
            } else {
                boolean canBuild = !player.hasPermission(PermissionNames.ADMIN_BUILDANYWHERE);
                PlotMapInfo pmi = manager.getMap(location);

                Plot plot = manager.getPlotById(plotId, pmi);
                if (event.isBlockInHand() && event.getAction() == Action.RIGHT_CLICK_BLOCK) {
                    if (plot == null || !plot.isAllowed(player.getUniqueId())) {
                        if (canBuild) {
                            player.sendMessage(api.C("ErrCannotBuild"));
                            event.setCancelled(true);
                        }
                    } else {
                        plot.resetExpire(pmi.getDaysToExpiration());
                    }
                } else {
                    boolean blocked = false;
                    if (pmi.isProtectedBlock(event.getClickedBlock().getTypeId()) && !player
                            .hasPermission("plotme.unblock." + event.getClickedBlock().getTypeId())) {
                        blocked = true;
                    }

                    ItemStack item = event.getItem();

                    if (event.getAction() == Action.RIGHT_CLICK_BLOCK && item != null) {
                        int itemId = item.getType().getId();
                        byte itemData = item.getData().getData();

                        if ((pmi.isPreventedItem(String.valueOf(itemId)) || pmi.isPreventedItem(itemId + ":" + itemData)) && !player
                                .hasPermission("plotme.unblock." + itemId) || item.getType().equals(Material.MONSTER_EGG)) {
                            blocked = true;
                        }
                    }

                    if (blocked) {
                        if (plot == null || !plot.isAllowed(player.getUniqueId())) {
                            if (canBuild) {
                                if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
                                    player.sendMessage(api.C("ErrCannotUse"));
                                }
                                event.setCancelled(true);
                            }
                        }
                    }
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onBlockSpread(BlockSpreadEvent event) {
        BukkitBlock block = new BukkitBlock(event.getBlock());

        if (manager.isPlotWorld(block.getWorld())) {
            PlotId id = manager.getPlotId(block.getLocation());

            if (id == null) {
                event.setCancelled(true);
            } else {
                boolean ptc = api.isPlotLocked(id);

                if (ptc) {
                    event.setCancelled(true);
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onBlockForm(BlockFormEvent event) {
        BukkitBlock block = new BukkitBlock(event.getBlock());

        if (manager.isPlotWorld(block.getWorld())) {
            PlotId id = manager.getPlotId(block.getLocation());

            if (id == null) {
                event.setCancelled(true);
            } else {
                boolean ptc = api.isPlotLocked(id);

                if (ptc) {
                    event.setCancelled(true);
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onBlockDamage(BlockDamageEvent event) {
        BukkitBlock block = new BukkitBlock(event.getBlock());

        if (manager.isPlotWorld(block.getWorld())) {
            PlotId id = manager.getPlotId(block.getLocation());

            if (id == null) {
                event.setCancelled(true);
            } else {
                boolean ptc = api.isPlotLocked(id);

                if (ptc) {
                    event.setCancelled(true);
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onBlockFade(BlockFadeEvent event) {
        BukkitBlock block = new BukkitBlock(event.getBlock());

        if (manager.isPlotWorld(block.getWorld())) {
            PlotId id = manager.getPlotId(block.getLocation());

            if (id == null) {
                event.setCancelled(true);
            } else {
                boolean ptc = api.isPlotLocked(id);

                if (ptc) {
                    event.setCancelled(true);
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onBlockFromTo(BlockFromToEvent event) {
        BukkitBlock block = new BukkitBlock(event.getToBlock());
        if (manager.isPlotWorld(block.getWorld())) {
            PlotId id = manager.getPlotId(block.getLocation());
            if (id == null) {
                event.setCancelled(true);
            } else {
                boolean ptc = api.isPlotLocked(id);

                if (ptc) {
                    event.setCancelled(true);
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onBlockGrow(BlockGrowEvent event) {
        BukkitBlock block = new BukkitBlock(event.getBlock());

        if (manager.isPlotWorld(block.getWorld())) {
            PlotId id = manager.getPlotId(block.getLocation());

            if (id == null) {
                event.setCancelled(true);
            } else {
                boolean ptc = api.isPlotLocked(id);

                if (ptc) {
                    event.setCancelled(true);
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    public void onBlockPistonExtend(BlockPistonExtendEvent event) {
        BukkitWorld world = BukkitConverter.adapt(event.getBlock().getWorld());
        if (manager.isPlotWorld(world)) {
            BlockFace face = event.getDirection();

            for (Block block : event.getBlocks()) {
                PlotId id = manager.getPlotId(new ILocation(world, BukkitConverter.locationToVector(
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
        BukkitWorld world = BukkitConverter.adapt(event.getBlock().getWorld());
        if (manager.isPlotWorld(world)) {
            List<Block> blocks = event.getBlocks();
            for (Block moved : blocks) {
                PlotId id = manager.getPlotId(new ILocation(world, BukkitConverter.locationToVector(moved.getLocation())));
                if (id == null) {
                    event.setCancelled(true);
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onStructureGrow(StructureGrowEvent event) {
        BukkitWorld world = new BukkitWorld(event.getWorld());
        if (manager.isPlotWorld(world)) {
            for (int i = 0; i < event.getBlocks().size(); i++) {
                PlotId id = manager.getPlotId(new ILocation(world, BukkitConverter.locationToVector(event.getBlocks().get(i).getLocation())));
                if (id == null) {
                    event.getBlocks().remove(i);
                    i--;
                } else {
                    boolean ptc = api.isPlotLocked(id);

                    if (ptc) {
                        event.setCancelled(true);
                    }
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
                } else {
                    boolean ptc = api.isPlotLocked(id);

                    if (ptc) {
                        event.setCancelled(true);
                    }
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onBlockExplode(BlockExplodeEvent event) {
        BukkitBlock location = new BukkitBlock(event.getBlock());
        PlotMapInfo pmi = manager.getMap(location.getLocation());

        if (pmi != null && pmi.isDisableExplosion()) {
            event.setCancelled(true);
        } else {
            PlotId id = manager.getPlotId(location.getLocation());
            if (id == null) {
                event.setCancelled(true);
            } else {
                boolean ptc = api.isPlotLocked(id);

                if (ptc) {
                    event.setCancelled(true);
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onBlockIgnite(BlockIgniteEvent event) {
        if (event.getIgnitingEntity() == null) {
            return;
        }
        BukkitBlock block = new BukkitBlock(event.getBlock());

        PlotMapInfo pmi = manager.getMap(block.getLocation());

        if (pmi != null) {
            if (pmi.isDisableIgnition()) {
                event.setCancelled(true);
            } else {
                PlotId id = manager.getPlotId(block.getLocation());

                if (id == null) {
                    event.setCancelled(true);
                } else {
                    boolean ptc = api.isPlotLocked(id);

                    if (ptc) {
                        event.setCancelled(true);
                    } else {
                        Player player = null;
                        if (event.getPlayer() != null) {
                            player = event.getPlayer();
                        }
                        Plot plot = manager.getPlotById(id, pmi);

                        if (plot == null || player != null && !plot.isAllowed(player.getUniqueId()) && !player
                                .hasPermission(PermissionNames.ADMIN_BUILDANYWHERE)) {
                            event.setCancelled(true);
                        }
                    }
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onHangingPlace(HangingPlaceEvent event) {
        IPlayer player = plugin.wrapPlayer(event.getPlayer());
        ILocation location = new ILocation(player.getWorld(), BukkitConverter.locationToVector(event.getBlock().getLocation()));

        if (manager.isPlotWorld(location.getWorld())) {
            PlotId id = manager.getPlotId(location);
            boolean canBuild = !player.hasPermission(PermissionNames.ADMIN_BUILDANYWHERE);

            if (id == null) {
                if (canBuild) {
                    player.sendMessage(api.C("ErrCannotBuild"));
                    event.setCancelled(true);
                }
            } else {
                boolean ptc = api.isPlotLocked(id);

                if (ptc) {
                    player.sendMessage(api.C("PlotLocked"));
                    event.setCancelled(true);
                } else {
                    Plot plot = manager.getPlotById(id);

                    if (plot == null || !plot.isAllowed(player.getUniqueId())) {
                        if (canBuild) {
                            player.sendMessage(api.C("ErrCannotBuild"));
                            event.setCancelled(true);
                        }
                    } else {
                        plot.resetExpire(manager.getMap(location).getDaysToExpiration());
                    }
                }
            }
        }

    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onHangingBreakByEntity(HangingBreakByEntityEvent event) {

        if (event.getRemover() instanceof Player) {
            BukkitPlayer player = (BukkitPlayer) plugin.wrapPlayer((Player) event.getRemover());

            boolean canBuild = !player.hasPermission(PermissionNames.ADMIN_BUILDANYWHERE);

            if (manager.isPlotWorld(player.getWorld())) {
                PlotId id = manager.getPlotId(player.getLocation());

                if (id == null) {
                    if (canBuild) {
                        player.sendMessage(api.C("ErrCannotBuild"));
                        event.setCancelled(true);
                    }
                } else {
                    boolean ptc = api.isPlotLocked(id);

                    if (ptc) {
                        player.sendMessage(api.C("PlotLocked"));
                        event.setCancelled(true);
                    } else {
                        Plot plot = manager.getPlotById(id);

                        if (plot == null || !plot.isAllowed(player.getUniqueId())) {
                            if (canBuild) {
                                player.sendMessage(api.C("ErrCannotBuild"));
                                event.setCancelled(true);
                            }
                        } else {
                            plot.resetExpire(manager.getMap(player).getDaysToExpiration());
                        }
                    }
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onPlayerInteractEntity(PlayerInteractEntityEvent event) {
        IPlayer player = plugin.wrapPlayer(event.getPlayer());
        Location clickLoc = event.getRightClicked().getLocation();
        ILocation location = new ILocation(player.getWorld(), clickLoc.getX(), clickLoc.getY(), clickLoc.getZ());
        if (manager.isPlotWorld(location)) {
            //Citizens Support
            if (event.getRightClicked().hasMetadata("NPC") && event.getRightClicked().getMetadata("NPC").get(0).asBoolean()) {
                return;
            }
            boolean cannotBuildAnywhere = !player.hasPermission(PermissionNames.ADMIN_BUILDANYWHERE);
            PlotId id = manager.getPlotId(location);

            if (id == null) {
                if (cannotBuildAnywhere) {
                    player.sendMessage(api.C("ErrCannotBuild"));
                    event.setCancelled(true);
                }
            } else {
                Plot plot = manager.getPlotById(id);

                if (plot == null) {
                    if (cannotBuildAnywhere) {
                        player.sendMessage(api.C("ErrCannotBuild"));
                        event.setCancelled(true);
                    }
                } else if (plot.isAllowed(player.getUniqueId())) {
                    plot.resetExpire(manager.getMap(location).getDaysToExpiration());
                } else {
                    player.sendMessage(api.C("ErrCannotBuild"));
                    event.setCancelled(true);
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onProjectileLaunchEvent(ProjectileLaunchEvent event) {
        if (event.getEntity() instanceof Player) {
            PlotMapInfo pmi = manager.getMap(event.getEntity().getWorld().getName());
            if (pmi != null && !pmi.canUseProjectiles()) {
                event.getEntity().sendMessage(api.C("ErrCannotUseEggs"));
                event.setCancelled(true);
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onCreatureSpawn(CreatureSpawnEvent event) {
        IEntity entity = plugin.wrapEntity(event.getEntity());

        if (manager.isPlotWorld(entity.getLocation())) {
            PlotId id = manager.getPlotId(entity.getLocation());
            if (id != null) {
                boolean plotLocked = api.isPlotLocked(id);

                if (plotLocked) {
                    event.setCancelled(true);
                }
            }
        }
    }

    @EventHandler
    public void onEntityDamagebyEntity(EntityDamageByEntityEvent event) {
        IEntity entity = plugin.wrapEntity(event.getDamager());
        if (manager.isPlotWorld(entity)) {
            if (!(entity instanceof IPlayer)) {
                event.setCancelled(true);
            } else {
                IPlayer player = (IPlayer) entity;
                boolean cantBuild = !player.hasPermission(PermissionNames.ADMIN_BUILDANYWHERE);
                PlotId id = manager.getPlotId(entity.getLocation());
                if (id == null) {
                    if (cantBuild) {
                        player.sendMessage(api.C("ErrCannotBuild"));
                        event.setCancelled(true);
                    }
                } else {
                    Plot plot = manager.getPlotById(id);
                    if (plot == null || !plot.isAllowed(player.getUniqueId())) {
                        if (cantBuild) {
                            player.sendMessage(api.C("ErrCannotBuild"));
                            event.setCancelled(true);
                        }
                    } else {
                        plot.resetExpire(manager.getMap(player).getDaysToExpiration());
                    }
                }

            }
        }
    }

    @EventHandler
    public void onArmorStand(PlayerArmorStandManipulateEvent event) {
        IPlayer player = plugin.wrapPlayer(event.getPlayer());
        ILocation location = new ILocation(player.getWorld(), BukkitConverter.locationToVector(event.getRightClicked().getLocation()));

        if (manager.isPlotWorld(location)) {
            boolean cannotBuildAnywhere = !player.hasPermission(PermissionNames.ADMIN_BUILDANYWHERE);
            PlotId id = manager.getPlotId(location);

            if (id == null) {
                if (cannotBuildAnywhere) {
                    player.sendMessage(api.C("ErrCannotBuild"));
                    event.setCancelled(true);
                }
            } else {
                Plot plot = manager.getPlotById(id);

                if (plot == null) {
                    if (cannotBuildAnywhere) {
                        player.sendMessage(api.C("ErrCannotBuild"));
                        event.setCancelled(true);
                    }
                } else if (plot.isAllowed(player.getUniqueId())) {
                    plot.resetExpire(manager.getMap(location).getDaysToExpiration());
                } else {
                    player.sendMessage(api.C("ErrCannotBuild"));
                    event.setCancelled(true);
                }
            }
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onSandCannon(EntityChangeBlockEvent event) {
        BukkitEntity entity = new BukkitEntity(event.getEntity());
        if (manager.isPlotWorld(entity) && event.getEntity().getType().equals(EntityType.FALLING_BLOCK)) {
            //todo finish this
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
            api.getLogger().info("Done loading " + event.getNbPlots() + " plots for world " + event
                    .getWorldName());
    }

    @EventHandler(ignoreCancelled = true)
    public void onPlayerQuit(PlayerQuitEvent event) {
        UUID playerUUID = event.getPlayer().getUniqueId();
        plugin.removePlayer(playerUUID);
    }

    @EventHandler(ignoreCancelled = true)
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player p = event.getPlayer();

        if (p != null) {
            manager.UpdatePlayerNameFromId(p.getUniqueId(), p.getName());
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onSignEdit(SignChangeEvent event) {
        IPlayer player = plugin.wrapPlayer(event.getPlayer());
        ILocation location = new ILocation(player.getWorld(), event.getBlock().getX(), event.getBlock().getY(), event.getBlock().getZ());

        if (manager.isPlotWorld(location)) {
            PlotId id = manager.getPlotId(location);

            if (id == null) {
                if (!player.hasPermission(PermissionNames.ADMIN_BUILDANYWHERE)) {
                    player.sendMessage(api.C("ErrCannotBuild"));
                    event.setCancelled(true);
                }
            } else {
                boolean ptc = api.isPlotLocked(id);

                if (ptc) {
                    player.sendMessage(api.C("PlotLocked"));
                    event.setCancelled(true);
                } else {
                    Plot plot = manager.getPlotById(id);

                    if (plot == null || !plot.isAllowed(player.getUniqueId())) {
                        if (!player.hasPermission(PermissionNames.ADMIN_BUILDANYWHERE)) {
                            player.sendMessage(api.C("ErrCannotBuild"));
                            event.setCancelled(true);
                        }
                    } else {
                        plot.resetExpire(manager.getMap(location).getDaysToExpiration());
                    }
                }
            }
        }
    }
}
