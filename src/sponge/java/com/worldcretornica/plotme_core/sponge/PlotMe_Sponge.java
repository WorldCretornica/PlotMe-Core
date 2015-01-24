package com.worldcretornica.plotme_core.sponge;

import com.worldcretornica.plotme_core.PlotMe_Core;
import com.worldcretornica.plotme_core.api.IServerBridge;
import org.slf4j.Logger;
import org.spongepowered.api.Game;
import org.spongepowered.api.event.state.PreInitializationEvent;
import org.spongepowered.api.event.state.ServerStartedEvent;
import org.spongepowered.api.event.state.ServerStoppingEvent;
import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.api.util.event.Subscribe;

@Plugin(id = "plotme", name = "PlotMe", version = "0.16")
public class PlotMe_Sponge {

    Game game;
    
    private Logger logger;
    private PlotMe_Core plotme;
    private IServerBridge serverObjectBuilder;
    
    @Subscribe
    public void onInit(PreInitializationEvent event) {
        // TODO -> start plugin: load config, assign variables
        
        logger = event.getPluginLog();
        logger.info("Plugin enabled.");
    }

    @Subscribe
    public void onEnable(ServerStartedEvent event) {
        game.getEventManager().register(this, new SpongePlotListener(this));
        
        serverObjectBuilder = new SpongeServerBridge(this);
        plotme = new PlotMe_Core(serverObjectBuilder);
    }
    
    @Subscribe
    public void onStop(ServerStoppingEvent event) {
        // TODO -> stop plugin: save config (if changed), clean up
        logger.info("Plugin disabled.");
    }
    
    public PlotMe_Core getAPI() {
        return plotme;
    }
    
    public IServerBridge getServerObjectBuilder() {
        return serverObjectBuilder;
    }
}
