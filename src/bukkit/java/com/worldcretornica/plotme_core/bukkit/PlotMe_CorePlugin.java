package com.worldcretornica.plotme_core.bukkit;

import com.worldcretornica.plotme_core.PlotMeCoreManager;
import com.worldcretornica.plotme_core.PlotMe_Core;
import com.worldcretornica.plotme_core.api.IEntity;
import com.worldcretornica.plotme_core.api.IPlayer;
import com.worldcretornica.plotme_core.api.IPlotMe_GeneratorManager;
import com.worldcretornica.plotme_core.api.IServerBridge;
import com.worldcretornica.plotme_core.bukkit.api.*;

import org.bukkit.Bukkit;
import org.bukkit.entity.*;
import org.bukkit.plugin.java.*;
import org.mcstats.Metrics;
import org.mcstats.Metrics.Graph;

import java.io.*;
import java.util.HashMap;
import java.util.UUID;


public class PlotMe_CorePlugin extends JavaPlugin {

    private final HashMap<UUID, BukkitPlayer> bukkitPlayerMap = new HashMap<>();
    private PlotMe_Core plotme;
    private IServerBridge serverObjectBuilder;

    @Override
    public void onDisable() {
        getAPI().disable();
        getBukkitPlayerMap().clear();
    }

    @Override
    public void onEnable() {
        serverObjectBuilder = new BukkitServerBridge(this);
        
        AbstractSchematicUtil schematicutil = null;
                
        if (Bukkit.getVersion().contains("1.7")) {
            schematicutil = new com.worldcretornica.plotme_core.bukkit.v1_7.SchematicUtil(this);
        } else if (Bukkit.getVersion().contains("1.8")) {
            schematicutil = new com.worldcretornica.plotme_core.bukkit.v1_8.SchematicUtil(this);
        } else {
            getLogger().warning("This MC version is not supported yet, trying latest version!");
            schematicutil = new com.worldcretornica.plotme_core.bukkit.v1_8.SchematicUtil(this);
        }
        
        plotme = new PlotMe_Core(serverObjectBuilder, schematicutil);
        getAPI().enable();
        doMetric();
    }

    public PlotMe_Core getAPI() {
        return plotme;
    }

    public IServerBridge getServerObjectBuilder() {
        return serverObjectBuilder;
    }

    /**
     * Metrics
     */
    private void doMetric() {
        try {
            Metrics metrics = new Metrics(this);
            final PlotMeCoreManager manager = PlotMeCoreManager.getInstance();

            Graph graphNbWorlds = metrics.createGraph("Plot worlds");

            graphNbWorlds.addPlotter(new Metrics.Plotter("Number of plot worlds") {
                @Override
                public int getValue() {
                    return manager.getPlotMaps().size();
                }
            });

            graphNbWorlds.addPlotter(new Metrics.Plotter("Average Plot size") {
                @Override
                public int getValue() {

                    if (!manager.getPlotMaps().isEmpty()) {
                        int totalPlotSize = 0;

                        for (String plotter : manager.getPlotMaps().keySet()) {
                            IPlotMe_GeneratorManager genmanager = plotme.getGenManager(plotter);
                            if (genmanager != null) {
                                if (genmanager.getPlotSize(plotter) != 0) {
                                    totalPlotSize += genmanager.getPlotSize(plotter);
                                }
                            }
                        }

                        return totalPlotSize / manager.getPlotMaps().size();
                    } else {
                        return 0;
                    }
                }
            });

            graphNbWorlds.addPlotter(new Metrics.Plotter("Number of plots") {
                @Override
                public int getValue() {
                    int nbPlot = 0;

                    for (String map : manager.getPlotMaps().keySet()) {
                        nbPlot += (int) getAPI().getSqlManager().getPlotCount(map);
                    }

                    return nbPlot;
                }
            });

            metrics.start();
        } catch (IOException e) {
            // Failed to submit the stats :-(
        }
    }


    /**
     * Gets a cache of BukkitPlayers for use in commands. Reducing the number of BukkitPlayer Objects being created. Players are removed on logoff.
     * @param player {@link Player} from Bukkit
     * @return a BukkitPlayer for the player given
     */
    public IPlayer wrapPlayer(Player player) {
        if (bukkitPlayerMap.containsKey(player.getUniqueId())) {
            return bukkitPlayerMap.get(player.getUniqueId());
        } else {
            BukkitPlayer bukkitplayer = new BukkitPlayer(player);
            bukkitPlayerMap.put(player.getUniqueId(), bukkitplayer);
            return bukkitplayer;
        }
    }

    public IEntity wrapEntity(Entity entity) {
        if (entity instanceof Player) {
            return wrapPlayer((Player) entity);
        }
        return new BukkitEntity(entity);
    }

    public void removePlayer(UUID playerUUID) {
        bukkitPlayerMap.remove(playerUUID);
    }

    public HashMap<UUID, BukkitPlayer> getBukkitPlayerMap() {
        return bukkitPlayerMap;
    }

}
