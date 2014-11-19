package com.worldcretornica.plotme_core.bukkit.listener;

import com.worldcretornica.plotme_core.*;
import com.worldcretornica.plotme_core.bukkit.PlotMe_CorePlugin;
import com.worldcretornica.plotme_core.bukkit.api.BukkitBlock;
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

        if (api.getPlotMeCoreManager().isPlotWorld(player)) {
            boolean cannotBuild = !player.hasPermission("plotme.admin.buildanywhere");
            String id = PlotMeCoreManager.getPlotId(player);

            if (id.isEmpty()) {
                if (cannotBuild) {
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
                    Plot plot = api.getPlotMeCoreManager().getMap(player).getPlot(id);

                    if (plot == null || !plot.isAllowed(player.getName(), player.getUniqueId())) {
                        if (cannotBuild) {
                            player.sendMessage(api.getUtil().C("ErrCannotBuild"));
                            event.setCancelled(true);
                        }
                    } else {
                        plot.resetExpire(api.getPlotMeCoreManager().getMap(player).getDaysToExpiration());
                    }
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onBlockPlace(BlockPlaceEvent event) {
        BukkitPlayer player = new BukkitPlayer(event.getPlayer());
        if (api.getPlotMeCoreManager().isPlotWorld(player)) {
            boolean canbuild = !player.hasPermission("plotme.admin.buildanywhere");
            String id = PlotMeCoreManager.getPlotId(player);

            if (id.isEmpty()) {
                if (canbuild) {
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
                    Plot plot = api.getPlotMeCoreManager().getPlotById(id, player);

                    if (plot == null || !plot.isAllowed(player.getName(), player.getUniqueId())) {
                        if (canbuild) {
                            player.sendMessage(api.getUtil().C("ErrCannotBuild"));
                            event.setCancelled(true);
                        }
                    } else {
                        plot.resetExpire(api.getPlotMeCoreManager().getMap(player).getDaysToExpiration());
                    }
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onPlayerBucketEmpty(PlayerBucketEmptyEvent event) {
        BukkitPlayer player = new BukkitPlayer(event.getPlayer());

        if (!player.hasPermission("plotme.admin.buildanywhere")) {
            if (api.getPlotMeCoreManager().isPlotWorld(player)) {
                String id = PlotMeCoreManager.getPlotId(player);

                if (id.isEmpty()) {
                    player.sendMessage(api.getUtil().C("ErrCannotBuild"));
                    event.setCancelled(true);
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
                        Plot plot = api.getPlotMeCoreManager().getPlotById(id, player);

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

        if (!player.hasPermission("plotme.admin.buildanywhere")) {
            if (api.getPlotMeCoreManager().isPlotWorld(player)) {
                String id = PlotMeCoreManager.getPlotId(player);

                if (id.isEmpty()) {
                    player.sendMessage(api.getUtil().C("ErrCannotBuild"));
                    event.setCancelled(true);
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
                        Plot plot = api.getPlotMeCoreManager().getPlotById(id, player);

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

        if (api.getPlotMeCoreManager().isPlotWorld(player)) {

            String id = PlotMeCoreManager.getPlotId(player);

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
                boolean canbuild = !player.hasPermission("plotme.admin.buildanywhere");
                PlotMapInfo pmi = api.getPlotMeCoreManager().getMap(player);

                if (event.isBlockInHand() && event.getAction() == Action.RIGHT_CLICK_BLOCK) {

                    id = PlotMeCoreManager.getPlotId(player);

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
                    BukkitBlock block = new BukkitBlock(event.getClickedBlock());
                    if (pmi.isProtectedBlock(block.getTypeId())) {
                        if (!player.hasPermission("plotme.unblock." + block.getTypeId())) {
                            blocked = true;
                        }
                    }

                    ItemStack item = event.getItem();

                    if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
                        int itemid = item.getType().getId();
                        byte itemdata = item.getData().getData();

                        if (pmi.isPreventedItem("" + itemid) || pmi.isPreventedItem(itemid + ":" + itemdata)) {
                            if (!player.hasPermission("plotme.unblock." + itemid)) {
                                blocked = true;
                            }
                        }
                    }

                    if (blocked) {
                        id = PlotMeCoreManager.getPlotId(player);

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

        if (api.getPlotMeCoreManager().isPlotWorld(player)) {
            String id = PlotMeCoreManager.getPlotId(player);

            if (id.isEmpty()) {
                event.setCancelled(true);
            } else {
                PlotToClear ptc = api.getPlotLocked(player.getWorld().getName(), id);

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
        BukkitBlock block = new BukkitBlock(event.getRetractLocation().getBlock());

        if (api.getPlotMeCoreManager().isPlotWorld(block) && block.getType() == Material.PISTON_STICKY_BASE) {
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
    public void onStructureGrow(StructureGrowEvent event) {
        BukkitPlayer player = new BukkitPlayer(event.getPlayer());
        List<BlockState> blocks = event.getBlocks();
        boolean found = false;

        for (int i = 0; i < blocks.size(); i++) {
            if (found || api.getPlotMeCoreManager().isPlotWorld(player)) {
                found = true;
                String id = PlotMeCoreManager.getPlotId(player);

                if (id.isEmpty()) {
                    event.getBlocks().remove(i);
                    i--;
                } else {
                    PlotToClear ptc = api.getPlotLocked(player.getWorld().getName(), id);

                    if (ptc != null) {
                        event.setCancelled(true);
                    }
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
        BukkitPlayer player = new BukkitPlayer(event.getPlayer());

        PlotMapInfo pmi = api.getPlotMeCoreManager().getMap(player);

        if (pmi != null) {
            if (pmi.isDisableIgnition()) {
                event.setCancelled(true);
            } else {
                String id = PlotMeCoreManager.getPlotId(player.getLocation());

                if (id.isEmpty()) {
                    event.setCancelled(true);
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
                        Plot plot = PlotMeCoreManager.getPlotById(id, pmi);

                        if (plot == null || !plot.isAllowed(player.getUniqueId())) {
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

        if (api.getPlotMeCoreManager().isPlotWorld(player)) {
            String id = PlotMeCoreManager.getPlotId(player);
            boolean canbuild = !player.hasPermission("plotme.admin.buildanywhere");

            if (id.isEmpty()) {
                if (canbuild) {
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
                    Plot plot = api.getPlotMeCoreManager().getPlotById(id, player);

                    if (plot == null || !plot.isAllowed(player.getName(), player.getUniqueId())) {
                        if (canbuild) {
                            player.sendMessage(api.getUtil().C("ErrCannotBuild"));
                            event.setCancelled(true);
                        }
                    } else {
                        plot.resetExpire(api.getPlotMeCoreManager().getMap(player).getDaysToExpiration());
                    }
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onHangingBreakByEntity(HangingBreakByEntityEvent event) {
        Entity entity = event.getRemover();

        if (entity instanceof Player) {
            BukkitPlayer player = new BukkitPlayer((Player) entity);

            boolean canbuild = !player.hasPermission("plotme.admin.buildanywhere");

            if (api.getPlotMeCoreManager().isPlotWorld(player)) {
                String id = PlotMeCoreManager.getPlotId(player);

                if (id.isEmpty()) {
                    if (canbuild) {
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
                        Plot plot = api.getPlotMeCoreManager().getPlotById(id, player);

                        if (plot == null || !plot.isAllowed(player.getUniqueId())) {
                            if (canbuild) {
                                player.sendMessage(api.getUtil().C("ErrCannotBuild"));
                                event.setCancelled(true);
                            }
                        } else {
                            plot.resetExpire(api.getPlotMeCoreManager().getMap(player).getDaysToExpiration());
                        }
                    }
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onPlayerInteractEntity(PlayerInteractEntityEvent event) {
        BukkitPlayer player = new BukkitPlayer(event.getPlayer());

        if (api.getPlotMeCoreManager().isPlotWorld(player)) {
            boolean canbuild = !player.hasPermission("plotme.admin.buildanywhere");
            String id = PlotMeCoreManager.getPlotId(player);

            if (id.isEmpty()) {
                if (canbuild) {
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
                    Plot plot = api.getPlotMeCoreManager().getPlotById(id, player);

                    if (plot == null || !plot.isAllowed(player.getName(), player.getUniqueId())) {
                        if (canbuild) {
                            player.sendMessage(api.getUtil().C("ErrCannotBuild"));
                            event.setCancelled(true);
                        }
                    } else {
                        plot.resetExpire(api.getPlotMeCoreManager().getMap(player).getDaysToExpiration());
                    }
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onPlayerEggThrow(PlayerEggThrowEvent event) {
        BukkitPlayer player = new BukkitPlayer(event.getPlayer());

        if (api.getPlotMeCoreManager().isPlotWorld(player)) {
            boolean canbuild = player.hasPermission("plotme.admin.buildanywhere");
            String id = PlotMeCoreManager.getPlotId(player);

            if (id.isEmpty()) {
                if (!canbuild) {
                    player.sendMessage(api.getUtil().C("ErrCannotUseEggs"));
                    event.setHatching(false);
                }
            } else {
                Plot plot = api.getPlotMeCoreManager().getPlotById(id, player);

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
