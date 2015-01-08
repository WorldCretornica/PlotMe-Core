package com.worldcretornica.plotme_core.sponge;

import org.spongepowered.api.event.player.PlayerInteractEvent;
import org.spongepowered.api.util.event.Subscribe;

/**
 * Created by Matthew on 1/7/2015.
 */
public class SpongePlotListener {

    private final PlotMe_Sponge plugin;

    public SpongePlotListener(PlotMe_Sponge plugin) {
        this.plugin = plugin;
    }

    @Subscribe
    public void onBlockBreak() {
        //Not yet able to implement.
    }

    @Subscribe
    public void onBlockPlace() {
        //Not yet able to implement.
    }

    @Subscribe
    public void onPlayerInteract(PlayerInteractEvent event) {
    }
}
