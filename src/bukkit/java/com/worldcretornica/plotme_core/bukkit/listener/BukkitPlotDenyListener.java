package com.worldcretornica.plotme_core.bukkit.listener;

import com.worldcretornica.plotme_core.PermissionNames;
import com.worldcretornica.plotme_core.Plot;
import com.worldcretornica.plotme_core.PlotMeCoreManager;
import com.worldcretornica.plotme_core.bukkit.*;
import com.worldcretornica.plotme_core.bukkit.api.*;
import org.bukkit.*;
import org.bukkit.event.*;
import org.bukkit.event.player.*;

public class BukkitPlotDenyListener implements Listener {

    private final PlotMe_CorePlugin plugin;
    private final PlotMeCoreManager manager;
    
    public BukkitPlotDenyListener(PlotMe_CorePlugin instance) {
        plugin = instance;
        manager = PlotMeCoreManager.getInstance();
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerMove(PlayerMoveEvent event) {
        BukkitPlayer player = (BukkitPlayer) plugin.wrapPlayer(event.getPlayer());

        if (manager.isPlotWorld(player) && !player.hasPermission(PermissionNames.ADMIN_BYPASSDENY)) {
            BukkitLocation to = new BukkitLocation(event.getTo());

            String idTo = manager.getPlotId(to);

            if (!idTo.isEmpty()) {
                Plot plot = manager.getPlotById(idTo, player);

                if (plot != null && plot.isDeniedInternal(player.getName(), player.getUniqueId())) {
                    Location t = event.getFrom().clone();
                    t.setYaw(event.getTo().getYaw());
                    t.setPitch(event.getTo().getPitch());
                    event.setTo(t);
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerTeleport(PlayerTeleportEvent event) {
        BukkitPlayer player = (BukkitPlayer) plugin.wrapPlayer(event.getPlayer());

        if (manager.isPlotWorld(player) && !player.hasPermission(PermissionNames.ADMIN_BYPASSDENY)) {
            BukkitLocation to = new BukkitLocation(event.getTo());

            String idTo = manager.getPlotId(to);

            if (!idTo.isEmpty()) {
                Plot plot = manager.getPlotById(idTo, player);

                if (plot != null && plot.isDeniedInternal(player.getName(), player.getUniqueId())) {
                    BukkitLocation location = (BukkitLocation) manager.getPlotHome(player.getWorld(), plot.getId());
                    event.setTo(location.getLocation());
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerJoin(PlayerJoinEvent event) {
        BukkitPlayer player = (BukkitPlayer) plugin.wrapPlayer(event.getPlayer());

        if (manager.isPlotWorld(player) && !player.hasPermission(PermissionNames.ADMIN_BYPASSDENY)) {
            String id = manager.getPlotId(player);

            if (!id.isEmpty()) {
                Plot plot = manager.getPlotById(id, player);

                if (plot != null && plot.isDenied(player.getUniqueId())) {
                    player.setLocation(manager.getPlotHome(player.getWorld(), plot.getId()));
                }
            }
        }
    }
}