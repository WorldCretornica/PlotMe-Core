package com.worldcretornica.plotme_core.sponge;

import org.spongepowered.api.Game;
import org.spongepowered.api.event.state.ServerStartedEvent;
import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.api.util.event.Subscribe;

/**
 * Created by Matthew on 12/30/2014.
 */
@Plugin(id = "plotme", name = "PlotMe", version = "0.16")
public class PlotMe_Sponge {

    public Game game;

    @Subscribe
    public void onEnable(ServerStartedEvent event) {
        game.getEventManager().register(this, new SpongePlotListener(this));
    }
}
