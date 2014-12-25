package com.worldcretornica.plotme_core.bukkit.listener;

import com.worldcretornica.plotme_core.*;
import com.worldcretornica.plotme_core.bukkit.PlotMe_CorePlugin;
import com.worldcretornica.plotme_core.bukkit.api.BukkitBlock;
import com.worldcretornica.plotme_core.bukkit.api.BukkitEntity;
import com.worldcretornica.plotme_core.bukkit.api.BukkitLocation;
import com.worldcretornica.plotme_core.bukkit.api.BukkitPlayer;
import com.worldcretornica.plotme_core.bukkit.event.PlotWorldLoadEvent;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.*;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.hanging.HangingBreakByEntityEvent;
import org.bukkit.event.hanging.HangingPlaceEvent;
import org.bukkit.event.player.*;
import org.bukkit.event.world.StructureGrowEvent;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class BukkitPlotListener implements Listener {

    private final PlotMe_CorePlugin plugin;
    private final PlotMe_Core api;

    public BukkitPlotListener(PlotMe_CorePlugin instance) {
        plugin = instance;
        api = plugin.getAPI();
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onBlockBreak(BlockBreakEvent event) {
        BukkitPlayer player = new BukkitPlayer(event.getPlayer());
        BukkitBlock block = new BukkitBlock(event.getBlock());

        if (api.getPlotMeCoreManager().isPlotWorld(block.getLocation())) {
            boolean cannotBuild = !player.hasPermission(PermissionNames.ADMIN_BUILDANYWHERE);
            String id = PlotMeCoreManager.getPlotId(block.getLocation());

            if (id.isEmpty()) {
                if (cannotBuild) {
                    player.sendMessage(api.getUtil().C("ErrCannotBuild"));
                    event.setCancelled(true);
                }
            } else {
                PlotToClear ptc = api.getPlotLocked(block.getWorld().getName(), id);

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
                    Plot plot = api.getPlotMeCoreManager().getMap(block.getLocation()).getPlot(id);

                    if (plot == null || !plot.isAllowed(player.getName(), player.getUniqueId())) {
                        if (cannotBuild) {
                            player.sendMessage(api.getUtil().C("ErrCannotBuild"));
                            event.setCancelled(true);
                        }
                    } else {
                        plot.resetExpire(api.getPlotMeCoreManager().getMap(block.getLocation()).getDaysToExpiration());
                    }
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onBlockPlace(BlockPlaceEvent event) {
        BukkitPlayer player = new BukkitPlayer(event.getPlayer());
        BukkitBlock block = new BukkitBlock(event.getBlock());

        if (api.getPlotMeCoreManager().isPlotWorld(block.getLocation())) {
            boolean canbuild = !player.hasPermission(PermissionNames.ADMIN_BUILDANYWHERE);
            String id = PlotMeCoreManager.getPlotId(block.getLocation());

            if (id.isEmpty()) {
                if (canbuild) {
                    player.sendMessage(api.getUtil().C("ErrCannotBuild"));
                    event.setCancelled(true);
                }
            } else {
                PlotToClear ptc = api.getPlotLocked(block.getWorld().getName(), id);

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
                    Plot plot = api.getPlotMeCoreManager().getPlotById(id, block.getWorld());

                    if (plot == null || !plot.isAllowed(player.getName(), player.getUniqueId())) {
                        if (canbuild) {
                            player.sendMessage(api.getUtil().C("ErrCannotBuild"));
                            event.setCancelled(true);
                        }
                    } else {
                        plot.resetExpire(api.getPlotMeCoreManager().getMap(block.getLocation()).getDaysToExpiration());
                    }
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onPlayerBucketEmpty(PlayerBucketEmptyEvent event) {
        BukkitPlayer player = new BukkitPlayer(event.getPlayer());
        BukkitBlock block = new BukkitBlock(event.getBlockClicked());

        if (!player.hasPermission(PermissionNames.ADMIN_BUILDANYWHERE)) {
            if (api.getPlotMeCoreManager().isPlotWorld(block.getLocation())) {
                String id = PlotMeCoreManager.getPlotId(block.getLocation());

                if (id.isEmpty()) {
                    player.sendMessage(api.getUtil().C("ErrCannotBuild"));
                    event.setCancelled(true);
                } else {
                    PlotToClear ptc = api.getPlotLocked(block.getWorld().getName(), id);

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
                        Plot plot = api.getPlotMeCoreManager().getPlotById(id, block.getWorld());

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
        BukkitPlayer player = new BukkitPlayer(event.getPlayer());
        BukkitBlock block = new BukkitBlock(event.getBlockClicked());

        if (!player.hasPermission(PermissionNames.ADMIN_BUILDANYWHERE)) {
            if (api.getPlotMeCoreManager().isPlotWorld(block.getLocation())) {
                String id = PlotMeCoreManager.getPlotId(block.getLocation());

                if (id.isEmpty()) {
                    player.sendMessage(api.getUtil().C("ErrCannotBuild"));
                    event.setCancelled(true);
                } else {
                    PlotToClear ptc = api.getPlotLocked(block.getWorld().getName(), id);

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
                        Plot plot = api.getPlotMeCoreManager().getPlotById(id, block.getWorld());

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
        BukkitPlayer player = new BukkitPlayer(event.getPlayer());
        BukkitBlock block = new BukkitBlock(event.getClickedBlock());

        if (api.getPlotMeCoreManager().isPlotWorld(block.getLocation())) {

            String id = PlotMeCoreManager.getPlotId(block.getLocation());

            PlotToClear ptc = api.getPlotLocked(block.getWorld().getName(), id);

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
                boolean canbuild = !player.hasPermission(PermissionNames.ADMIN_BUILDANYWHERE);
                PlotMapInfo pmi = api.getPlotMeCoreManager().getMap(block.getLocation());

                if (event.isBlockInHand() && event.getAction() == Action.RIGHT_CLICK_BLOCK) {

                    id = PlotMeCoreManager.getPlotId(block.getLocation());

                    if (id.isEmpty()) {
                        if (canbuild) {
                            player.sendMessage(api.getUtil().C("ErrCannotBuild"));
                            event.setCancelled(true);
                        }
                    } else {
                        Plot plot = PlotMeCoreManager.getPlotById(id, pmi);

                        if (plot == null || !plot.isAllowed(player.getName(), player.getUniqueId())) {
                            if (canbuild) {
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
                            int itemid = item.getType().getId();
                            byte itemdata = item.getData().getData();

                            if (pmi.isPreventedItem(String.valueOf(itemid)) || pmi.isPreventedItem(itemid + ":" + itemdata)) {
                                if (!player.hasPermission("plotme.unblock." + itemid)) {
                                    blocked = true;
                                }
                            }
                        }
                    }

                    if (blocked) {
                        id = PlotMeCoreManager.getPlotId(block.getLocation());

                        if (id.isEmpty()) {
                            if (canbuild) {
                                if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
                                    player.sendMessage(api.getUtil().C("ErrCannotUse"));
                                }
                                event.setCancelled(true);
                            }
                        } else {
                            Plot plot = PlotMeCoreManager.getPlotById(id, pmi);

                            if ((plot == null || !plot.isAllowed(player.getName(), player.getUniqueId())) && canbuild) {
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
        BukkitBlock block = new BukkitBlock(event.getBlock());

        if (api.getPlotMeCoreManager().isPlotWorld(block)) {
            String id = PlotMeCoreManager.getPlotId(block.getLocation());

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
    public void onBlockForm(BlockFormEvent event) {
        BukkitBlock block = new BukkitBlock(event.getBlock());

        if (api.getPlotMeCoreManager().isPlotWorld(block)) {
            String id = PlotMeCoreManager.getPlotId(block.getLocation());

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
    public void onBlockDamage(BlockDamageEvent event) {
        BukkitPlayer player = new BukkitPlayer(event.getPlayer());
        BukkitBlock block = new BukkitBlock(event.getBlock());

        if (api.getPlotMeCoreManager().isPlotWorld(block.getLocation())) {
            String id = PlotMeCoreManager.getPlotId(block.getLocation());

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
    public void onBlockFade(BlockFadeEvent event) {
        BukkitBlock block = new BukkitBlock(event.getBlock());

        if (api.getPlotMeCoreManager().isPlotWorld(block)) {
            String id = PlotMeCoreManager.getPlotId(block.getLocation());

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
    public void onBlockFromTo(BlockFromToEvent event) {
        BukkitBlock block = new BukkitBlock(event.getBlock());

        if (api.getPlotMeCoreManager().isPlotWorld(block)) {
            String id = PlotMeCoreManager.getPlotId(block.getLocation());

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
    public void onBlockGrow(BlockGrowEvent event) {
        BukkitBlock block = new BukkitBlock(event.getBlock());

        if (api.getPlotMeCoreManager().isPlotWorld(block)) {
            String id = PlotMeCoreManager.getPlotId(block.getLocation());

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
    public void onBlockPistonExtend(BlockPistonExtendEvent event) {
        if (api.getPlotMeCoreManager().isPlotWorld(new BukkitBlock(event.getBlock()))) {
            BlockFace face = event.getDirection();

            for (Block block : event.getBlocks()) {
                String id = PlotMeCoreManager.getPlotId(new BukkitLocation(block.getLocation().add(face.getModX(), face.getModY(), face.getModZ())));

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
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onBlockPistonRetract(BlockPistonRetractEvent event) {
        BukkitBlock piston = new BukkitBlock(event.getBlock());
        BukkitBlock block = new BukkitBlock(event.getRetractLocation().getBlock());

        if (api.getPlotMeCoreManager().isPlotWorld(piston) && piston.getType().equals(Material.PISTON_STICKY_BASE)) {
            String id = PlotMeCoreManager.getPlotId(block.getLocation());

            if (id.isEmpty()) {
                event.setCancelled(true);
            } else {
                PlotToClear ptc = api.getPlotLocked(piston.getWorld().getName(), id);

                if (ptc != null) {
                    event.setCancelled(true);
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onStructureGrow(StructureGrowEvent event) {
        BukkitLocation location = new BukkitLocation(event.getLocation());
        List<BlockState> blocks = event.getBlocks();

        if (!api.getPlotMeCoreManager().isPlotWorld(location)) {
          return;
        }

        for (int i = 0; i < blocks.size(); i++) {
            String id = PlotMeCoreManager.getPlotId(new BukkitLocation(blocks.get(i).getLocation()));
            if (id.isEmpty()) {
                blocks.remove(i);
                i--;
            } else {
                PlotToClear ptc = api.getPlotLocked(blocks.get(i).getWorld().getName(), id);

                if (ptc != null) {
                    event.setCancelled(true);
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onEntityExplode(EntityExplodeEvent event) {
        BukkitLocation location = new BukkitLocation(event.getLocation());

        PlotMapInfo pmi = api.getPlotMeCoreManager().getMap(location);

        if (pmi != null && pmi.isDisableExplosion()) {
            event.setCancelled(true);
        } else {
            String id = PlotMeCoreManager.getPlotId(location);
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
        BukkitEntity entity = new BukkitEntity(event.getIgnitingEntity());

        PlotMapInfo pmi = api.getPlotMeCoreManager().getMap(entity.getLocation());

        if (pmi == null) {
            return;
        }
        if (pmi.isDisableIgnition()) {
            event.setCancelled(true);
        } else {
            String id = PlotMeCoreManager.getPlotId(entity.getLocation());

            if (id.isEmpty()) {
                event.setCancelled(true);
            } else {
                PlotToClear ptc = api.getPlotLocked(entity.getLocation().getWorld().getName(), id);

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
                    Plot plot = PlotMeCoreManager.getPlotById(id, pmi);

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
        BukkitPlayer player = new BukkitPlayer(event.getPlayer());
        BukkitBlock block = new BukkitBlock(event.getBlock());

        if (api.getPlotMeCoreManager().isPlotWorld(block.getLocation())) {
            String id = PlotMeCoreManager.getPlotId(block.getLocation());
            boolean canbuild = !player.hasPermission(PermissionNames.ADMIN_BUILDANYWHERE);

            if (id.isEmpty()) {
                if (canbuild) {
                    player.sendMessage(api.getUtil().C("ErrCannotBuild"));
                    event.setCancelled(true);
                }
            } else {
                PlotToClear ptc = api.getPlotLocked(block.getWorld().getName(), id);

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
                    Plot plot = api.getPlotMeCoreManager().getPlotById(id, block.getLocation().getWorld());

                    if (plot == null || !plot.isAllowed(player.getName(), player.getUniqueId())) {
                        if (canbuild) {
                            player.sendMessage(api.getUtil().C("ErrCannotBuild"));
                            event.setCancelled(true);
                        }
                    } else {
                        plot.resetExpire(api.getPlotMeCoreManager().getMap(block.getLocation()).getDaysToExpiration());
                    }
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onHangingBreakByEntity(HangingBreakByEntityEvent event) {
        Entity entity = event.getRemover();
        BukkitLocation location = new BukkitLocation(event.getEntity().getLocation());

        if (entity instanceof Player) {
            BukkitPlayer player = new BukkitPlayer((Player) entity);

            boolean canbuild = !player.hasPermission(PermissionNames.ADMIN_BUILDANYWHERE);

            if (api.getPlotMeCoreManager().isPlotWorld(location)) {
                String id = PlotMeCoreManager.getPlotId(location);

                if (id.isEmpty()) {
                    if (canbuild) {
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
                        Plot plot = api.getPlotMeCoreManager().getPlotById(id, location.getWorld());

                        if (plot == null || !plot.isAllowed(player.getName(), player.getUniqueId())) {
                            if (canbuild) {
                                player.sendMessage(api.getUtil().C("ErrCannotBuild"));
                                event.setCancelled(true);
                            }
                        } else {
                            plot.resetExpire(api.getPlotMeCoreManager().getMap(location).getDaysToExpiration());
                        }
                    }
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onPlayerInteractEntity(PlayerInteractEntityEvent event) {
        BukkitPlayer player = new BukkitPlayer(event.getPlayer());
        BukkitLocation location = new BukkitLocation(event.getRightClicked().getLocation());

        if (api.getPlotMeCoreManager().isPlotWorld(location)) {
            boolean canbuild = !player.hasPermission(PermissionNames.ADMIN_BUILDANYWHERE);
            String id = PlotMeCoreManager.getPlotId(location);

            if (id.isEmpty()) {
                if (canbuild) {
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
                    Plot plot = api.getPlotMeCoreManager().getPlotById(id, location.getWorld());

                    if (plot == null || !plot.isAllowed(player.getName(), player.getUniqueId())) {
                        if (canbuild) {
                            player.sendMessage(api.getUtil().C("ErrCannotBuild"));
                            event.setCancelled(true);
                        }
                    } else {
                        plot.resetExpire(api.getPlotMeCoreManager().getMap(location).getDaysToExpiration());
                    }
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onPlayerEggThrow(PlayerEggThrowEvent event) {
        BukkitPlayer player = new BukkitPlayer(event.getPlayer());
        BukkitLocation location = new BukkitLocation(event.getEgg().getLocation());

        if (api.getPlotMeCoreManager().isPlotWorld(location)) {
            boolean canbuild = player.hasPermission(PermissionNames.ADMIN_BUILDANYWHERE);
            String id = PlotMeCoreManager.getPlotId(location);

            if (id.isEmpty()) {
                if (!canbuild) {
                    player.sendMessage(api.getUtil().C("ErrCannotUseEggs"));
                    event.setHatching(false);
                }
            } else {
                Plot plot = api.getPlotMeCoreManager().getPlotById(id, location.getWorld());

                if (plot == null || !plot.isAllowed(player.getName(), player.getUniqueId())) {
                    if (!canbuild) {
                        player.sendMessage(api.getUtil().C("ErrCannotUseEggs"));
                        event.setHatching(false);
                    }
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onCreatureSpawn(CreatureSpawnEvent event) {
        BukkitLocation location = new BukkitLocation(event.getLocation());

        if (api.getPlotMeCoreManager().isPlotWorld(location)) {
            String id = PlotMeCoreManager.getPlotId(location);

            if (!id.isEmpty()) {
                PlotToClear ptc = api.getPlotLocked(location.getWorld().getName(), id);

                if (ptc != null) {
                    event.setCancelled(true);
                }
            }
        }
    }

    @EventHandler
    public void onPlotWorldLoad(PlotWorldLoadEvent event) {
        plugin.getLogger().info("Done loading " + event.getNbPlots() + " plots for world " + event.getWorldName());
    }
}
