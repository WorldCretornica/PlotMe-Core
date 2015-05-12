package com.worldcretornica.plotme_core;

import com.worldcretornica.plotme_core.api.IPlotMe_GeneratorManager;

public class PlotMeSpool implements Runnable {

    private final PlotMe_Core plugin;
    private final PlotMeCoreManager plotMeCoreManager;
    private long[] currentClear;

    private PlotToClear plotToClear;
    private int taskId;

    public PlotMeSpool(PlotMe_Core instance, PlotToClear plotToClear) {
        plugin = instance;
        plotMeCoreManager = PlotMeCoreManager.getInstance();
        this.plotToClear = plotToClear;
    }

    @Override
    public void run() {
        if (getPlotToClear() != null) {
            IPlotMe_GeneratorManager genmanager = plotMeCoreManager.getGenManager(getPlotToClear().getWorld());

            if (currentClear == null) {
                currentClear = genmanager.clear(getPlotToClear().getPlotId(), plugin.getConfig().getInt("NbBlocksPerClearStep"), null);
            } else {
                currentClear = genmanager.clear(getPlotToClear().getPlotId(), plugin.getConfig().getInt("NbBlocksPerClearStep"), currentClear);
            }
            if (currentClear == null) {
                if (getPlotToClear().getReason() == ClearReason.Clear) {
                    genmanager.adjustPlotFor(getPlotToClear().getPlot(), true, false, false);
                } else {
                    genmanager.adjustPlotFor(getPlotToClear().getPlot(), false, false, false);
                }
                genmanager.refreshPlotChunks(getPlotToClear().getPlotId());

                assert plotToClear != null;
                plotToClear.getRequester().sendMessage(plugin.C("WordPlot") + " " + getPlotToClear().getPlotId() + " " + plugin.C("WordCleared"));

                plugin.removePlotToClear(getPlotToClear(), taskId);
                plotToClear = null;
            }
        }
    }

    public void setTaskId(int taskId) {
        this.taskId = taskId;
    }

    private PlotToClear getPlotToClear() {
        return plotToClear;
    }
}