package com.worldcretornica.plotme_core.sponge;

import com.google.inject.Inject;
import com.worldcretornica.configuration.ConfigAccessor;
import com.worldcretornica.plotme_core.PlotMe_Core;
import com.worldcretornica.plotme_core.api.BridgeLogger;
import com.worldcretornica.plotme_core.api.IServerBridge;
import com.worldcretornica.plotme_core.sponge.api.SpongePlayer;
import com.worldcretornica.plotme_core.sponge.listener.SpongePlotDenyListener;
import com.worldcretornica.plotme_core.sponge.listener.SpongePlotListener;
import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import ninja.leaping.configurate.hocon.HoconConfigurationLoader;
import ninja.leaping.configurate.loader.ConfigurationLoader;
import org.slf4j.Logger;
import org.spongepowered.api.Game;
import org.spongepowered.api.entity.player.Player;
import org.spongepowered.api.event.state.PreInitializationEvent;
import org.spongepowered.api.event.state.ServerStartedEvent;
import org.spongepowered.api.event.state.ServerStoppingEvent;
import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.api.service.config.ConfigDir;
import org.spongepowered.api.util.event.Subscribe;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.UUID;

@Plugin(id = "PlotMeCore", name = "PlotMe", version = "0.17")
public class PlotMe_Sponge {

    private final HashMap<UUID, SpongePlayer> spongePlayerMap = new HashMap<>();

    @Inject
    private Game game;

    @Inject
    @ConfigDir(sharedRoot = false)
    private File configDir;
    // The config manager for the mail storage file
    private ConfigurationLoader<CommentedConfigurationNode> pmConfigLoader;
    private CommentedConfigurationNode pmStorageConfig;
    @Inject
    private Logger logger;
    private PlotMe_Core plotme;
    private IServerBridge serverObjectBuilder;
    private ConfigAccessor configFile;
    private ConfigAccessor captionFile;

    @Subscribe
    public void onInit(PreInitializationEvent event) {
        configFile = new ConfigAccessor(configDir, "config.conf");
        captionFile = new ConfigAccessor(configDir, "captions.conf");

        File pmStorage = new File(configDir, "config.conf");
        this.pmConfigLoader = HoconConfigurationLoader.builder().setFile(pmStorage).build();
        try {
            pmStorageConfig = pmConfigLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (!configDir.isDirectory()) {
            configDir.mkdirs();
        }
        if (!pmStorage.isFile()) {
            try {
                pmStorage.createNewFile();
            } catch (IOException e) {
                logger.error("Error creating config files.");
            }
        }
    }

    @Subscribe
    public void onEnable(ServerStartedEvent event) {
        game.getEventManager().register(this, new SpongePlotListener(this));
        game.getEventManager().register(this, new SpongePlotDenyListener(this));
        serverObjectBuilder = new SpongeServerBridge(this, new BridgeLogger(logger));

        plotme = new PlotMe_Core(serverObjectBuilder, new SchematicUtil(this), configDir);
    }

    @Subscribe
    public void onStop(ServerStoppingEvent event) {
        // TODO -> stop plugin: save config (if changed), clean up

    }

    public PlotMe_Core getAPI() {
        return plotme;
    }

    public Game getGame() {
        return game;
    }

    public IServerBridge getServerObjectBuilder() {
        return serverObjectBuilder;
    }

    public SpongePlayer wrapPlayer(Player player) {
        if (spongePlayerMap.containsKey(player.getUniqueId())) {
            return spongePlayerMap.get(player.getUniqueId());
        } else {
            SpongePlayer spongePlayer = new SpongePlayer(player);
            spongePlayerMap.put(player.getUniqueId(), spongePlayer);
            return spongePlayer;
        }

    }
}
