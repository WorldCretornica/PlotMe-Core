package com.worldcretornica.plotme_core.bukkit;

import java.io.IOException;

import org.bukkit.plugin.java.JavaPlugin;
import org.mcstats.Metrics;
import org.mcstats.Metrics.Graph;

import com.worldcretornica.plotme_core.PlotMe_Core;
import com.worldcretornica.plotme_core.api.IServerObjectBuilder;
import com.worldcretornica.plotme_core.bukkit.api.BukkitServerObjectBuilder;


public class PlotMe_CorePlugin extends JavaPlugin {

    private PlotMe_Core plotme;
    private IServerObjectBuilder serverObjectBuilder = null;

    @Override
    public void onDisable() {
        plotme.disable();
    }

    @Override
    public void onEnable() {
        serverObjectBuilder = new BukkitServerObjectBuilder(this);
        plotme = new PlotMe_Core(serverObjectBuilder);
        plotme.enable();
        doMetric();
    }
    
    public PlotMe_Core getAPI() {
        return plotme;
    }
    
    public IServerObjectBuilder getServerObjectBuilder() {
        return serverObjectBuilder;
    }
    
    private void doMetric() {
        try {
            Metrics metrics = new Metrics(this);

            Graph graphNbWorlds = metrics.createGraph("Plot worlds");

            graphNbWorlds.addPlotter(new Metrics.Plotter("Number of plot worlds") {
                @Override
                public int getValue() {
                    return plotme.getPlotMeCoreManager().getPlotMaps().size();
                }
            });

            graphNbWorlds.addPlotter(new Metrics.Plotter("Average Plot size") {
                @Override
                public int getValue() {

                    if (!plotme.getPlotMeCoreManager().getPlotMaps().isEmpty()) {
                        int totalplotsize = 0;

                        for (String s : plotme.getPlotMeCoreManager().getPlotMaps().keySet()) {
                            if (plotme.getPlotMeCoreManager().getGenMan(s) != null) {
                                if (plotme.getPlotMeCoreManager().getGenMan(s).getPlotSize(s) != 0) {
                                    totalplotsize += plotme.getPlotMeCoreManager().getGenMan(s).getPlotSize(s);
                                }
                            }
                        }

                        return totalplotsize / plotme.getPlotMeCoreManager().getPlotMaps().size();
                    } else {
                        return 0;
                    }
                }
            });

            graphNbWorlds.addPlotter(new Metrics.Plotter("Number of plots") {
                @Override
                public int getValue() {
                    int nbplot = 0;

                    for (String map : plotme.getPlotMeCoreManager().getPlotMaps().keySet()) {
                        nbplot += plotme.getSqlManager().getPlotCount(map);
                    }

                    return nbplot;
                }
            });

            metrics.start();
        } catch (IOException e) {
            // Failed to submit the stats :-(
        }
    }
}
