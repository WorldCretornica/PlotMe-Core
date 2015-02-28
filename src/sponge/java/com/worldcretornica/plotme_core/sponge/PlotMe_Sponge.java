package com.worldcretornica.plotme_core.sponge;

import com.worldcretornica.plotme_core.PlotMe_Core;
import com.worldcretornica.plotme_core.api.IServerBridge;
import com.worldcretornica.plotme_core.sponge.api.SpongePlayer;
import org.spongepowered.api.Game;
import org.spongepowered.api.entity.player.Player;
import org.spongepowered.api.event.state.PreInitializationEvent;
import org.spongepowered.api.event.state.ServerStartedEvent;
import org.spongepowered.api.event.state.ServerStoppingEvent;
import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.api.util.event.Subscribe;

import java.util.HashMap;
import java.util.UUID;

@Plugin(id = "plotme", name = "PlotMe", version = "0.16")
public class PlotMe_Sponge {

    private final HashMap<UUID, SpongePlayer> spongePlayerMap = new HashMap<>();
    Game game;
    private PlotMe_Core plotme;
    private IServerBridge serverObjectBuilder;

    @Subscribe
    public void onInit(PreInitializationEvent event) {
        // TODO -> start plugin: load config, assign variables

    }

    @Subscribe
    public void onEnable(ServerStartedEvent event) {
        //game.getEventManager().register(this, new SpongePlotListener(this));
        //game.getEventManager().register(this, new SpongePlotDenyListener(this));

        serverObjectBuilder = new SpongeServerBridge(this);

        SpongeAbstractSchematicUtil schematicutil = new SchematicUtil(this); //TODO
        plotme = new PlotMe_Core(serverObjectBuilder, null);
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
