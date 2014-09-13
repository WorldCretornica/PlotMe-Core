package com.worldcretornica.plotme_core.listener;

import com.worldcretornica.plotme_core.Plot;
import com.worldcretornica.plotme_core.PlotMe_Core;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.*;

public class PlotWorldEditListener implements Listener {

    private PlotMe_Core plugin = null;

    public PlotWorldEditListener(PlotMe_Core instance) {
        plugin = instance;
    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onPlayerMove(final PlayerMoveEvent event) {
        Location from = event.getFrom();
        Location to = event.getTo();

        boolean changemask = false;
        Player p = event.getPlayer();

        if (to == null) {
            plugin.getPlotWorldEdit().removeMask(p);
        } else {
            String idTo = "";

            if (from != null) {
                if (!from.getWorld().getName().equalsIgnoreCase(to.getWorld().getName())) {
                    changemask = true;
                } else if (from.getBlockX() != to.getBlockX() || from.getBlockZ() != to.getBlockZ()) {
                    String idFrom = plugin.getPlotMeCoreManager().getPlotId(from);
                    idTo = plugin.getPlotMeCoreManager().getPlotId(to);

                    if (!idFrom.equalsIgnoreCase(idTo)) {
                        changemask = true;
                    }
                }
            }

	        if (changemask && plugin.getPlotMeCoreManager().isPlotWorld(to.getWorld())) {
		        if (!plugin.getPlotMeCoreManager().isPlayerIgnoringWELimit(p.getName())) {
			        plugin.getPlotWorldEdit().setMask(p, idTo);
		        } else {
			        plugin.getPlotWorldEdit().removeMask(p);
		        }
	        }
        }
    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onPlayerJoin(final PlayerJoinEvent event) {
        Player p = event.getPlayer();
        if (plugin.getPlotMeCoreManager().isPlotWorld(p)) {
            if (!plugin.getPlotMeCoreManager().isPlayerIgnoringWELimit(p.getName())) {
                plugin.getPlotWorldEdit().setMask(p);
            } else {
                plugin.getPlotWorldEdit().removeMask(p);
            }
        } else {
            plugin.getPlotWorldEdit().removeMask(p);
        }
    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onPlayerTeleport(final PlayerTeleportEvent event) {
        Player p = event.getPlayer();
        Location from = event.getFrom();
        Location to = event.getTo();

	    if (to == null || from != null && plugin.getPlotMeCoreManager().isPlotWorld(from) && !plugin.getPlotMeCoreManager().isPlotWorld(to)) {
		    plugin.getPlotWorldEdit().removeMask(p);
	    } else if (plugin.getPlotMeCoreManager().isPlotWorld(to)) {
		    plugin.getPlotWorldEdit().setMask(p);
	    }
    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onPlayerPortal(final PlayerPortalEvent event) {
        Player p = event.getPlayer();
        Location from = event.getFrom();
        Location to = event.getTo();

	    if (to == null || from != null && plugin.getPlotMeCoreManager().isPlotWorld(from) && !plugin.getPlotMeCoreManager().isPlotWorld(to)) {
		    plugin.getPlotWorldEdit().removeMask(p);
	    } else if (plugin.getPlotMeCoreManager().isPlotWorld(to)) {
		    plugin.getPlotWorldEdit().setMask(p);
	    }
    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onPlayerCommandPreprocess(final PlayerCommandPreprocessEvent event) {
        Player p = event.getPlayer();

        if (plugin.getPlotMeCoreManager().isPlotWorld(p) && !plugin.getPlotMeCoreManager().isPlayerIgnoringWELimit(p.getName())) {
            if (event.getMessage().startsWith("//gmask")) {
                event.setCancelled(true);
            } else if (event.getMessage().startsWith("//up")) {
                Plot plot = plugin.getPlotMeCoreManager().getPlotById(p);

                if (plot == null || !plot.isAllowed(p.getName())) {
                    event.setCancelled(true);
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
    public void onPlayerInteract(final PlayerInteractEvent event) {
        Player p = event.getPlayer();

	    if (plugin.getPlotMeCoreManager().isPlotWorld(p) && !plugin.cPerms(p, "plotme.admin.buildanywhere") && !plugin.getPlotMeCoreManager().isPlayerIgnoringWELimit(p.getName()) && (event.getAction() == Action.LEFT_CLICK_BLOCK || event.getAction() == Action.RIGHT_CLICK_BLOCK)
			        && p.getItemInHand() != null && p.getItemInHand().getType() != Material.AIR) {
		    Block b = event.getClickedBlock();
		    Plot plot = plugin.getPlotMeCoreManager().getPlotById(b);

            if (plot != null && plot.isAllowed(p.getName())) {
                plugin.getPlotWorldEdit().setMask(p, b.getLocation());
		    } else {
			    event.setCancelled(true);
		    }
	    }
    }
}
