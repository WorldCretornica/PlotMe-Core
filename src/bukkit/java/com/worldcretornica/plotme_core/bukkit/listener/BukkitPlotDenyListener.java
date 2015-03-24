package com.worldcretornica.plotme_core.bukkit.listener;

import com.worldcretornica.plotme_core.PermissionNames;
import com.worldcretornica.plotme_core.Plot;
import com.worldcretornica.plotme_core.PlotId;
import com.worldcretornica.plotme_core.PlotMeCoreManager;
import com.worldcretornica.plotme_core.bukkit.PlotMe_CorePlugin;
import com.worldcretornica.plotme_core.bukkit.api.BukkitLocation;
import com.worldcretornica.plotme_core.bukkit.api.BukkitPlayer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;

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

            PlotId idTo = manager.getPlotId(to);

            if (idTo != null) {
                Plot plot = manager.getPlotById(idTo, player);

                if (plot != null && plot.isDeniedInternal(player.getName())) {
                    event.setTo(event.getFrom());
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerJoin(PlayerJoinEvent event) {
        BukkitPlayer player = (BukkitPlayer) plugin.wrapPlayer(event.getPlayer());

        if (manager.isPlotWorld(player) && !player.hasPermission(PermissionNames.ADMIN_BYPASSDENY)) {
            PlotId id = manager.getPlotId(player);

            if (id != null) {
                Plot plot = manager.getPlotById(id, player);

                if (plot != null && plot.isDenied(player.getUniqueId())) {
                    player.setLocation(manager.getPlotHome(player.getWorld(), plot.getId()));
                }
            }
        }
    }
}