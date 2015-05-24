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
import org.bukkit.entity.Monster;
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
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;

import java.util.List;
import java.util.UUID;
import java.util.logging.Level;

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

        if (manager.isPlotWorld(location)) {
            if (player.hasPermission(PermissionNames.ADMIN_BUILDANYWHERE)) {
                return;
            }
            Plot plot = manager.getPlot(location);
            if (plot == null || !plot.isAllowed(player.getUniqueId())) {
                player.sendMessage(api.C("ErrCannotBuild"));
                event.setCancelled(true);
            } else if (api.isPlotLocked(plot)) {
                player.sendMessage(api.C("PlotLocked"));
                event.setCancelled(true);
            } else {
                plot.resetExpire(manager.getMap(location).getDaysToExpiration());
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onBlockPlace(BlockPlaceEvent event) {
        IPlayer player = plugin.wrapPlayer(event.getPlayer());
        Location bloc = event.getBlockPlaced().getLocation();
        ILocation location = new ILocation(player.getWorld(), bloc.getX(), bloc.getY(), bloc.getZ());

        if (manager.isPlotWorld(location)) {
            Plot plot = manager.getPlot(location);
            if (plot == null || !plot.isAllowed(player.getUniqueId())) {
                if (!player.hasPermission(PermissionNames.ADMIN_BUILDANYWHERE)) {
                    player.sendMessage(api.C("ErrCannotBuild"));
                    event.setCancelled(true);
                }
            } else if (api.isPlotLocked(plot)) {
                player.sendMessage(api.C("PlotLocked"));
                event.setCancelled(true);
            } else {
                plot.resetExpire(manager.getMap(location).getDaysToExpiration());
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onBucketEmptyEvent(PlayerBucketEmptyEvent event) {
        IPlayer player = plugin.wrapPlayer(event.getPlayer());
        ILocation location = new ILocation(player.getWorld(), BukkitConverter.locationToVector(event.getBlockClicked().getLocation()));

        if (!player.hasPermission(PermissionNames.ADMIN_BUILDANYWHERE) && manager.isPlotWorld(location)) {
            Plot plot =
                    manager.getPlot(location.add(event.getBlockFace().getModX(), event.getBlockFace().getModY(), event.getBlockFace().getModZ()));

            if (plot == null) {
                player.sendMessage(api.C("ErrCannotBuild"));
                event.setCancelled(true);
            } else if (!plot.isAllowed(player.getUniqueId())) {
                if (api.isPlotLocked(plot)) {
                    player.sendMessage(api.C("PlotLocked"));
                    event.setCancelled(true);
                } else {
                    player.sendMessage(api.C("ErrCannotBuild"));
                    event.setCancelled(true);
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
                Plot plot = manager.getPlot(location);

                if (plot == null) {
                    player.sendMessage(api.C("ErrCannotBuild"));
                    event.setCancelled(true);
                } else if (!plot.isAllowed(player.getUniqueId())) {
                    if (api.isPlotLocked(plot)) {
                        player.sendMessage(api.C("PlotLocked"));
                        event.setCancelled(true);
                    } else {
                        player.sendMessage(api.C("ErrCannotBuild"));
                        event.setCancelled(true);
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
            Plot plot = manager.getPlot(location);

            boolean canBuild = !player.hasPermission(PermissionNames.ADMIN_BUILDANYWHERE);
            PlotMapInfo pmi = manager.getMap(location);

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
                            player.sendMessage(api.C("ErrCannotUse"));
                            event.setCancelled(true);
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
        BukkitBlock block = new BukkitBlock(event.getBlock());
        PlotMapInfo pmi = manager.getMap(block.getLocation());

        if (pmi != null && pmi.isDisableExplosion()) {
            event.setCancelled(true);
        } else {
            PlotId id = manager.getPlotId(block.getLocation());
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
        IEntity entity = plugin.wrapEntity(event.getIgnitingEntity());
        BukkitBlock block = new BukkitBlock(event.getBlock());

        PlotMapInfo pmi = manager.getMap(block.getLocation());

        if (pmi != null) {
            if (pmi.isDisableIgnition()) {
                event.setCancelled(true);
            } else {
                Plot plot = manager.getPlot(block.getLocation());
                if (plot == null || !plot.isAllowed(entity.getUniqueId())) {
                    event.setCancelled(true);
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    public void onHangingPlace(HangingPlaceEvent event) {
        IPlayer player = plugin.wrapPlayer(event.getPlayer());
        ILocation location = new ILocation(player.getWorld(), BukkitConverter.locationToVector(event.getBlock().getLocation()));

        if (manager.isPlotWorld(location.getWorld())) {
            Plot plot = manager.getPlot(location);
            boolean canBuild = !player.hasPermission(PermissionNames.ADMIN_BUILDANYWHERE);

            if (plot == null || !plot.isAllowed(player.getUniqueId())) {
                if (canBuild) {
                    player.sendMessage(api.C("ErrCannotBuild"));
                    event.setCancelled(true);
                }
            } else {
                if (api.isPlotLocked(plot)) {
                    player.sendMessage(api.C("PlotLocked"));
                    event.setCancelled(true);
                }
                plot.resetExpire(manager.getMap(location).getDaysToExpiration());
            }
        }

    }

    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    public void onHangingBreakByEntity(HangingBreakByEntityEvent event) {

        if (event.getRemover() instanceof Player) {
            BukkitPlayer player = (BukkitPlayer) plugin.wrapPlayer((Player) event.getRemover());

            if (manager.isPlotWorld(player)) {
                boolean cannotBuildAnywhere = !player.hasPermission(PermissionNames.ADMIN_BUILDANYWHERE);

                Plot plot = manager.getPlot(player);

                if (plot == null) {
                    if (cannotBuildAnywhere) {
                        player.sendMessage(api.C("ErrCannotBuild"));
                        event.setCancelled(true);
                    }
                } else if (plot.isAllowed(player.getUniqueId())) {
                    plot.resetExpire(manager.getMap(player).getDaysToExpiration());
                } else {
                    player.sendMessage(api.C("ErrCannotBuild"));
                    event.setCancelled(true);
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

            Plot plot = manager.getPlot(location);

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

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onProjectileLaunchEvent(ProjectileLaunchEvent event) {
        if (event.getEntity() instanceof Player) {
            PlotMapInfo pmi = manager.getMap(new BukkitWorld(event.getEntity().getWorld()));
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
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onEntityDamage(EntityDamageEvent event) {
        IEntity entity = plugin.wrapEntity(event.getEntity());
        if (manager.isPlotWorld(entity)) {
            //Don't protect Monsters!
            if (event.getEntity() instanceof Monster) {
                return;
            }
            //This includes everything except for Monsters which were excluded above.
            if (event.getCause().equals(EntityDamageEvent.DamageCause.ENTITY_ATTACK)) {
                if (event instanceof EntityDamageByEntityEvent) {
                    EntityDamageByEntityEvent damageByEntityEvent = (EntityDamageByEntityEvent) event;
                    //Specific to Players to allow PVP. event.getEntity() is the damaged entity
                    if (event.getEntity() instanceof Player) {
                        //Don't allow PVP on the roads. Only allow pvp if both entities are in the plot.
                        PlotId id = manager.getPlotId(entity.getLocation());
                        IEntity damager = plugin.wrapEntity(damageByEntityEvent.getDamager());
                        PlotId id2 = manager.getPlotId(damager.getLocation());
                        if (id == null) {
                            event.setCancelled(true);
                        }
                        if (id2 == null) {
                            event.setCancelled(true);
                        }
                    } else {
                        PlotId id = manager.getPlotId(entity.getLocation());
                        if (id == null) {
                            event.setCancelled(true);
                        } else {
                            Plot plot = manager.getPlotById(id, entity.getWorld());
                            if (plot == null || !plot.isAllowed(damageByEntityEvent.getDamager().getUniqueId())) {
                                event.setCancelled(true);
                            }
                        }
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

            Plot plot = manager.getPlot(location);

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

    @EventHandler(ignoreCancelled = true)
    public void onSandCannon(EntityChangeBlockEvent event) {
        BukkitEntity entity = new BukkitEntity(event.getEntity());
        if (manager.isPlotWorld(entity) && event.getEntityType().equals(EntityType.FALLING_BLOCK)) {
            if (event.getTo().equals(Material.AIR)) {
                entity.setMetadata("plotFallBlock", new FixedMetadataValue(plugin, event.getBlock().getLocation()));
            } else {
                List<MetadataValue> values = entity.getMetadata("plotFallBlock");

                if (!values.isEmpty()) {

                    Location spawn = (Location) (values.get(0).value());
                    Location createdNew = event.getBlock().getLocation();

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
        ILocation location = new ILocation(player.getWorld(), event.getBlock().getX(), event.getBlock().getY(), event.getBlock().getZ());

        if (manager.isPlotWorld(location)) {
            Plot plot = manager.getPlot(location);
            if (plot == null || !plot.isAllowed(player.getUniqueId())) {
                if (!player.hasPermission(PermissionNames.ADMIN_BUILDANYWHERE)) {
                    player.sendMessage(api.C("ErrCannotBuild"));
                    event.setCancelled(true);
                }
            } else if (api.isPlotLocked(plot)) {
                player.sendMessage(api.C("PlotLocked"));
                event.setCancelled(true);
            } else {
                plot.resetExpire(manager.getMap(location).getDaysToExpiration());
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
