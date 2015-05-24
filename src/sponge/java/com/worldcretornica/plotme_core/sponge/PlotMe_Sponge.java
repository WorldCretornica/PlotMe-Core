package com.worldcretornica.plotme_core.sponge;

import com.google.inject.Inject;
import com.worldcretornica.plotme_core.PermissionNames;
import com.worldcretornica.plotme_core.PlotMe_Core;
import com.worldcretornica.plotme_core.api.BridgeLogger;
import com.worldcretornica.plotme_core.api.IServerBridge;
import com.worldcretornica.plotme_core.sponge.api.SpongePlayer;
import com.worldcretornica.plotme_core.sponge.listener.SpongePlotDenyListener;
import com.worldcretornica.plotme_core.sponge.listener.SpongePlotListener;
import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import ninja.leaping.configurate.hocon.HoconConfigurationLoader;
import ninja.leaping.configurate.loader.ConfigurationLoader;
import org.slf4j.Logger;
import org.spongepowered.api.Game;
import org.spongepowered.api.entity.player.Player;
import org.spongepowered.api.event.Subscribe;
import org.spongepowered.api.event.state.PreInitializationEvent;
import org.spongepowered.api.event.state.ServerStartedEvent;
import org.spongepowered.api.event.state.ServerStoppingEvent;
import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.api.service.ServiceManager;
import org.spongepowered.api.service.ServiceReference;
import org.spongepowered.api.service.config.ConfigDir;
import org.spongepowered.api.service.config.DefaultConfig;
import org.spongepowered.api.service.permission.PermissionService;
import org.spongepowered.api.service.scheduler.AsynchronousScheduler;
import org.spongepowered.api.service.sql.SqlService;
import org.spongepowered.api.text.Texts;
import org.spongepowered.api.util.command.args.CommandElement;
import org.spongepowered.api.util.command.spec.CommandSpec;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

@Plugin(id = "PlotMe", name = "PlotMe-Core", version = "0.17")
public class PlotMe_Sponge {

    private final HashMap<UUID, SpongePlayer> spongePlayerMap = new HashMap<>();

    @Inject
    private Game game;
    @Inject
    private ServiceManager services;

    @Inject
    @ConfigDir(sharedRoot = false)
    private File configDir;
    // The config manager for the mail storage file
    @Inject
    @DefaultConfig(sharedRoot = false)
    private ConfigurationLoader<CommentedConfigurationNode> configLoader;
    private CommentedConfigurationNode pmStorageConfig;
    @Inject
    private Logger logger;
    private PlotMe_Core plotme;
    private IServerBridge serverObjectBuilder;
    private ServiceReference<SqlService> sql;
    private ServiceReference<AsynchronousScheduler> aSync;
    private CommentedConfigurationNode rawConfig;
    private ConfigurationNode fallbackConfig;

    @Subscribe
    public void onInit(PreInitializationEvent event) {
        if (game.getServiceManager().provide(PermissionService.class).isPresent()){
            game.getServiceManager().provide(PermissionService.class).get().
        }
        sql = services.potentiallyProvide(SqlService.class);
        aSync = services.potentiallyProvide(AsynchronousScheduler.class);
        configDir.mkdirs();
        load();
        game.getCommandDispatcher().register(this, CommandSpec.builder()., "plotme");
    }

    private void load() {
        try {
            rawConfig = configLoader.load();
            fallbackConfig = loadDefaultConfiguration();
        } catch (IOException e) {
            e.printStackTrace();
        }
        rawConfig.mergeValuesFrom(fallbackConfig);
    }

    @Subscribe
    public void onEnable(ServerStartedEvent event) {
        game.getEventManager().register(this, new SpongePlotListener(this));
        game.getEventManager().register(this, new SpongePlotDenyListener(this));
        serverObjectBuilder = new SpongeServerBridge(this, new BridgeLogger(logger));

        plotme = new PlotMe_Core(serverObjectBuilder);
        setupCommands();
    }

    private void setupCommands() {
        HashMap<List<String>, CommandSpec> subCommands = new HashMap<>();
        subCommands.put(Arrays.asList("add", "+"), CommandSpec.builder().setDescription(Texts.of("Plot Add")).setExtendedDescription(Texts.of
                ("Plot Add")).setPermission(PermissionNames.USER_ADD).setArguments(CommandElement))
        game.getCommandDispatcher().register(this, CommandSpec.builder().setChildren(), )
    }

    @Subscribe
    public void onStop(ServerStoppingEvent event) {
        // TODO -> stop plugin: save config (if changed), clean up

    }

    public File getConfigDir() {
        return configDir;
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

    ConfigurationNode loadDefaultConfiguration() throws IOException {
        URL defaultConfig = PlotMe_Sponge.class.getResource("default.conf");
        HoconConfigurationLoader loader = HoconConfigurationLoader.builder().setURL(defaultConfig).build();
        return loader.load();

    }
}
