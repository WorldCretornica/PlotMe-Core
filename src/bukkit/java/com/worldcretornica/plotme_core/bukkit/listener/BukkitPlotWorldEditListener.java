package com.worldcretornica.plotme_core.bukkit.listener;

import com.worldcretornica.plotme_core.Plot;
import com.worldcretornica.plotme_core.PlotMe_Core;
import com.worldcretornica.plotme_core.bukkit.PlotMe_CorePlugin;
import com.worldcretornica.plotme_core.bukkit.api.BukkitBlock;
import com.worldcretornica.plotme_core.bukkit.api.BukkitLocation;
import com.worldcretornica.plotme_core.bukkit.api.BukkitMaterial;
import com.worldcretornica.plotme_core.bukkit.api.BukkitPlayer;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.*;

public class BukkitPlotWorldEditListener implements Listener {

    private PlotMe_Core api;

    public BukkitPlotWorldEditListener(PlotMe_CorePlugin plugin) {
        this.api = plugin.getAPI();
    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onPlayerMove(PlayerMoveEvent event) {
        BukkitLocation from = new BukkitLocation(event.getFrom());
        BukkitLocation to = new BukkitLocation(event.getTo());

        BukkitPlayer p = new BukkitPlayer(event.getPlayer());

        if (event.getTo() == null) {
            api.getServerBridge().getPlotWorldEdit().removeMask(p);
        } else {
            String idTo = "";

            boolean changemask = false;
            if (!from.getWorld().getName().equalsIgnoreCase(to.getWorld().getName())) {
                changemask = true;
            } else if (from.getBlockX() != to.getBlockX() || from.getBlockZ() != to.getBlockZ()) {
                String idFrom = api.getPlotMeCoreManager().getPlotId(from);
                idTo = api.getPlotMeCoreManager().getPlotId(to);

                if (!idFrom.equalsIgnoreCase(idTo)) {
                    changemask = true;
                }
            }

            if (changemask && api.getPlotMeCoreManager().isPlotWorld(to.getWorld())) {
                if (!api.getPlotMeCoreManager().isPlayerIgnoringWELimit(p.getUniqueId())) {
                    api.getServerBridge().getPlotWorldEdit().setMask(p, idTo);
                } else {
                    api.getServerBridge().getPlotWorldEdit().removeMask(p);
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onPlayerJoin(PlayerJoinEvent event) {
        BukkitPlayer p = new BukkitPlayer(event.getPlayer());
        if (api.getPlotMeCoreManager().isPlotWorld(p)) {
            if (!api.getPlotMeCoreManager().isPlayerIgnoringWELimit(p.getUniqueId())) {
                api.getServerBridge().getPlotWorldEdit().setMask(p);
            } else {
                api.getServerBridge().getPlotWorldEdit().removeMask(p);
            }
        } else {
            api.getServerBridge().getPlotWorldEdit().removeMask(p);
        }
    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onPlayerTeleport(PlayerTeleportEvent event) {
        BukkitPlayer p = new BukkitPlayer(event.getPlayer());
        BukkitLocation from = new BukkitLocation(event.getFrom());
        BukkitLocation to = new BukkitLocation(event.getTo());

        if (event.getTo() == null || event.getFrom() != null && api.getPlotMeCoreManager().isPlotWorld(from) && !api.getPlotMeCoreManager().isPlotWorld(to)) {
            api.getServerBridge().getPlotWorldEdit().removeMask(p);
        } else if (api.getPlotMeCoreManager().isPlotWorld(to)) {
            api.getServerBridge().getPlotWorldEdit().setMask(p);
        }
    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onPlayerPortal(PlayerPortalEvent event) {
        BukkitPlayer p = new BukkitPlayer(event.getPlayer());
        BukkitLocation from = new BukkitLocation(event.getFrom());
        BukkitLocation to = new BukkitLocation(event.getTo());

        if (event.getTo() == null || event.getFrom() != null && api.getPlotMeCoreManager().isPlotWorld(from) && !api.getPlotMeCoreManager().isPlotWorld(to)) {
            api.getServerBridge().getPlotWorldEdit().removeMask(p);
        } else if (api.getPlotMeCoreManager().isPlotWorld(to)) {
            api.getServerBridge().getPlotWorldEdit().setMask(p);
        }
    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onPlayerCommandPreprocess(PlayerCommandPreprocessEvent event) {
        BukkitPlayer p = new BukkitPlayer(event.getPlayer());

        if (api.getPlotMeCoreManager().isPlotWorld(p) && !api.getPlotMeCoreManager().isPlayerIgnoringWELimit(p.getUniqueId())) {
            if (event.getMessage().startsWith("//gmask")) {
                event.setCancelled(true);
            } else if (event.getMessage().startsWith("//up")) {
                Plot plot = api.getPlotMeCoreManager().getPlotById(p.getLocation());

                if (plot == null || !plot.isAllowed(p.getUniqueId())) {
                    event.setCancelled(true);
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
    public void onPlayerInteract(PlayerInteractEvent event) {
        BukkitPlayer p = new BukkitPlayer(event.getPlayer());

        if (api.getPlotMeCoreManager().isPlotWorld(p) && !PlotMe_Core.cPerms(p, "plotme.admin.buildanywhere") &&
                !api.getPlotMeCoreManager().isPlayerIgnoringWELimit(p.getUniqueId()) && 
                (event.getAction() == Action.LEFT_CLICK_BLOCK || event.getAction() == Action.RIGHT_CLICK_BLOCK) && 
                p.getItemInHand() != null && ((BukkitMaterial) p.getItemInHand().getType()).getMaterial() != Material.AIR) {
            BukkitBlock b = new BukkitBlock(event.getClickedBlock());
            Plot plot = api.getPlotMeCoreManager().getPlotById(b.getLocation());

            if (plot != null && plot.isAllowed(p.getUniqueId())) {
                api.getServerBridge().getPlotWorldEdit().setMask(p, b.getLocation());
            } else {
                event.setCancelled(true);
            }
        }
    }
}
