package com.worldcretornica.plotme_core.bukkit.listener;

import com.worldcretornica.plotme_core.Plot;
import com.worldcretornica.plotme_core.PlotMeCoreManager;
import com.worldcretornica.plotme_core.PlotMe_Core;
import com.worldcretornica.plotme_core.bukkit.PlotMe_CorePlugin;
import com.worldcretornica.plotme_core.bukkit.api.BukkitLocation;
import com.worldcretornica.plotme_core.bukkit.api.BukkitPlayer;
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

        if (api.getPlotMeCoreManager().isPlotWorld(player) && !player.hasPermission("plotme.admin.bypassdeny")) {
            BukkitLocation to = new BukkitLocation(event.getTo());

            String idTo = PlotMeCoreManager.getPlotId(to);

            if (!idTo.isEmpty()) {
                Plot plot = api.getPlotMeCoreManager().getPlotById(player, idTo);

                if (plot != null && plot.isDenied(player.getUniqueId())) {
                    event.setCancelled(true);
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerTeleport(PlayerTeleportEvent event) {
        BukkitPlayer player = new BukkitPlayer(event.getPlayer());

        if (api.getPlotMeCoreManager().isPlotWorld(player) && !player.hasPermission("plotme.admin.bypassdeny")) {
            BukkitLocation to = new BukkitLocation(event.getTo());

            String idTo = PlotMeCoreManager.getPlotId(to);

            if (!idTo.isEmpty()) {
                Plot plot = api.getPlotMeCoreManager().getPlotById(player, idTo);

                if (plot != null && plot.isDenied(player.getUniqueId())) {
                    BukkitLocation location = (BukkitLocation) PlotMeCoreManager.getPlotHome(player.getWorld(), plot.getId());
                    event.setTo(location.getLocation());
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerJoin(PlayerJoinEvent event) {
        BukkitPlayer player = new BukkitPlayer(event.getPlayer());

        if (api.getPlotMeCoreManager().isPlotWorld(player) && !player.hasPermission("plotme.admin.bypassdeny")) {
            String id = PlotMeCoreManager.getPlotId(player);

            if (!id.isEmpty()) {
                Plot plot = api.getPlotMeCoreManager().getPlotById(player, id);

                if (plot != null && plot.isDenied(player.getUniqueId())) {
                    player.teleport(PlotMeCoreManager.getPlotHome(player.getWorld(), plot.getId()));
                }
            }
        }
    }
}
