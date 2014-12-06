package com.worldcretornica.plotme_core.listener;

import com.worldcretornica.plotme_core.Plot;
import com.worldcretornica.plotme_core.PlotMe_Core;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

public class PlotDenyListener implements Listener {

    private PlotMe_Core plugin = null;

    public PlotDenyListener(PlotMe_Core instance) {
        plugin = instance;
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerMove(final PlayerMoveEvent event) {
        Player p = event.getPlayer();

        if (plugin.getPlotMeCoreManager().isPlotWorld(p) && !plugin.cPerms(p, "plotme.admin.bypassdeny")) {
            Location to = event.getTo();

            String idTo = plugin.getPlotMeCoreManager().getPlotId(to);

            if (!idTo.equals("")) {
                Plot plot = plugin.getPlotMeCoreManager().getPlotById(p, idTo);

                if (plot != null && plot.isDenied(p.getUniqueId())) {
                    Location to = event.getFrom().clone();
                    to.setYaw(event.getTo().getYaw());
                    to.setPitch(Event.getTo().getPitch());
                    event.setTo(to);
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerTeleport(final PlayerTeleportEvent event) {
        Player p = event.getPlayer();

        if (plugin.getPlotMeCoreManager().isPlotWorld(p) && !plugin.cPerms(p, "plotme.admin.bypassdeny")) {
            Location to = event.getTo();

            String idTo = plugin.getPlotMeCoreManager().getPlotId(to);

            if (!idTo.equals("")) {
                Plot plot = plugin.getPlotMeCoreManager().getPlotById(p, idTo);

                if (plot != null && plot.isDenied(p.getUniqueId())) {
                    event.setTo(plugin.getPlotMeCoreManager().getPlotHome(p.getWorld(), plot.getId()));
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerJoin(final PlayerJoinEvent event) {
        Player p = event.getPlayer();

        if (plugin.getPlotMeCoreManager().isPlotWorld(p) && !plugin.cPerms(p, "plotme.admin.bypassdeny")) {
            String id = plugin.getPlotMeCoreManager().getPlotId(p.getLocation());

            if (!id.equals("")) {
                Plot plot = plugin.getPlotMeCoreManager().getPlotById(p, id);

                if (plot != null && plot.isDenied(p.getUniqueId())) {
                    p.teleport(plugin.getPlotMeCoreManager().getPlotHome(p.getWorld(), plot.getId()));
                }
            }
        }
    }
}
