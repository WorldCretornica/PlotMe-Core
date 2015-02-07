package com.worldcretornica.plotme_core.sponge.listener;

import com.worldcretornica.plotme_core.PermissionNames;
import com.worldcretornica.plotme_core.Plot;
import com.worldcretornica.plotme_core.PlotMeCoreManager;
import com.worldcretornica.plotme_core.sponge.PlotMe_Sponge;
import com.worldcretornica.plotme_core.sponge.api.SpongeLocation;
import com.worldcretornica.plotme_core.sponge.api.SpongePlayer;
import org.spongepowered.api.event.entity.living.player.PlayerMoveEvent;
import org.spongepowered.api.util.event.Subscribe;

public class SpongePlotDenyListener {

    private final PlotMe_Sponge plugin;
    private PlotMeCoreManager manager;

    public SpongePlotDenyListener(PlotMe_Sponge plotMe_sponge) {
        plugin = plotMe_sponge;

    }

    @Subscribe
    public void onPlayerMove(PlayerMoveEvent event) {
        SpongePlayer player = plugin.wrapPlayer(event.getPlayer());

        if (manager.isPlotWorld(player) && !player.hasPermission(PermissionNames.ADMIN_BYPASSDENY)) {
            SpongeLocation to = new SpongeLocation(event.getNewLocation());

            String idTo = manager.getPlotId(to);

            if (!idTo.isEmpty()) {
                Plot plot = manager.getPlotById(idTo, player);

                if (plot != null && plot.isDeniedInternal(player.getName(), player.getUniqueId())) {
                    event.getPlayer().setLocation(event.getOldLocation());
                }
            }
        }

    }
}
