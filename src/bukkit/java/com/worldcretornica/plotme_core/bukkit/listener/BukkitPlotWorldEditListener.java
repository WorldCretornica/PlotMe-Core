package com.worldcretornica.plotme_core.bukkit.listener;

import com.worldcretornica.plotme_core.PermissionNames;
import com.worldcretornica.plotme_core.Plot;
import com.worldcretornica.plotme_core.PlotId;
import com.worldcretornica.plotme_core.PlotMeCoreManager;
import com.worldcretornica.plotme_core.PlotMe_Core;
import com.worldcretornica.plotme_core.PlotWorldEdit;
import com.worldcretornica.plotme_core.bukkit.PlotMe_CorePlugin;
import com.worldcretornica.plotme_core.bukkit.api.BukkitLocation;
import com.worldcretornica.plotme_core.bukkit.api.BukkitPlayer;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

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
        if (event.getTo() == null || event.getFrom() == null) {
            return;
        }
        BukkitLocation from = new BukkitLocation(event.getFrom());
        BukkitLocation to = new BukkitLocation(event.getTo());

        BukkitPlayer player = (BukkitPlayer) plugin.wrapPlayer(event.getPlayer());

        PlotId idTo = null;

        boolean changeMask = false;
        if (!from.getWorld().getName().equalsIgnoreCase(to.getWorld().getName())) {
            changeMask = true;
        } else if (from.getLocation() != to.getLocation()) {
            PlotId idFrom = manager.getPlotId(from);
            idTo = manager.getPlotId(to);
            if (idFrom != null) {
                if (!idFrom.equals(idTo)) {
                    changeMask = true;
                }
            } else if (idTo != null) {
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
            if (!manager.isPlayerIgnoringWELimit(player)) {
                worldEdit.setMask(player);
            } else {
                worldEdit.removeMask(player);
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
        if (event.getFrom() == null || event.getTo() == null) {
            return;
        }
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
        if (manager.isPlotWorld(player)) {
            if (!player.hasPermission(PermissionNames.ADMIN_BUILDANYWHERE) &&
                    !manager.isPlayerIgnoringWELimit(player) &&
                    (event.getAction() == Action.LEFT_CLICK_BLOCK || event.getAction() == Action.RIGHT_CLICK_BLOCK)
                    && event.getItem() != null && event.getItem().getType() != Material.AIR) {
                PlotId id = manager.getPlotId(location);
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