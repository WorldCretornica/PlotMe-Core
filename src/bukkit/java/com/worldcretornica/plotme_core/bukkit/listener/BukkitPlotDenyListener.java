package com.worldcretornica.plotme_core.bukkit.listener;

import com.worldcretornica.plotme_core.PermissionNames;
import com.worldcretornica.plotme_core.Plot;
import com.worldcretornica.plotme_core.PlotMeCoreManager;
import com.worldcretornica.plotme_core.PlotMe_Core;
import com.worldcretornica.plotme_core.bukkit.PlotMe_CorePlugin;
import com.worldcretornica.plotme_core.bukkit.api.BukkitLocation;
import com.worldcretornica.plotme_core.bukkit.api.BukkitPlayer;
import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

public class BukkitPlotDenyListener implements Listener {

    private final PlotMe_Core api;

    public BukkitPlotDenyListener(PlotMe_CorePlugin instance) {
        api = instance.getAPI();
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerMove(PlayerMoveEvent event) {
        BukkitPlayer player = new BukkitPlayer(event.getPlayer());

        if (api.getPlotMeCoreManager().isPlotWorld(player) && !player.hasPermission(PermissionNames.ADMIN_BYPASSDENY)) {
            if (event.getTo() == null) {
                return;
            }
            BukkitLocation to = new BukkitLocation(event.getTo());

            String idTo = PlotMeCoreManager.getPlotId(to);

            if (!idTo.isEmpty()) {
                Plot plot = api.getPlotMeCoreManager().getPlotById(idTo, player);

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
        BukkitPlayer player = new BukkitPlayer(event.getPlayer());

        if (api.getPlotMeCoreManager().isPlotWorld(player) && !player.hasPermission(PermissionNames.ADMIN_BYPASSDENY)) {
            if (event.getTo() == null) {
                return;
            }
            BukkitLocation to = new BukkitLocation(event.getTo());

            String idTo = PlotMeCoreManager.getPlotId(to);

            if (!idTo.isEmpty()) {
                Plot plot = api.getPlotMeCoreManager().getPlotById(idTo, player);

                if (plot != null && plot.isDeniedInternal(player.getName(), player.getUniqueId())) {
                    BukkitLocation location = (BukkitLocation) PlotMeCoreManager.getPlotHome(player.getWorld(), plot.getId());
                    event.setTo(location.getLocation());
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerJoin(PlayerJoinEvent event) {
        BukkitPlayer player = new BukkitPlayer(event.getPlayer());

        if (api.getPlotMeCoreManager().isPlotWorld(player) && !player.hasPermission(PermissionNames.ADMIN_BYPASSDENY)) {
            String id = PlotMeCoreManager.getPlotId(player);

            if (!id.isEmpty()) {
                Plot plot = api.getPlotMeCoreManager().getPlotById(id, player);

                if (plot != null && plot.isDeniedInternal(player.getName(), player.getUniqueId())) {
                    player.setLocation(PlotMeCoreManager.getPlotHome(player.getWorld(), plot.getId()));
                }
            }
        }
    }
}