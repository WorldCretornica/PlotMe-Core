package com.worldcretornica.plotme_core.bukkit.listener;

import com.worldcretornica.plotme_core.PermissionNames;
import com.worldcretornica.plotme_core.Plot;
import com.worldcretornica.plotme_core.PlotId;
import com.worldcretornica.plotme_core.PlotMeCoreManager;
import com.worldcretornica.plotme_core.PlotMe_Core;
import com.worldcretornica.plotme_core.PlotWorldEdit;
import com.worldcretornica.plotme_core.api.IPlayer;
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

        BukkitLocation from = new BukkitLocation(event.getFrom());
        BukkitLocation to = new BukkitLocation(event.getTo());

        IPlayer player = plugin.wrapPlayer(event.getPlayer());

        if (event.getTo() == null) {
            worldEdit.removeMask(player);
        } else {
            boolean changemask = false;
            if (event.getFrom() != null) {
                if (!from.getWorld().getName().equalsIgnoreCase(to.getWorld().getName())) {
                    changemask = true;
                } else if (from.getLocation() != to.getLocation()) {
                    PlotId idFrom = manager.getPlotId(from);
                    PlotId idTo = manager.getPlotId(to);
                    if (idFrom == null) {
                        if (idTo != null) {
                            changemask = true;
                        }
                    } else if (!idFrom.equals(idTo)) {
                        changemask = true;
                    }
                }
            }

            if (changemask && manager.isPlotWorld(to.getWorld())) {
                if (!manager.isPlayerIgnoringWELimit(player)) {
                    worldEdit.setMask(player);
                } else {
                    worldEdit.removeMask(player);
                }
            }
        }

    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onPlayerJoin(PlayerJoinEvent event) {
        IPlayer player = plugin.wrapPlayer(event.getPlayer());
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
        if (event.getTo() == null) {
            worldEdit.removeMask(player);
            return;
        }
        BukkitLocation to = new BukkitLocation(event.getTo());
        BukkitLocation from = new BukkitLocation(event.getFrom());
        if (event.getFrom() != null && manager.isPlotWorld(from) && !manager.isPlotWorld(to)) {
            worldEdit.removeMask(player);
        } else if (manager.isPlotWorld(to)) {
            worldEdit.setMask(player);
        }
    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onPlayerCommandPreprocess(PlayerCommandPreprocessEvent event) {
        BukkitPlayer player = (BukkitPlayer) plugin.wrapPlayer(event.getPlayer());

        if (manager.isPlotWorld(player) && !manager.isPlayerIgnoringWELimit(player)) {
            if (event.getMessage().startsWith("//gmask") || event.getMessage().startsWith("/repl")) {
                player.sendMessage(api.C("ErrCannotUse"));
                event.setCancelled(true);
            } else if (event.getMessage().startsWith("//up")) {
                PlotId id = manager.getPlotId(player);
                if(id == null) {
                    event.setCancelled(true);
                    return;
                }
                Plot plot = manager.getPlotById(id, player);

                if (plot == null || !plot.isAllowed(player.getUniqueId())) {
                    player.sendMessage(api.C("ErrCannotUse"));
                    event.setCancelled(true);
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
    public void onPlayerInteract(PlayerInteractEvent event) {
        BukkitPlayer player = (BukkitPlayer) plugin.wrapPlayer(event.getPlayer());
        BukkitLocation location = new BukkitLocation(event.getClickedBlock().getLocation());
        if (manager.isPlotWorld(player) && (event.getAction() == Action.LEFT_CLICK_BLOCK || event.getAction() == Action.RIGHT_CLICK_BLOCK)) {
            if (!player.hasPermission(PermissionNames.ADMIN_BUILDANYWHERE) && !manager.isPlayerIgnoringWELimit(player) && event.getItem() != null
                    && event.getItem().getType() != Material.AIR) {
                PlotId id = manager.getPlotId(location);
                Plot plot = manager.getMap(location).getPlot(id);

                if (plot != null && plot.isAllowed(player.getUniqueId())) {
                    worldEdit.setMask(player, id);
                } else {
                    player.sendMessage("You can't WorldEdit here");
                    event.setCancelled(true);
                }
            }
        }
    }
}