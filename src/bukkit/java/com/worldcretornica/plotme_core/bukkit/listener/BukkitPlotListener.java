package com.worldcretornica.plotme_core.bukkit.listener;

import com.worldcretornica.plotme_core.PermissionNames;
import com.worldcretornica.plotme_core.Plot;
import com.worldcretornica.plotme_core.PlotMapInfo;
import com.worldcretornica.plotme_core.PlotMeCoreManager;
import com.worldcretornica.plotme_core.PlotMe_Core;
import com.worldcretornica.plotme_core.PlotToClear;
import com.worldcretornica.plotme_core.bukkit.*;
import com.worldcretornica.plotme_core.bukkit.api.*;
import com.worldcretornica.plotme_core.bukkit.event.*;
import org.bukkit.block.*;
import org.bukkit.entity.*;
import org.bukkit.event.*;
import org.bukkit.event.block.*;
import org.bukkit.event.entity.*;
import org.bukkit.event.hanging.*;
import org.bukkit.event.player.*;
import org.bukkit.event.world.*;
import org.bukkit.inventory.*;

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

    @EventHandler(priority = EventPriority.HIGH)
    public void onBlockBreak(BlockBreakEvent event) {
        BukkitLocation location = new BukkitLocation(event.getBlock().getLocation());

        if (manager.isPlotWorld(location)) {
            Player player = event.getPlayer();
            boolean cannotBuild = !player.hasPermission(PermissionNames.ADMIN_BUILDANYWHERE);
            String id = manager.getPlotId(location);

            if (id.isEmpty()) {
                if (cannotBuild) {
                    player.sendMessage(api.getUtil().C("ErrCannotBuild"));
                    event.setCancelled(true);
                }
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

    @EventHandler(priority = EventPriority.HIGH)
    public void onBlockPlace(BlockPlaceEvent event) {
        Player player = event.getPlayer();
        BukkitLocation location = new BukkitLocation(event.getBlockPlaced().getLocation());

        if (manager.isPlotWorld(location)) {
            boolean canBuild = !player.hasPermission(PermissionNames.ADMIN_BUILDANYWHERE);
            String id = manager.getPlotId(location);

            if (id.isEmpty()) {
                if (canBuild) {
                    player.sendMessage(api.getUtil().C("ErrCannotBuild"));
                    event.setCancelled(true);
                }
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
    public void onPlayerBucketEmpty(PlayerBucketEmptyEvent event) {
        Player player = event.getPlayer();
        BukkitLocation location = new BukkitLocation(event.getBlockClicked().getLocation());

        if (!player.hasPermission(PermissionNames.ADMIN_BUILDANYWHERE)) {
            if (manager.isPlotWorld(location)) {
                String id = manager.getPlotId(location);

                if (id.isEmpty()) {
                    player.sendMessage(api.getUtil().C("ErrCannotBuild"));
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
                        Plot plot = manager.getPlotById(id, location.getWorld());

                        if (plot == null || !plot.isAllowed(player.getName(), player.getUniqueId())) {
                            player.sendMessage(api.getUtil().C("ErrCannotBuild"));
                            event.setCancelled(true);
                        }
                    }
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onPlayerBucketFill(PlayerBucketFillEvent event) {
        Player player = event.getPlayer();
        BukkitLocation location = new BukkitLocation(event.getBlockClicked().getLocation());
        if (!player.hasPermission(PermissionNames.ADMIN_BUILDANYWHERE)) {
            if (manager.isPlotWorld(location)) {
                String id = manager.getPlotId(location);

                if (id.isEmpty()) {
                    player.sendMessage(api.getUtil().C("ErrCannotBuild"));
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
                        Plot plot = manager.getPlotById(id, location.getWorld());

                        if (plot == null || !plot.isAllowed(player.getName(), player.getUniqueId())) {
                            player.sendMessage(api.getUtil().C("ErrCannotBuild"));
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

        BukkitBlock block = new BukkitBlock(event.getClickedBlock());
        if (manager.isPlotWorld(block.getWorld())) {
            Player player = event.getPlayer();

            PlotToClear ptc = api.getPlotLocked(block.getWorld().getName(), manager.getPlotId(block.getLocation()));

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

                String id = manager.getPlotId(block.getLocation());
                Plot plot = manager.getPlotById(id, pmi);
                if (event.isBlockInHand() && event.getAction() == Action.RIGHT_CLICK_BLOCK) {

                    if (id.isEmpty()) {
                        if (canBuild) {
                            player.sendMessage(api.getUtil().C("ErrCannotBuild"));
                            event.setCancelled(true);
                        }
                    } else {

                        if (plot == null || !plot.isAllowed(player.getName(), player.getUniqueId())) {
                            if (canBuild) {
                                player.sendMessage(api.getUtil().C("ErrCannotBuild"));
                                event.setCancelled(true);
                            }
                        } else {
                            plot.resetExpire(pmi.getDaysToExpiration());
                        }
                    }
                } else {
                    boolean blocked = false;
                    if (pmi.isProtectedBlock(block.getTypeId())) {
                        if (!player.hasPermission("plotme.unblock." + block.getTypeId())) {
                            blocked = true;
                        }
                    }

                    ItemStack item = event.getItem();

                    if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
                        if (item != null) {
                            int itemId = item.getType().getId();
                            byte itemData = item.getData().getData();

                            if (pmi.isPreventedItem(String.valueOf(itemId)) || pmi.isPreventedItem(itemId + ":" + itemData)) {
                                if (!player.hasPermission("plotme.unblock." + itemId)) {
                                    blocked = true;
                                }
                            }
                        }
                    }

                    if (blocked) {
                        if (id.isEmpty()) {
                            if (canBuild) {
                                if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
                                    player.sendMessage(api.getUtil().C("ErrCannotUse"));
                                }
                                event.setCancelled(true);
                            }
                        } else if ((plot == null || !plot.isAllowed(player.getName(), player.getUniqueId())) && canBuild) {
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

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onBlockSpread(BlockSpreadEvent event) {
        BukkitLocation location = new BukkitLocation(event.getBlock().getLocation());

        if (manager.isPlotWorld(location)) {
            String id = manager.getPlotId(location);

            if (id.isEmpty()) {
                event.setCancelled(true);
            } else {
                PlotToClear ptc = api.getPlotLocked(location.getWorld().getName(), id);

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
            String id = manager.getPlotId(location);

            if (id.isEmpty()) {
                event.setCancelled(true);
            } else {
                PlotToClear ptc = api.getPlotLocked(location.getWorld().getName(), id);

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
            String id = manager.getPlotId(location);

            if (id.isEmpty()) {
                event.setCancelled(true);
            } else {
                PlotToClear ptc = api.getPlotLocked(location.getWorld().getName(), id);

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
            String id = manager.getPlotId(location);

            if (id.isEmpty()) {
                event.setCancelled(true);
            } else {
                PlotToClear ptc = api.getPlotLocked(location.getWorld().getName(), id);

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
            String id = manager.getPlotId(location);
            if (id.isEmpty()) {
                event.setCancelled(true);
            } else {
                PlotToClear ptc = api.getPlotLocked(location.getWorld().getName(), id);

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
            String id = manager.getPlotId(location);

            if (id.isEmpty()) {
                event.setCancelled(true);
            } else {
                PlotToClear ptc = api.getPlotLocked(location.getWorld().getName(), id);

                if (ptc != null) {
                    event.setCancelled(true);
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onBlockPistonExtend(BlockPistonExtendEvent event) {
        BukkitLocation location = new BukkitLocation(event.getBlock().getLocation());
        if (manager.isPlotWorld(location)) {
            BlockFace face = event.getDirection();

            for (Block block : event.getBlocks()) {
                String id = manager.getPlotId(new BukkitLocation(block.getLocation().add(face.getModX(), face.getModY(), face.getModZ())));

                if (id.isEmpty()) {
                    event.setCancelled(true);
                } else {
                    PlotToClear ptc = api.getPlotLocked(location.getWorld().getName(), id);

                    if (ptc != null) {
                        event.setCancelled(true);
                    }
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onBlockPistonRetract(BlockPistonRetractEvent event) {
        @SuppressWarnings("deprecation")
        BukkitBlock block = new BukkitBlock(event.getRetractLocation().getBlock());

        if (manager.isPlotWorld(block.getWorld())) {
            String id = manager.getPlotId(block.getLocation());

            if (id.isEmpty()) {
                event.setCancelled(true);
            } else {
                PlotToClear ptc = api.getPlotLocked(block.getWorld().getName(), id);

                if (ptc != null) {
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
            String id = manager.getPlotId(new BukkitLocation(blocks.get(i).getLocation()));
            if (id.isEmpty()) {
                blocks.remove(i);
                i--;
            } else {
                PlotToClear ptc = api.getPlotLocked(location.getWorld().getName(), id);

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
            String id = manager.getPlotId(location);
            PlotToClear ptc = api.getPlotLocked(location.getWorld().getName(), id);

            if (ptc != null) {
                event.setCancelled(true);
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

        if (pmi == null) {
            return;
        }
        if (pmi.isDisableIgnition()) {
            event.setCancelled(true);
        } else {
            String id = manager.getPlotId(location);

            if (id.isEmpty()) {
                event.setCancelled(true);
            } else {
                PlotToClear ptc = api.getPlotLocked(location.getWorld().getName(), id);

                Player player = null;
                if (ptc != null) {
                    if (event.getPlayer() != null) {
                        player = event.getPlayer();
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
                    }
                    event.setCancelled(true);
                } else {
                    if (event.getPlayer() != null) {
                        player = event.getPlayer();
                    }
                    Plot plot = manager.getPlotById(id, pmi);

                    if (plot == null) {
                        event.setCancelled(true);
                    } else {
                        if (player != null && !plot.isAllowed(player.getName(), player.getUniqueId())) {
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
            String id = manager.getPlotId(location);
            boolean canBuild = !player.hasPermission(PermissionNames.ADMIN_BUILDANYWHERE);

            if (id.isEmpty()) {
                if (canBuild) {
                    player.sendMessage(api.getUtil().C("ErrCannotBuild"));
                    event.setCancelled(true);
                }
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
                String id = manager.getPlotId(player.getLocation());

                if (id.isEmpty()) {
                    if (canBuild) {
                        player.sendMessage(api.getUtil().C("ErrCannotBuild"));
                        event.setCancelled(true);
                    }
                } else {
                    PlotToClear ptc = api.getPlotLocked(player.getWorld().getName(), id);

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
            boolean canBuild = !player.hasPermission(PermissionNames.ADMIN_BUILDANYWHERE);
            String id = manager.getPlotId(location);

            if (id.isEmpty()) {
                if (canBuild) {
                    player.sendMessage(api.getUtil().C("ErrCannotBuild"));
                    event.setCancelled(true);
                }
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
    public void onProjectileLaunchEvent(ProjectileLaunchEvent event) {
        if (event.getEntity() instanceof Player) {
            PlotMapInfo pmi = manager.getMap(event.getEntity().getWorld().getName());
            if (pmi != null && !pmi.canUseProjectiles()) {
                event.getEntity().sendMessage(api.getUtil().C("ErrCannotUseEggs"));
                event.setCancelled(true);
            /* Player player = event.getPlayer();
            BukkitLocation location = new BukkitLocation(event.getEgg().getLocation());

            if (manager.isPlotWorld(location)) {
                boolean canBuild = player.hasPermission(PermissionNames.ADMIN_BUILDANYWHERE);
                String id = manager.getPlotId(location);

                if (id.isEmpty()) {
                    if (!canBuild) {
                        player.sendMessage(api.getUtil().C("ErrCannotUseEggs"));
                        event.setHatching(false);
                    }
                } else {
                    Plot plot = pmi.getPlot(id);

                    if (plot == null || !plot.isAllowed(player.getName(), player.getUniqueId())) {
                        if (!canBuild) {
                            player.sendMessage(api.getUtil().C("ErrCannotUseEggs"));
                            event.setHatching(false);
                        }
                    }
                }
            }
            */
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
            String id = manager.getPlotId(location);

            if (!id.isEmpty()) {
                PlotToClear plotLocked = api.getPlotLocked(location.getWorld().getName(), id);

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
                String id = manager.getPlotId(entity.getLocation());
                if (id.isEmpty()) {
                    if (cantBuild) {
                        player.sendMessage(api.getUtil().C("ErrCannotBuild"));
                        event.setCancelled(true);
                    }
                } else {
                    Plot plot = manager.getPlotById(id, bukkitPlayer);
                    if (plot == null) {
                        if (cantBuild) {
                            bukkitPlayer.sendMessage(api.getUtil().C("ErrCannotBuild"));
                            event.setCancelled(true);
                        }
                    } else if (!plot.isAllowed(player.getName(), player.getUniqueId())) {
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
        
        if(p != null) {
            PlotMeCoreManager.getInstance().UpdatePlayerNameFromId(p.getUniqueId(), p.getName());
        }
    }
}
