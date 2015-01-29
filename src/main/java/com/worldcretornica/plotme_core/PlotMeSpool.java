package com.worldcretornica.plotme_core;

import com.worldcretornica.plotme_core.api.IWorld;

public class PlotMeSpool implements Runnable {

    private final PlotMe_Core plugin;
    private Long[] currentClear;

    private PlotToClear plottoclear;

    public PlotMeSpool(PlotMe_Core instance, PlotToClear plotToClear) {
        plugin = instance;
        plottoclear = plotToClear;
    }

    @Override
    public void run() {
        if (getPlotToClear() != null) {
            IWorld world = plugin.getServerBridge().getWorld(getPlotToClear().getWorld());
            PlotMeCoreManager plotMeCoreManager = PlotMeCoreManager.getInstance();

            if (world != null) {
                if (currentClear == null) {
                    currentClear = plotMeCoreManager.getGenManager(world)
                                    .clear(world, getPlotToClear().getPlotId(), plugin.getServerBridge().getConfig().getInt("NbBlocksPerClearStep"), null);
                } else {
                    currentClear = plotMeCoreManager.getGenManager(world)
                                    .clear(world, getPlotToClear().getPlotId(), plugin.getServerBridge().getConfig().getInt("NbBlocksPerClearStep"), currentClear);
                }

                if (currentClear == null) {
                    if (getPlotToClear().getReason() == ClearReason.Clear) {
                        plotMeCoreManager.getGenManager(world).adjustPlotFor(world, getPlotToClear().getPlotId(), false, false, false, false);
                    } else {
                        plotMeCoreManager.getGenManager(world).adjustPlotFor(world, getPlotToClear().getPlotId(), true, false, false, false);
                    }
                    if (plugin.getServerBridge().getUsingLwc()) {
                        plotMeCoreManager.removeLWC(world, getPlotToClear().getPlotId());
                    }
                    plotMeCoreManager.getGenManager(world).refreshPlotChunks(world, getPlotToClear().getPlotId());

                    plottoclear.getRequester().sendMessage(plugin.getUtil().C("WordPlot") + " " + getPlotToClear().getPlotId() + " " + plugin.getUtil().C("WordCleared"));

                    plugin.removePlotToClear(getPlotToClear(), plugin.getClearTaskID());
                    plottoclear = null;
                }
            } else {
                plugin.removePlotToClear(getPlotToClear(), plugin.getClearTaskID());
                plottoclear = null;
            }
        }
    }

    public PlotToClear getPlotToClear() {
        return plottoclear;
    }

}
