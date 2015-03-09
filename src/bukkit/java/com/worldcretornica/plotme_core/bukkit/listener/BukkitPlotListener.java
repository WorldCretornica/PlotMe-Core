package com.worldcretornica.plotme_core.bukkit.listener;

import com.worldcretornica.plotme_core.PermissionNames;
import com.worldcretornica.plotme_core.Plot;
import com.worldcretornica.plotme_core.PlotId;
import com.worldcretornica.plotme_core.PlotMapInfo;
import com.worldcretornica.plotme_core.PlotMeCoreManager;
import com.worldcretornica.plotme_core.PlotMe_Core;
import com.worldcretornica.plotme_core.PlotToClear;
import com.worldcretornica.plotme_core.bukkit.PlotMe_CorePlugin;
import com.worldcretornica.plotme_core.bukkit.api.BukkitBlock;
import com.worldcretornica.plotme_core.bukkit.api.BukkitEntity;
import com.worldcretornica.plotme_core.bukkit.api.BukkitLocation;
import com.worldcretornica.plotme_core.bukkit.api.BukkitPlayer;
import com.worldcretornica.plotme_core.bukkit.event.PlotWorldLoadEvent;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockDamageEvent;
import org.bukkit.event.block.BlockFadeEvent;
import org.bukkit.event.block.BlockFormEvent;
import org.bukkit.event.block.BlockFromToEvent;
import org.bukkit.event.block.BlockGrowEvent;
import org.bukkit.event.block.BlockIgniteEvent;
import org.bukkit.event.block.BlockPistonExtendEvent;
import org.bukkit.event.block.BlockPistonRetractEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.block.BlockSpreadEvent;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.event.hanging.HangingBreakByEntityEvent;
import org.bukkit.event.hanging.HangingPlaceEvent;
import org.bukkit.event.player.PlayerArmorStandManipulateEvent;
import org.bukkit.event.player.PlayerBucketEmptyEvent;
import org.bukkit.event.player.PlayerBucketEvent;
import org.bukkit.event.player.PlayerBucketFillEvent;
import org.bukkit.event.player.PlayerEggThrowEvent;
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
        BukkitLocation location = new BukkitLocation(event.getBlock().getLocation());

        if (manager.isPlotWorld(location)) {
            Player player = event.getPlayer();
            PlotId id = manager.getPlotId(location);

            if (id == null) {
                if (!player.hasPermission(PermissionNames.ADMIN_BUILDANYWHERE)) {
                    player.sendMessage(api.getUtil().C("ErrCannotBuild"));
                    event.setCancelled(true);
                }
            } else {
                PlotToClear ptc = api.getPlotLocked(location.getWorld(), id);

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
                        if (!player.hasPermission(PermissionNames.ADMIN_BUILDANYWHERE)) {
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

    @EventHandler(priority = EventPriority.HIGH)
    public void onBlockPlace(BlockPlaceEvent event) {
        Player player = event.getPlayer();
        BukkitLocation location = new BukkitLocation(event.getBlockPlaced().getLocation());

        if (manager.isPlotWorld(location)) {
            boolean canBuild = !player.hasPermission(PermissionNames.ADMIN_BUILDANYWHERE);
            PlotId id = manager.getPlotId(location);

            if (id == null) {
                if (canBuild) {
                    player.sendMessage(api.getUtil().C("ErrCannotBuild"));
                    event.setCancelled(true);
                }
            } else {
                PlotToClear ptc = api.getPlotLocked(location.getWorld(), id);

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
                    Plot plot = manager.getPlotById(id, location.getWorld());

                    if (plot == null || !plot.isAllowed(player.getName(), player.getUniqueId())) {
                        if (canBuild) {
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

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onBucketEmptyEvent(PlayerBucketEmptyEvent event) {
        Player player = event.getPlayer();
        BukkitLocation location = new BukkitLocation(event.getBlockClicked().getLocation());

        if (!player.hasPermission(PermissionNames.ADMIN_BUILDANYWHERE) && manager.isPlotWorld(location)) {
            PlotId id =
                    manager.getPlotId(location.add(event.getBlockFace().getModX(), event.getBlockFace().getModY(), event.getBlockFace().getModZ()));
            if (id == null) {
                player.sendMessage(api.getUtil().C("ErrCannotBuild"));
                event.setCancelled(true);
            } else {
                PlotToClear ptc = api.getPlotLocked(location.getWorld(), id);

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
                    Plot plot = manager.getPlotById(id, location.getWorld());

                    if (plot == null || !plot.isAllowed(player.getName(), player.getUniqueId())) {
                        player.sendMessage(api.getUtil().C("ErrCannotBuild"));
                        event.setCancelled(true);
                    }
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onBucketFillEvent(PlayerBucketFillEvent event) {
        handleBucketEvent(event);
    }

    private void handleBucketEvent(PlayerBucketEvent event) {
        Player player = event.getPlayer();
        BukkitLocation location = new BukkitLocation(event.getBlockClicked().getLocation());

        if (!player.hasPermission(PermissionNames.ADMIN_BUILDANYWHERE) && manager.isPlotWorld(location)) {
            PlotId id = manager.getPlotId(location);

            if (id == null) {
                player.sendMessage(api.getUtil().C("ErrCannotBuild"));
                event.setCancelled(true);
            } else {
                PlotToClear ptc = api.getPlotLocked(location.getWorld(), id);

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
                    Plot plot = manager.getPlotById(id, location.getWorld());

                    if (plot == null || !plot.isAllowed(player.getName(), player.getUniqueId())) {
                        player.sendMessage(api.getUtil().C("ErrCannotBuild"));
                        event.setCancelled(true);
                    }
                }
            }
        }
    }

    @SuppressWarnings("deprecation")
    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onPlayerInteract(PlayerInteractEvent event) {

        BukkitBlock block = new BukkitBlock(event.getClickedBlock());
        if (manager.isPlotWorld(block.getWorld())) {
            Player player = event.getPlayer();

            PlotId plotId = manager.getPlotId(block.getLocation());
            PlotToClear ptc = api.getPlotLocked(block.getWorld(), plotId);

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
                boolean canBuild = !player.hasPermission(PermissionNames.ADMIN_BUILDANYWHERE);
                PlotMapInfo pmi = manager.getMap(block.getWorld());

                Plot plot = manager.getPlotById(plotId, pmi);
                if (event.isBlockInHand() && event.getAction() == Action.RIGHT_CLICK_BLOCK) {
                    if (plot == null || !plot.isAllowed(player.getName(), player.getUniqueId())) {
                        if (canBuild) {
                            player.sendMessage(api.getUtil().C("ErrCannotBuild"));
                            event.setCancelled(true);
                        }
                    } else {
                        plot.resetExpire(pmi.getDaysToExpiration());
                    }
                } else {
                    boolean blocked = false;
                    if (pmi.isProtectedBlock(block.getTypeId()) && !player.hasPermission("plotme.unblock." + block.getTypeId())) {
                        blocked = true;
                    }

                    ItemStack item = event.getItem();

                    if (event.getAction() == Action.RIGHT_CLICK_BLOCK && item != null) {
                        int itemId = item.getType().getId();
                        byte itemData = item.getData().getData();

                        if ((pmi.isPreventedItem(String.valueOf(itemId)) || pmi.isPreventedItem(itemId + ":" + itemData)) && !player
                                .hasPermission("plotme.unblock." + itemId)) {
                            blocked = true;
                        }
                    }

                    if (blocked) {
                        if (plot == null || !plot.isAllowed(player.getName(), player.getUniqueId())) {
                            if (canBuild) {
                                if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
                                    player.sendMessage(api.getUtil().C("ErrCannotUse"));
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
        BukkitLocation location = new BukkitLocation(event.getBlock().getLocation());

        if (manager.isPlotWorld(location)) {
            PlotId id = manager.getPlotId(location);

            if (id == null) {
                event.setCancelled(true);
            } else {
                PlotToClear ptc = api.getPlotLocked(location.getWorld(), id);

                if (ptc != null) {
                    event.setCancelled(true);
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onBlockForm(BlockFormEvent event) {
        BukkitLocation location = new BukkitLocation(event.getBlock().getLocation());

        if (manager.isPlotWorld(location)) {
            PlotId id = manager.getPlotId(location);

            if (id == null) {
                event.setCancelled(true);
            } else {
                PlotToClear ptc = api.getPlotLocked(location.getWorld(), id);

                if (ptc != null) {
                    event.setCancelled(true);
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onBlockDamage(BlockDamageEvent event) {
        BukkitLocation location = new BukkitLocation(event.getBlock().getLocation());

        if (manager.isPlotWorld(location)) {
            PlotId id = manager.getPlotId(location);

            if (id == null) {
                event.setCancelled(true);
            } else {
                PlotToClear ptc = api.getPlotLocked(location.getWorld(), id);

                if (ptc != null) {
                    event.setCancelled(true);
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onBlockFade(BlockFadeEvent event) {
        BukkitLocation location = new BukkitLocation(event.getBlock().getLocation());

        if (manager.isPlotWorld(location)) {
            PlotId id = manager.getPlotId(location);

            if (id == null) {
                event.setCancelled(true);
            } else {
                PlotToClear ptc = api.getPlotLocked(location.getWorld(), id);

                if (ptc != null) {
                    event.setCancelled(true);
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onBlockFromTo(BlockFromToEvent event) {
        BukkitLocation location = new BukkitLocation(event.getToBlock().getLocation());
        if (manager.isPlotWorld(location)) {
            PlotId id = manager.getPlotId(location);
            if (id == null) {
                event.setCancelled(true);
            } else {
                PlotToClear ptc = api.getPlotLocked(location.getWorld(), id);

                if (ptc != null) {
                    event.setCancelled(true);
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onBlockGrow(BlockGrowEvent event) {
        BukkitLocation location = new BukkitLocation(event.getBlock().getLocation());

        if (manager.isPlotWorld(location)) {
            PlotId id = manager.getPlotId(location);

            if (id == null) {
                event.setCancelled(true);
            } else {
                PlotToClear ptc = api.getPlotLocked(location.getWorld(), id);

                if (ptc != null) {
                    event.setCancelled(true);
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    public void onBlockPistonExtend(BlockPistonExtendEvent event) {
        BukkitLocation location = new BukkitLocation(event.getBlock().getLocation());
        if (manager.isPlotWorld(location)) {
            BlockFace face = event.getDirection();

            for (Block block : event.getBlocks()) {
                PlotId id = manager.getPlotId(new BukkitLocation(block.getLocation().add(face.getModX(), face.getModY(), face.getModZ())));

                if (id == null) {
                    event.setCancelled(true);
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    public void onBlockPistonRetract(BlockPistonRetractEvent event) {
        BukkitLocation location = new BukkitLocation(event.getBlock().getLocation());
        if (manager.isPlotWorld(location)) {
            List<Block> blocks = event.getBlocks();
            for (Block moved : blocks) {
                PlotId id = manager.getPlotId(new BukkitLocation(moved.getLocation()));
                if (id == null) {
                    event.setCancelled(true);
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onStructureGrow(StructureGrowEvent event) {
        List<BlockState> blocks = event.getBlocks();

        BukkitLocation location = new BukkitLocation(event.getLocation());
        if (!manager.isPlotWorld(location)) {
            return;
        }

        for (int i = 0; i < blocks.size(); i++) {
            PlotId id = manager.getPlotId(new BukkitLocation(blocks.get(i).getLocation()));
            if (id == null) {
                blocks.remove(i);
                i--;
            } else {
                PlotToClear ptc = api.getPlotLocked(location.getWorld(), id);

                if (ptc != null) {
                    event.setCancelled(true);
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onEntityExplode(EntityExplodeEvent event) {
        BukkitLocation location = new BukkitLocation(event.getLocation());

        PlotMapInfo pmi = manager.getMap(location);

        if (pmi != null && pmi.isDisableExplosion()) {
            event.setCancelled(true);
        } else {
            PlotId id = manager.getPlotId(location);
            if (id == null) {
                event.setCancelled(true);
            } else {
                PlotToClear ptc = api.getPlotLocked(location.getWorld(), id);

                if (ptc != null) {
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
        BukkitLocation location = new BukkitLocation(event.getBlock().getLocation());

        PlotMapInfo pmi = manager.getMap(location);

        if (pmi != null) {
            if (pmi.isDisableIgnition()) {
                event.setCancelled(true);
            } else {
                PlotId id = manager.getPlotId(location);

                if (id == null) {
                    event.setCancelled(true);
                } else {
                    PlotToClear ptc = api.getPlotLocked(location.getWorld(), id);

                    if (ptc != null) {
                        event.setCancelled(true);
                    } else {
                        Player player = null;
                        if (event.getPlayer() != null) {
                            player = event.getPlayer();
                        }
                        Plot plot = manager.getPlotById(id, pmi);

                        if (plot == null || player != null && !plot.isAllowed(player.getName(), player.getUniqueId())) {
                            event.setCancelled(true);
                        }
                    }
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onHangingPlace(HangingPlaceEvent event) {
        Player player = event.getPlayer();
        BukkitLocation location = new BukkitLocation(event.getBlock().getLocation());

        if (manager.isPlotWorld(location)) {
            PlotId id = manager.getPlotId(location);
            boolean canBuild = !player.hasPermission(PermissionNames.ADMIN_BUILDANYWHERE);

            if (id == null) {
                if (canBuild) {
                    player.sendMessage(api.getUtil().C("ErrCannotBuild"));
                    event.setCancelled(true);
                }
            } else {
                PlotToClear ptc = api.getPlotLocked(location.getWorld(), id);

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
                    Plot plot = manager.getPlotById(id, location.getWorld());

                    if (plot == null || !plot.isAllowed(player.getName(), player.getUniqueId())) {
                        if (canBuild) {
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

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onHangingBreakByEntity(HangingBreakByEntityEvent event) {

        if (event.getRemover() instanceof Player) {
            BukkitPlayer player = (BukkitPlayer) plugin.wrapPlayer((Player) event.getRemover());

            boolean canBuild = !player.hasPermission(PermissionNames.ADMIN_BUILDANYWHERE);

            if (manager.isPlotWorld(player.getWorld())) {
                PlotId id = manager.getPlotId(player.getLocation());

                if (id == null) {
                    if (canBuild) {
                        player.sendMessage(api.getUtil().C("ErrCannotBuild"));
                        event.setCancelled(true);
                    }
                } else {
                    PlotToClear ptc = api.getPlotLocked(player.getWorld(), id);

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
                        Plot plot = manager.getPlotById(id, player.getWorld());

                        if (plot == null || !plot.isAllowed(player.getName(), player.getUniqueId())) {
                            if (canBuild) {
                                player.sendMessage(api.getUtil().C("ErrCannotBuild"));
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
        Player player = event.getPlayer();
        BukkitLocation location = new BukkitLocation(event.getRightClicked().getLocation());
        if (manager.isPlotWorld(location)) {
            //Citizens Support
            if (event.getRightClicked().hasMetadata("NPC") && event.getRightClicked().getMetadata("NPC").get(0).asBoolean()) {
                return;
            }
            boolean cannotBuildAnywhere = !player.hasPermission(PermissionNames.ADMIN_BUILDANYWHERE);
            PlotId id = manager.getPlotId(location);

            if (id == null) {
                if (cannotBuildAnywhere) {
                    player.sendMessage(api.getUtil().C("ErrCannotBuild"));
                    event.setCancelled(true);
                }
            } else {
                Plot plot = manager.getPlotById(id, location.getWorld());

                if (plot == null) {
                    if (cannotBuildAnywhere) {
                        player.sendMessage(api.getUtil().C("ErrCannotBuild"));
                        event.setCancelled(true);
                    }
                } else if (plot.isAllowed(player.getName(), player.getUniqueId())) {
                    plot.resetExpire(manager.getMap(location).getDaysToExpiration());
                } else {
                    player.sendMessage(api.getUtil().C("ErrCannotBuild"));
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
                event.getEntity().sendMessage(api.getUtil().C("ErrCannotUseEggs"));
                event.setCancelled(true);
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onBowEvent(EntityShootBowEvent event) {
        if (event.getEntity() instanceof Player) {
            PlotMapInfo pmi = manager.getMap(event.getEntity().getWorld().getName());
            if (pmi != null && !pmi.canUseProjectiles()) {
                event.getEntity().sendMessage(api.getUtil().C("ErrCannotUseEggs"));
                event.setCancelled(true);
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onEggEvent(PlayerEggThrowEvent event) {
        PlotMapInfo pmi = manager.getMap(event.getEgg().getWorld().getName());
        if (pmi != null && !pmi.canUseProjectiles()) {
            event.getPlayer().sendMessage(api.getUtil().C("ErrCannotUseEggs"));
            event.setHatching(false);
        }
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onCreatureSpawn(CreatureSpawnEvent event) {
        BukkitLocation location = new BukkitLocation(event.getLocation());

        if (manager.isPlotWorld(location)) {
            PlotId id = manager.getPlotId(location);
            if (id != null) {
                PlotToClear plotLocked = api.getPlotLocked(location.getWorld(), id);

                if (plotLocked != null) {
                    event.setCancelled(true);
                }
            }
        }
    }

    @EventHandler
    public void onEntityDamagebyEntity(EntityDamageByEntityEvent event) {
        BukkitEntity entity = new BukkitEntity(event.getDamager());
        if (manager.isPlotWorld(entity)) {
            if (!(event.getDamager() instanceof Player)) {
                event.setCancelled(true);
            } else {
                Player player = (Player) event.getDamager();
                BukkitPlayer bukkitPlayer = (BukkitPlayer) plugin.wrapPlayer(player);
                boolean cantBuild = !player.hasPermission(PermissionNames.ADMIN_BUILDANYWHERE);
                PlotId id = manager.getPlotId(entity.getLocation());
                if (id == null) {
                    if (cantBuild) {
                        player.sendMessage(api.getUtil().C("ErrCannotBuild"));
                        event.setCancelled(true);
                    }
                } else {
                    Plot plot = manager.getPlotById(id, bukkitPlayer);
                    if (plot == null || !plot.isAllowed(player.getName(), player.getUniqueId())) {
                        if (cantBuild) {
                            bukkitPlayer.sendMessage(api.getUtil().C("ErrCannotBuild"));
                            event.setCancelled(true);
                        }
                    } else {
                        plot.resetExpire(manager.getMap(bukkitPlayer).getDaysToExpiration());
                    }
                }

            }
        }
    }

    @EventHandler
    public void onArmorStand(PlayerArmorStandManipulateEvent event) {
        Player player = event.getPlayer();
        BukkitLocation location = new BukkitLocation(event.getRightClicked().getLocation());

        if (manager.isPlotWorld(location)) {
            boolean cannotBuildAnywhere = !player.hasPermission(PermissionNames.ADMIN_BUILDANYWHERE);
            PlotId id = manager.getPlotId(location);

            if (id == null) {
                if (cannotBuildAnywhere) {
                    player.sendMessage(api.getUtil().C("ErrCannotBuild"));
                    event.setCancelled(true);
                }
            } else {
                Plot plot = manager.getPlotById(id, location.getWorld());

                if (plot == null) {
                    if (cannotBuildAnywhere) {
                        player.sendMessage(api.getUtil().C("ErrCannotBuild"));
                        event.setCancelled(true);
                    }
                } else if (plot.isAllowed(player.getName(), player.getUniqueId())) {
                    plot.resetExpire(manager.getMap(location).getDaysToExpiration());
                } else {
                    player.sendMessage(api.getUtil().C("ErrCannotBuild"));
                    event.setCancelled(true);
                }
            }
        }
    }

    @EventHandler
    public void onPlotWorldLoad(PlotWorldLoadEvent event) {
        api.getLogger().info("Done loading " + event.getNbPlots() + " plots for world " + event.getWorldName());
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
}
