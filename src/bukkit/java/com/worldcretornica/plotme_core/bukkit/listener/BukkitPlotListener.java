package com.worldcretornica.plotme_core.bukkit.listener;

import com.worldcretornica.plotme_core.Plot;
import com.worldcretornica.plotme_core.PlotMapInfo;
import com.worldcretornica.plotme_core.PlotMe_Core;
import com.worldcretornica.plotme_core.PlotToClear;
import com.worldcretornica.plotme_core.bukkit.PlotMe_CorePlugin;
import com.worldcretornica.plotme_core.bukkit.api.BukkitBlock;
import com.worldcretornica.plotme_core.bukkit.api.BukkitBlockState;
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
    private PlotMe_Core api;

    public BukkitPlotListener(PlotMe_CorePlugin instance) {
        plugin = instance;
        api = plugin.getAPI();
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onBlockBreak(BlockBreakEvent event) {
        BukkitBlock b = new BukkitBlock(event.getBlock());

        if (api.getPlotMeCoreManager().isPlotWorld(b)) {
            BukkitPlayer p = new BukkitPlayer(event.getPlayer());
            boolean canbuild = PlotMe_Core.cPerms(p, "plotme.admin.buildanywhere");
            String id = api.getPlotMeCoreManager().getPlotId(b.getLocation());

            if (id.isEmpty()) {
                if (!canbuild) {
                    p.sendMessage(api.getUtil().C("ErrCannotBuild"));
                    event.setCancelled(true);
                }
            } else {
                PlotToClear ptc = api.getPlotLocked(p.getWorld().getName(), id);

                if (ptc != null) {
                    switch (ptc.getReason()) {
                    case Clear:
                        p.sendMessage(api.getUtil().C("MsgPlotLockedClear"));
                        break;
                    case Reset:
                        p.sendMessage(api.getUtil().C("MsgPlotLockedReset"));
                        break;
                    case Expired:
                        p.sendMessage(api.getUtil().C("MsgPlotLockedExpired"));
                        break;
                    }
                    event.setCancelled(true);
                } else {
                    Plot plot = api.getPlotMeCoreManager().getMap(p).getPlot(id);

                    if (plot == null || !plot.isAllowed(p.getUniqueId())) {
                        if (!canbuild) {
                            p.sendMessage(api.getUtil().C("ErrCannotBuild"));
                            event.setCancelled(true);
                        }
                    } else {
                        plot.resetExpire(api.getPlotMeCoreManager().getMap(b).getDaysToExpiration());
                    }
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onBlockPlace(BlockPlaceEvent event) {
        BukkitBlock block = new BukkitBlock(event.getBlock());

        if (api.getPlotMeCoreManager().isPlotWorld(block)) {
            BukkitPlayer p = new BukkitPlayer(event.getPlayer());
            boolean canbuild = PlotMe_Core.cPerms(p, "plotme.admin.buildanywhere");
            String id = api.getPlotMeCoreManager().getPlotId(block.getLocation());

            if (id.isEmpty()) {
                if (!canbuild) {
                    p.sendMessage(api.getUtil().C("ErrCannotBuild"));
                    event.setCancelled(true);
                }
            } else {
                PlotToClear ptc = api.getPlotLocked(p.getWorld().getName(), id);

                if (ptc != null) {
                    switch (ptc.getReason()) {
                    case Clear:
                        p.sendMessage(api.getUtil().C("MsgPlotLockedClear"));
                        break;
                    case Reset:
                        p.sendMessage(api.getUtil().C("MsgPlotLockedReset"));
                        break;
                    case Expired:
                        p.sendMessage(api.getUtil().C("MsgPlotLockedExpired"));
                        break;
                    }
                    event.setCancelled(true);
                } else {
                    Plot plot = api.getPlotMeCoreManager().getPlotById(p, id);

                    if (plot == null || !plot.isAllowed(p.getUniqueId())) {
                        if (!canbuild) {
                            p.sendMessage(api.getUtil().C("ErrCannotBuild"));
                            event.setCancelled(true);
                        }
                    } else {
                        plot.resetExpire(api.getPlotMeCoreManager().getMap(block).getDaysToExpiration());
                    }
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onPlayerBucketEmpty(PlayerBucketEmptyEvent event) {
        BukkitPlayer p = new BukkitPlayer(event.getPlayer());

        if (!PlotMe_Core.cPerms(p, "plotme.admin.buildanywhere")) {
            BlockFace bf = event.getBlockFace();
            BukkitBlock block = new BukkitBlock(event.getBlockClicked().getLocation().add(bf.getModX(), bf.getModY(), bf.getModZ()).getBlock());
            if (api.getPlotMeCoreManager().isPlotWorld(block)) {
                String id = api.getPlotMeCoreManager().getPlotId(block.getLocation());

                if (id.isEmpty()) {
                    p.sendMessage(api.getUtil().C("ErrCannotBuild"));
                    event.setCancelled(true);
                } else {
                    PlotToClear ptc = api.getPlotLocked(p.getWorld().getName(), id);

                    if (ptc != null) {
                        switch (ptc.getReason()) {
                        case Clear:
                            p.sendMessage(api.getUtil().C("MsgPlotLockedClear"));
                            break;
                        case Reset:
                            p.sendMessage(api.getUtil().C("MsgPlotLockedReset"));
                            break;
                        case Expired:
                            p.sendMessage(api.getUtil().C("MsgPlotLockedExpired"));
                            break;
                        }
                        event.setCancelled(true);
                    } else {
                        Plot plot = api.getPlotMeCoreManager().getPlotById(p, id);

                        if (plot == null || !plot.isAllowed(p.getUniqueId())) {
                            p.sendMessage(api.getUtil().C("ErrCannotBuild"));
                            event.setCancelled(true);
                        }
                    }
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onPlayerBucketFill(PlayerBucketFillEvent event) {
        BukkitPlayer p = new BukkitPlayer(event.getPlayer());

        if (!PlotMe_Core.cPerms(p, "plotme.admin.buildanywhere")) {
            BukkitBlock block = new BukkitBlock(event.getBlockClicked());
            if (api.getPlotMeCoreManager().isPlotWorld(block)) {
                String id = api.getPlotMeCoreManager().getPlotId(block.getLocation());

                if (id.isEmpty()) {
                    p.sendMessage(api.getUtil().C("ErrCannotBuild"));
                    event.setCancelled(true);
                } else {
                    PlotToClear ptc = api.getPlotLocked(p.getWorld().getName(), id);

                    if (ptc != null) {
                        switch (ptc.getReason()) {
                        case Clear:
                            p.sendMessage(api.getUtil().C("MsgPlotLockedClear"));
                            break;
                        case Reset:
                            p.sendMessage(api.getUtil().C("MsgPlotLockedReset"));
                            break;
                        case Expired:
                            p.sendMessage(api.getUtil().C("MsgPlotLockedExpired"));
                            break;
                        }
                        event.setCancelled(true);
                    } else {
                        Plot plot = api.getPlotMeCoreManager().getPlotById(p, id);

                        if (plot == null || !plot.isAllowed(p.getUniqueId())) {
                            p.sendMessage(api.getUtil().C("ErrCannotBuild"));
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
        BukkitBlock b = new BukkitBlock(event.getClickedBlock());

        if (api.getPlotMeCoreManager().isPlotWorld(b)) {
            BukkitPlayer p = new BukkitPlayer(event.getPlayer());

            String id = api.getPlotMeCoreManager().getPlotId(b.getLocation());

            PlotToClear ptc = api.getPlotLocked(p.getWorld().getName(), id);

            if (ptc != null) {
                switch (ptc.getReason()) {
                case Clear:
                    p.sendMessage(api.getUtil().C("MsgPlotLockedClear"));
                    break;
                case Reset:
                    p.sendMessage(api.getUtil().C("MsgPlotLockedReset"));
                    break;
                case Expired:
                    p.sendMessage(api.getUtil().C("MsgPlotLockedExpired"));
                    break;
                }
                event.setCancelled(true);
            } else {
                boolean canbuild = PlotMe_Core.cPerms(p, "plotme.admin.buildanywhere");
                PlotMapInfo pmi = api.getPlotMeCoreManager().getMap(b);

                if (event.isBlockInHand() && event.getAction() == Action.RIGHT_CLICK_BLOCK) {
                    BlockFace face = event.getBlockFace();
                    BukkitBlock builtblock = (BukkitBlock) b.getWorld().getBlockAt(b.getX() + face.getModX(), b.getY() + face.getModY(), b.getZ() + face.getModZ());

                    id = api.getPlotMeCoreManager().getPlotId(builtblock.getLocation());

                    if (id.isEmpty()) {
                        if (!canbuild) {
                            p.sendMessage(api.getUtil().C("ErrCannotBuild"));
                            event.setCancelled(true);
                        }
                    } else {
                        Plot plot = api.getPlotMeCoreManager().getPlotById(p, id);

                        if (plot == null || !plot.isAllowed(p.getName())) {
                            if (!canbuild) {
                                p.sendMessage(api.getUtil().C("ErrCannotBuild"));
                                event.setCancelled(true);
                            }
                        } else {
                            plot.resetExpire(api.getPlotMeCoreManager().getMap(b).getDaysToExpiration());
                        }
                    }
                } else {
                    boolean blocked = false;
                    if (pmi.isProtectedBlock(b.getTypeId())) {
                        if (!PlotMe_Core.cPerms(p, "plotme.unblock." + b.getTypeId())) {
                            blocked = true;
                        }
                    }

                    ItemStack is = event.getItem();

                    if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
                        int itemid = is.getType().getId();
                        byte itemdata = is.getData().getData();

                        if (pmi.isPreventedItem("" + itemid) || pmi.isPreventedItem("" + itemid + ":" + itemdata)) {
                            if (!PlotMe_Core.cPerms(p, "plotme.unblock." + itemid)) {
                                blocked = true;
                            }
                        }
                    }

                    if (blocked) {
                        id = api.getPlotMeCoreManager().getPlotId(b.getLocation());

                        if (id.isEmpty()) {
                            if (!canbuild) {
                                if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
                                    p.sendMessage(api.getUtil().C("ErrCannotUse"));
                                }
                                event.setCancelled(true);
                            }
                        } else {
                            Plot plot = api.getPlotMeCoreManager().getPlotById(p, id);

                            if ((plot == null || !plot.isAllowed(p.getName())) && !canbuild) {
                                if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
                                    p.sendMessage(api.getUtil().C("ErrCannotUse"));
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
            String id = api.getPlotMeCoreManager().getPlotId(block.getLocation());

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
        BukkitBlock b = new BukkitBlock(event.getBlock());

        if (api.getPlotMeCoreManager().isPlotWorld(b)) {
            String id = api.getPlotMeCoreManager().getPlotId(b.getLocation());

            if (id.isEmpty()) {
                event.setCancelled(true);
            } else {
                PlotToClear ptc = api.getPlotLocked(b.getWorld().getName(), id);

                if (ptc != null) {
                    event.setCancelled(true);
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onBlockDamage(BlockDamageEvent event) {
        BukkitBlock b = new BukkitBlock(event.getBlock());

        if (api.getPlotMeCoreManager().isPlotWorld(b)) {
            String id = api.getPlotMeCoreManager().getPlotId(b.getLocation());

            if (id.isEmpty()) {
                event.setCancelled(true);
            } else {
                PlotToClear ptc = api.getPlotLocked(b.getWorld().getName(), id);

                if (ptc != null) {
                    event.setCancelled(true);
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onBlockFade(BlockFadeEvent event) {
        BukkitBlock b = new BukkitBlock(event.getBlock());

        if (api.getPlotMeCoreManager().isPlotWorld(b)) {
            String id = api.getPlotMeCoreManager().getPlotId(b.getLocation());

            if (id.isEmpty()) {
                event.setCancelled(true);
            } else {
                PlotToClear ptc = api.getPlotLocked(b.getWorld().getName(), id);

                if (ptc != null) {
                    event.setCancelled(true);
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onBlockFromTo(BlockFromToEvent event) {
        BukkitBlock b = new BukkitBlock(event.getBlock());

        if (api.getPlotMeCoreManager().isPlotWorld(b)) {
            String id = api.getPlotMeCoreManager().getPlotId(b.getLocation());

            if (id.isEmpty()) {
                event.setCancelled(true);
            } else {
                PlotToClear ptc = api.getPlotLocked(b.getWorld().getName(), id);

                if (ptc != null) {
                    event.setCancelled(true);
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onBlockGrow(BlockGrowEvent event) {
        BukkitBlock b = new BukkitBlock(event.getBlock());

        if (api.getPlotMeCoreManager().isPlotWorld(b)) {
            String id = api.getPlotMeCoreManager().getPlotId(b.getLocation());

            if (id.isEmpty()) {
                event.setCancelled(true);
            } else {
                PlotToClear ptc = api.getPlotLocked(b.getWorld().getName(), id);

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

            for (Block b : event.getBlocks()) {
                String id = api.getPlotMeCoreManager().getPlotId(new BukkitLocation(b.getLocation().add(face.getModX(), face.getModY(), face.getModZ())));

                if (id.isEmpty()) {
                    event.setCancelled(true);
                } else {
                    PlotToClear ptc = api.getPlotLocked(b.getWorld().getName(), id);

                    if (ptc != null) {
                        event.setCancelled(true);
                    }
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onBlockPistonRetract(BlockPistonRetractEvent event) {
        BukkitBlock b = new BukkitBlock(event.getRetractLocation().getBlock());

        if (api.getPlotMeCoreManager().isPlotWorld(b) && event.getBlock().getType() == Material.PISTON_STICKY_BASE) {
            String id = api.getPlotMeCoreManager().getPlotId(b.getLocation());

            if (id.isEmpty()) {
                event.setCancelled(true);
            } else {
                PlotToClear ptc = api.getPlotLocked(b.getWorld().getName(), id);

                if (ptc != null) {
                    event.setCancelled(true);
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onStructureGrow(StructureGrowEvent event) {
        List<BlockState> blocks = event.getBlocks();
        boolean found = false;

        for (int i = 0; i < blocks.size(); i++) {
            if (found || api.getPlotMeCoreManager().isPlotWorld(new BukkitBlockState(blocks.get(i)))) {
                found = true;
                String id = api.getPlotMeCoreManager().getPlotId(new BukkitLocation(blocks.get(i).getLocation()));

                if (id.isEmpty()) {
                    event.getBlocks().remove(i);
                    i--;
                } else {
                    PlotToClear ptc = api.getPlotLocked(blocks.get(i).getWorld().getName(), id);

                    if (ptc != null) {
                        event.setCancelled(true);
                    }
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onEntityExplode(EntityExplodeEvent event) {
        BukkitLocation l = new BukkitLocation(event.getLocation());

        PlotMapInfo pmi = api.getPlotMeCoreManager().getMap(l);

        if (pmi != null && pmi.isDisableExplosion()) {
            event.setCancelled(true);
        } else {
            String id = api.getPlotMeCoreManager().getPlotId(l);
            PlotToClear ptc = api.getPlotLocked(l.getWorld().getName(), id);

            if (ptc != null) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onBlockIgnite(BlockIgniteEvent event) {
        BukkitBlock block = new BukkitBlock(event.getBlock());

        PlotMapInfo pmi = api.getPlotMeCoreManager().getMap(block);

        if (pmi != null) {
            if (pmi.isDisableIgnition()) {
                event.setCancelled(true);
            } else {
                String id = api.getPlotMeCoreManager().getPlotId(block.getLocation());
                Player player = event.getPlayer();

                if (id.isEmpty()) {
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
                        Plot plot = api.getPlotMeCoreManager().getPlotById(block, id);

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
        BukkitBlock b = new BukkitBlock(event.getBlock());

        if (api.getPlotMeCoreManager().isPlotWorld(b)) {
            String id = api.getPlotMeCoreManager().getPlotId(b.getLocation());
            BukkitPlayer p = new BukkitPlayer(event.getPlayer());
            boolean canbuild = PlotMe_Core.cPerms(p, "plotme.admin.buildanywhere");

            if (id.isEmpty()) {
                if (!canbuild) {
                    p.sendMessage(api.getUtil().C("ErrCannotBuild"));
                    event.setCancelled(true);
                }
            } else {
                PlotToClear ptc = api.getPlotLocked(b.getWorld().getName(), id);

                if (ptc != null) {
                    switch (ptc.getReason()) {
                    case Clear:
                        p.sendMessage(api.getUtil().C("MsgPlotLockedClear"));
                        break;
                    case Reset:
                        p.sendMessage(api.getUtil().C("MsgPlotLockedReset"));
                        break;
                    case Expired:
                        p.sendMessage(api.getUtil().C("MsgPlotLockedExpired"));
                        break;
                    }
                    event.setCancelled(true);
                } else {
                    Plot plot = api.getPlotMeCoreManager().getPlotById(p, id);

                    if (plot == null || !plot.isAllowed(p.getUniqueId())) {
                        if (!canbuild) {
                            p.sendMessage(api.getUtil().C("ErrCannotBuild"));
                            event.setCancelled(true);
                        }
                    } else {
                        plot.resetExpire(api.getPlotMeCoreManager().getMap(b).getDaysToExpiration());
                    }
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onHangingBreakByEntity(HangingBreakByEntityEvent event) {
        Entity entity = event.getRemover();

        if (entity instanceof Player) {
            BukkitPlayer p = new BukkitPlayer((Player) entity);

            boolean canbuild = PlotMe_Core.cPerms(p, "plotme.admin.buildanywhere");

            BukkitLocation l = new BukkitLocation(event.getEntity().getLocation());

            if (api.getPlotMeCoreManager().isPlotWorld(l)) {
                String id = api.getPlotMeCoreManager().getPlotId(l);

                if (id.isEmpty()) {
                    if (!canbuild) {
                        p.sendMessage(api.getUtil().C("ErrCannotBuild"));
                        event.setCancelled(true);
                    }
                } else {
                    PlotToClear ptc = api.getPlotLocked(l.getWorld().getName(), id);

                    if (ptc != null) {
                        switch (ptc.getReason()) {
                        case Clear:
                            p.sendMessage(api.getUtil().C("MsgPlotLockedClear"));
                            break;
                        case Reset:
                            p.sendMessage(api.getUtil().C("MsgPlotLockedReset"));
                            break;
                        case Expired:
                            p.sendMessage(api.getUtil().C("MsgPlotLockedExpired"));
                            break;
                        }
                        event.setCancelled(true);
                    } else {
                        Plot plot = api.getPlotMeCoreManager().getPlotById(p, id);

                        if (plot == null || !plot.isAllowed(p.getUniqueId())) {
                            if (!canbuild) {
                                p.sendMessage(api.getUtil().C("ErrCannotBuild"));
                                event.setCancelled(true);
                            }
                        } else {
                            plot.resetExpire(api.getPlotMeCoreManager().getMap(l).getDaysToExpiration());
                        }
                    }
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onPlayerInteractEntity(PlayerInteractEntityEvent event) {
        BukkitLocation l = new BukkitLocation(event.getRightClicked().getLocation());

        if (api.getPlotMeCoreManager().isPlotWorld(l)) {
            BukkitPlayer p = new BukkitPlayer(event.getPlayer());
            boolean canbuild = PlotMe_Core.cPerms(p, "plotme.admin.buildanywhere");
            String id = api.getPlotMeCoreManager().getPlotId(l);

            if (id.isEmpty()) {
                if (!canbuild) {
                    p.sendMessage(api.getUtil().C("ErrCannotBuild"));
                    event.setCancelled(true);
                }
            } else {
                PlotToClear ptc = api.getPlotLocked(l.getWorld().getName(), id);

                if (ptc != null) {
                    switch (ptc.getReason()) {
                    case Clear:
                        p.sendMessage(api.getUtil().C("MsgPlotLockedClear"));
                        break;
                    case Reset:
                        p.sendMessage(api.getUtil().C("MsgPlotLockedReset"));
                        break;
                    case Expired:
                        p.sendMessage(api.getUtil().C("MsgPlotLockedExpired"));
                        break;
                    }
                    event.setCancelled(true);
                } else {
                    Plot plot = api.getPlotMeCoreManager().getPlotById(p, id);

                    if (plot == null || !plot.isAllowed(p.getUniqueId())) {
                        if (!canbuild) {
                            p.sendMessage(api.getUtil().C("ErrCannotBuild"));
                            event.setCancelled(true);
                        }
                    } else {
                        plot.resetExpire(api.getPlotMeCoreManager().getMap(l).getDaysToExpiration());
                    }
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onPlayerEggThrow(PlayerEggThrowEvent event) {
        BukkitLocation l = new BukkitLocation(event.getEgg().getLocation());

        if (api.getPlotMeCoreManager().isPlotWorld(l)) {
            BukkitPlayer p = new BukkitPlayer(event.getPlayer());
            boolean canbuild = PlotMe_Core.cPerms(p, "plotme.admin.buildanywhere");
            String id = api.getPlotMeCoreManager().getPlotId(l);

            if (id.isEmpty()) {
                if (!canbuild) {
                    p.sendMessage(api.getUtil().C("ErrCannotUseEggs"));
                    event.setHatching(false);
                }
            } else {
                Plot plot = api.getPlotMeCoreManager().getPlotById(p, id);

                if (plot == null || !plot.isAllowed(p.getUniqueId())) {
                    if (!canbuild) {
                        p.sendMessage(api.getUtil().C("ErrCannotUseEggs"));
                        event.setHatching(false);
                    }
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onCreatureSpawn(CreatureSpawnEvent event) {
        BukkitLocation l = new BukkitLocation(event.getLocation());

        if (api.getPlotMeCoreManager().isPlotWorld(l)) {
            String id = api.getPlotMeCoreManager().getPlotId(l);

            if (!id.isEmpty()) {
                PlotToClear ptc = api.getPlotLocked(l.getWorld().getName(), id);

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
