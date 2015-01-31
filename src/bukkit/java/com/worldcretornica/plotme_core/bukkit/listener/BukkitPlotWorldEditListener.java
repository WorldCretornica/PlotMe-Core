package com.worldcretornica.plotme_core.bukkit.listener;

import com.worldcretornica.plotme_core.PermissionNames;
import com.worldcretornica.plotme_core.Plot;
import com.worldcretornica.plotme_core.PlotMeCoreManager;
import com.worldcretornica.plotme_core.PlotMe_Core;
import com.worldcretornica.plotme_core.PlotWorldEdit;
import com.worldcretornica.plotme_core.bukkit.*;
import com.worldcretornica.plotme_core.bukkit.api.*;
import org.bukkit.*;
import org.bukkit.event.*;
import org.bukkit.event.block.*;
import org.bukkit.event.player.*;

public class BukkitPlotWorldEditListener implements Listener {

    private final PlotMe_Core api;
    private final PlotWorldEdit worldEdit;
    private final PlotMe_CorePlugin plugin;
    private final PlotMeCoreManager manager;


    public BukkitPlotWorldEditListener(PlotWorldEdit worldEdit, PlotMe_CorePlugin plugin) {
        api = plugin.getAPI();
        this.plugin = plugin;
        this.worldEdit = worldEdit;
        manager = PlotMeCoreManager.getInstance();
    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onPlayerMove(PlayerMoveEvent event) {
        BukkitLocation from = new BukkitLocation(event.getFrom());
        BukkitLocation to = new BukkitLocation(event.getTo());

        BukkitPlayer player = (BukkitPlayer) plugin.wrapPlayer(event.getPlayer());

        String idTo = "";

        boolean changeMask = false;
        if (!from.getWorld().getName().equalsIgnoreCase(to.getWorld().getName())) {
            changeMask = true;
        } else if (from.getLocation() != to.getLocation()) {
            String idFrom = manager.getPlotId(from);
            idTo = manager.getPlotId(to);

            if (!idFrom.equals(idTo)) {
                changeMask = true;
            }
        }

        if (changeMask && manager.isPlotWorld(to.getWorld())) {
            if (manager.isPlayerIgnoringWELimit(player)) {
                worldEdit.removeMask(player);
            } else {
                worldEdit.setMask(player, idTo);
            }
        }
    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onPlayerJoin(PlayerJoinEvent event) {
        BukkitPlayer player = (BukkitPlayer) plugin.wrapPlayer(event.getPlayer());
        if (manager.isPlotWorld(player)) {
            if (manager.isPlayerIgnoringWELimit(player)) {
                worldEdit.removeMask(player);
            } else {
                worldEdit.setMask(player);
            }
        } else {
            worldEdit.removeMask(player);
        }
    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onPlayerTeleport(PlayerTeleportEvent event) {
        BukkitPlayer player = (BukkitPlayer) plugin.wrapPlayer(event.getPlayer());
        BukkitLocation from = new BukkitLocation(event.getFrom());
        BukkitLocation to = new BukkitLocation(event.getTo());
        if (manager.isPlotWorld(from)) {
            if (manager.isPlotWorld(to)) {
                worldEdit.setMask(player);
            } else {
                worldEdit.removeMask(player);
            }
        } else if (manager.isPlotWorld(to)) {
            worldEdit.setMask(player);
        }
    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onPlayerPortal(PlayerPortalEvent event) {
        BukkitPlayer player = (BukkitPlayer) plugin.wrapPlayer(event.getPlayer());
        BukkitLocation from = new BukkitLocation(event.getFrom());
        BukkitLocation to = new BukkitLocation(event.getTo());
        if (manager.isPlotWorld(from)) {
            if (manager.isPlotWorld(to)) {
                worldEdit.setMask(player);
            } else {
                worldEdit.removeMask(player);
            }
        } else if (manager.isPlotWorld(to)) {
            worldEdit.setMask(player);
        }
    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onPlayerCommandPreprocess(PlayerCommandPreprocessEvent event) {
        BukkitPlayer player = (BukkitPlayer) plugin.wrapPlayer(event.getPlayer());

        if (manager.isPlotWorld(player)) {
            if (!manager.isPlayerIgnoringWELimit(player)) {
                if (event.getMessage().startsWith("//gmask")) {
                    player.sendMessage(api.getUtil().C("ErrCannotUse"));
                    event.setCancelled(true);
                } else if (event.getMessage().startsWith("//up")) {
                    Plot plot = manager.getPlotById(player);

                    if (plot == null || !plot.isAllowed(player.getName(), player.getUniqueId())) {
                        player.sendMessage(api.getUtil().C("ErrCannotUse"));
                        event.setCancelled(true);
                    }
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
    public void onPlayerInteract(PlayerInteractEvent event) {
        BukkitPlayer player = (BukkitPlayer) plugin.wrapPlayer(event.getPlayer());
        BukkitLocation location = new BukkitLocation(event.getClickedBlock().getLocation());

        if (manager.isPlotWorld(location)) {
            if (!player.hasPermission(PermissionNames.ADMIN_BUILDANYWHERE) &&
                !manager.isPlayerIgnoringWELimit(player) &&
                (event.getAction() == Action.LEFT_CLICK_BLOCK || event.getAction() == Action.RIGHT_CLICK_BLOCK)
                && ((BukkitMaterial) player.getItemInHand().getType()).getMaterial() != Material.AIR) {
                String id = manager.getPlotId(location);
                Plot plot = manager.getMap(location).getPlot(id);

                if (plot != null && plot.isAllowed(player.getName(), player.getUniqueId())) {
                    worldEdit.setMask(player);
                } else {
                    player.sendMessage(api.getUtil().C("ErrCannotBuild"));
                    event.setCancelled(true);
                }
            }
        }
    }
}