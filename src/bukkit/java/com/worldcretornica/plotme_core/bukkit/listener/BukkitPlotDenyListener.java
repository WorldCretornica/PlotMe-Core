package com.worldcretornica.plotme_core.bukkit.listener;

import com.worldcretornica.plotme_core.Plot;
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

    private PlotMe_Core api;

    public BukkitPlotDenyListener(PlotMe_CorePlugin instance) {
        PlotMe_CorePlugin plugin = instance;
        api = plugin.getAPI();
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerMove(final PlayerMoveEvent event) {
        BukkitPlayer p = new BukkitPlayer(event.getPlayer());

        if (api.getPlotMeCoreManager().isPlotWorld(p) && !api.cPerms(p, "plotme.admin.bypassdeny")) {
            BukkitLocation to = new BukkitLocation(event.getTo());

            String idTo = api.getPlotMeCoreManager().getPlotId(to);

            if (!idTo.isEmpty()) {
                Plot plot = api.getPlotMeCoreManager().getPlotById(p, idTo);

                if (plot != null && plot.isDenied(p.getUniqueId())) {
                    event.setCancelled(true);
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerTeleport(final PlayerTeleportEvent event) {
        BukkitPlayer p = new BukkitPlayer(event.getPlayer());

        if (api.getPlotMeCoreManager().isPlotWorld(p) && !api.cPerms(p, "plotme.admin.bypassdeny")) {
            BukkitLocation to = new BukkitLocation(event.getTo());

            String idTo = api.getPlotMeCoreManager().getPlotId(to);

            if (!idTo.isEmpty()) {
                Plot plot = api.getPlotMeCoreManager().getPlotById(p, idTo);

                if (plot != null && plot.isDenied(p.getUniqueId())) {
                    BukkitLocation location = (BukkitLocation) api.getPlotMeCoreManager().getPlotHome(p.getWorld(), plot.getId());
                    event.setTo(location.getLocation());
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerJoin(final PlayerJoinEvent event) {
        BukkitPlayer p = new BukkitPlayer(event.getPlayer());

        if (api.getPlotMeCoreManager().isPlotWorld(p) && !api.cPerms(p, "plotme.admin.bypassdeny")) {
            String id = api.getPlotMeCoreManager().getPlotId(p.getLocation());

            if (!id.isEmpty()) {
                Plot plot = api.getPlotMeCoreManager().getPlotById(p, id);

                if (plot != null && plot.isDenied(p.getUniqueId())) {
                    p.teleport(api.getPlotMeCoreManager().getPlotHome(p.getWorld(), plot.getId()));
                }
            }
        }
    }
}
