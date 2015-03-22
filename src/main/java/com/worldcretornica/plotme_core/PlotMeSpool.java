package com.worldcretornica.plotme_core;

import com.worldcretornica.plotme_core.api.IPlotMe_GeneratorManager;
import com.worldcretornica.plotme_core.api.IWorld;

public class PlotMeSpool implements Runnable {

    private final PlotMe_Core plugin;
    private final PlotMeCoreManager plotMeCoreManager;
    private Long[] currentClear;

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
            IWorld world = getPlotToClear().getWorld();
            IPlotMe_GeneratorManager genmanager = plotMeCoreManager.getGenManager(world);

            if (currentClear == null) {
                currentClear = genmanager.clear(world, getPlotToClear().getPlotId(), plugin.getConfig().getInt("NbBlocksPerClearStep"), null);
            } else {
                currentClear = genmanager.clear(world, getPlotToClear().getPlotId(), plugin.getConfig().getInt("NbBlocksPerClearStep"), currentClear);
            }
            if (currentClear == null) {
                if (getPlotToClear().getReason() == ClearReason.Clear) {
                    genmanager.adjustPlotFor(world, getPlotToClear().getPlotId(), true, false, false);
                } else {
                    genmanager.adjustPlotFor(world, getPlotToClear().getPlotId(), false, false, false);
                }
                if (plugin.getServerBridge().isUsingLwc()) {
                    plotMeCoreManager.removeLWC(world, getPlotToClear().getPlotId());
                }
                genmanager.refreshPlotChunks(world, getPlotToClear().getPlotId());

                plotToClear.getRequester()
                        .sendMessage(plugin.C("WordPlot") + " " + getPlotToClear().getPlotId() + " " + plugin.C("WordCleared"));

                plugin.removePlotToClear(getPlotToClear(), taskId);
                plotToClear = null;
            }
        }
    }

    public void setTaskId(int taskId) {
        this.taskId = taskId;
    }

    public PlotToClear getPlotToClear() {
        return plotToClear;
    }

}
