package com.worldcretornica.plotme_core.bukkit.listener;

import com.worldcretornica.plotme_core.*;
import com.worldcretornica.plotme_core.bukkit.PlotMe_CorePlugin;
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

    private final PlotMe_Core api;
    private final PlotWorldEdit worldEdit;


    public BukkitPlotWorldEditListener(PlotMe_CorePlugin plugin, PlotWorldEdit worldEdit) {
        api = plugin.getAPI();
        this.worldEdit = worldEdit;
    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onPlayerMove(PlayerMoveEvent event) {
        BukkitLocation from = new BukkitLocation(event.getFrom());
        BukkitLocation to = new BukkitLocation(event.getTo());

        BukkitPlayer player = new BukkitPlayer(event.getPlayer());

        String idTo = "";

        boolean changemask = false;
        if (!from.getWorld().getName().equalsIgnoreCase(to.getWorld().getName())) {
            changemask = true;
        } else if (from.getBlockX() != to.getBlockX() || from.getBlockZ() != to.getBlockZ()) {
            String idFrom = PlotMeCoreManager.getPlotId(from);
            idTo = PlotMeCoreManager.getPlotId(to);

            if (!idFrom.equals(idTo)) {
                changemask = true;
            }
        }

        if (changemask && api.getPlotMeCoreManager().isPlotWorld(to.getWorld())) {
            if (api.getPlotMeCoreManager().isPlayerIgnoringWELimit(player.getUniqueId())) {
                worldEdit.removeMask(player);
            } else {
                worldEdit.setMask(player, idTo);
            }
        }
    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onPlayerJoin(PlayerJoinEvent event) {
        BukkitPlayer player = new BukkitPlayer(event.getPlayer());
        if (api.getPlotMeCoreManager().isPlotWorld(player)) {
            if (api.getPlotMeCoreManager().isPlayerIgnoringWELimit(player.getUniqueId())) {
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
        BukkitPlayer player = new BukkitPlayer(event.getPlayer());
        BukkitLocation from = new BukkitLocation(event.getFrom());
        BukkitLocation to = new BukkitLocation(event.getTo());

        if (api.getPlotMeCoreManager().isPlotWorld(from)) {
            if (api.getPlotMeCoreManager().isPlotWorld(to)) {
                worldEdit.setMask(player);
            } else {
                worldEdit.removeMask(player);
            }
        } else if (api.getPlotMeCoreManager().isPlotWorld(to)) {
            worldEdit.setMask(player);
        }
    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onPlayerPortal(PlayerPortalEvent event) {
        BukkitPlayer player = new BukkitPlayer(event.getPlayer());
        BukkitLocation from = new BukkitLocation(event.getFrom());
        BukkitLocation to = new BukkitLocation(event.getTo());

        if (api.getPlotMeCoreManager().isPlotWorld(from)) {
            if (api.getPlotMeCoreManager().isPlotWorld(to)) {
                worldEdit.setMask(player);
            } else {
                worldEdit.removeMask(player);
            }
        } else if (api.getPlotMeCoreManager().isPlotWorld(to)) {
            worldEdit.setMask(player);
        }
    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onPlayerCommandPreprocess(PlayerCommandPreprocessEvent event) {
        BukkitPlayer player = new BukkitPlayer(event.getPlayer());

        if (api.getPlotMeCoreManager().isPlotWorld(player)) {
            if (!api.getPlotMeCoreManager().isPlayerIgnoringWELimit(player.getUniqueId())) {
                if (event.getMessage().startsWith("//gmask")) {
                    event.setCancelled(true);
                } else if (event.getMessage().startsWith("//up")) {
                    Plot plot = api.getPlotMeCoreManager().getPlotById(player);

                    if (plot == null || !plot.isAllowed(player.getName(), player.getUniqueId())) {
                        event.setCancelled(true);
                    }
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
    public void onPlayerInteract(PlayerInteractEvent event) {
        BukkitPlayer player = new BukkitPlayer(event.getPlayer());

        if (api.getPlotMeCoreManager().isPlotWorld(player)) {
            if (!player.hasPermission(PermissionNames.ADMIN_BUILDANYWHERE) &&
                        !api.getPlotMeCoreManager().isPlayerIgnoringWELimit(player.getUniqueId()) &&
                        (event.getAction() == Action.LEFT_CLICK_BLOCK || event.getAction() == Action.RIGHT_CLICK_BLOCK) && ((BukkitMaterial) player.getItemInHand().getType()).getMaterial() != Material.AIR) {
                Plot plot = api.getPlotMeCoreManager().getPlotById(player);

                if (plot != null && plot.isAllowed(player.getName(), player.getUniqueId())) {
                    worldEdit.setMask(player);
                } else {
                    event.setCancelled(true);
                }
            }
        }
    }
}