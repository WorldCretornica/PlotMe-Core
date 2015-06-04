package com.worldcretornica.plotme_core.bukkit.listener;

import com.worldcretornica.plotme_core.PermissionNames;
import com.worldcretornica.plotme_core.Plot;
import com.worldcretornica.plotme_core.PlotMeCoreManager;
import com.worldcretornica.plotme_core.api.IPlayer;
import com.worldcretornica.plotme_core.api.Location;
import com.worldcretornica.plotme_core.bukkit.BukkitUtil;
import com.worldcretornica.plotme_core.bukkit.PlotMe_CorePlugin;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;

public class BukkitPlotDenyListener implements Listener {

    private final PlotMe_CorePlugin plugin;
    private final PlotMeCoreManager manager;

    public BukkitPlotDenyListener() {
        plugin = PlotMe_CorePlugin.getInstance();
        manager = PlotMeCoreManager.getInstance();
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerMove(PlayerMoveEvent event) {
        IPlayer player = plugin.wrapPlayer(event.getPlayer());

        if (manager.isPlotWorld(player) && !player.hasPermission(PermissionNames.ADMIN_BYPASSDENY)) {
            Location to = new Location(player.getWorld(), BukkitUtil.locationToVector(event.getTo()));

            Plot plot = manager.getPlot(to);

            if (plot != null && plot.isDenied(player.getUniqueId())) {
                event.setTo(event.getFrom());
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerJoin(PlayerJoinEvent event) {
        IPlayer player = plugin.wrapPlayer(event.getPlayer());

        if (manager.isPlotWorld(player) && !player.hasPermission(PermissionNames.ADMIN_BYPASSDENY)) {

            Plot plot = manager.getPlot(player);

            if (plot != null && plot.isDenied(player.getUniqueId())) {
                player.setLocation(manager.getPlotHome(plot.getId(), player.getWorld()));
            }
        }
    }
}