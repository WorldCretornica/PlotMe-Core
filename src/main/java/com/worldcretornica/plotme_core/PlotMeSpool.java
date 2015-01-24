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

            if (world != null) {
                if (currentClear == null) {
                    currentClear =
                            PlotMeCoreManager.getGenManager(world)
                                    .clear(world, getPlotToClear().getPlotId(), plugin.getServerBridge().getConfig().getInt("NbBlocksPerClearStep"),
                                           null);
                } else {
                    currentClear =
                            PlotMeCoreManager.getGenManager(world)
                                    .clear(world, getPlotToClear().getPlotId(), plugin.getServerBridge().getConfig().getInt("NbBlocksPerClearStep"),
                                           currentClear);
                }

                //showProgress();

                if (currentClear == null) {
                    if (getPlotToClear().getReason() == ClearReason.Clear) {
                        PlotMeCoreManager.getGenManager(world).adjustPlotFor(world, getPlotToClear().getPlotId(), false, false, false, false);
                    } else {
                        PlotMeCoreManager.getGenManager(world).adjustPlotFor(world, getPlotToClear().getPlotId(), true, false, false, false);
                    }
                    if (plugin.getServerBridge().getUsinglwc()) {
                        plugin.getPlotMeCoreManager().removeLWC(world, getPlotToClear().getPlotId());
                    }
                    PlotMeCoreManager.getGenManager(world).refreshPlotChunks(world, getPlotToClear().getPlotId());

                    plottoclear.getRequester().sendMessage(
                            plugin.getUtil().C("WordPlot") + " " + getPlotToClear().getPlotId() + " " + plugin.getUtil().C("WordCleared"));

                    plugin.removePlotToClear(getPlotToClear(), plugin.getClearTaskID());
                    plottoclear = null;
                }
            } else {
                plugin.removePlotToClear(getPlotToClear(), plugin.getClearTaskID());
                plottoclear = null;
            }
        }
    }

    /*
    private void showProgress() {
        long done = getDoneBlocks();
        long total = getTotalPlotBlocks();
        double percent = (done / total * 100);
        plugin.getLogger().info(
                plugin.getUtil().C("WordPlot") + " " + getPlotToClear().getPlotId() + " " + plugin.getUtil().C("WordIn") + " " + getPlotToClear()
                        .getWorld() + " " + plugin.getUtil().C("WordIs") + " " + Math.round(percent * 10) / 10 +
                "% " + plugin.getUtil().C("WordCleared") + " (" + format(done) + "/" + format(total) + " " + plugin.getUtil().C("WordBlocks") + ")");
    }

    private long getTotalPlotBlocks() {
        IWorld world = plugin.getServerBridge().getWorld(getPlotToClear().getWorld());
        ILocation bottom = PlotMeCoreManager.getGenManager(world).getPlotBottomLoc(world, getPlotToClear().getPlotId());
        ILocation top = PlotMeCoreManager.getGenManager(world).getPlotTopLoc(world, getPlotToClear().getPlotId());

        return (top.getBlockX() - bottom.getBlockX() + 1) * (top.getBlockY() - bottom.getBlockY() + 1) * (top.getBlockZ() - bottom.getBlockZ() + 1);
    }

    private long getDoneBlocks() {
        return currentClear[3];
    }*/


    public PlotToClear getPlotToClear() {
        return plottoclear;
    }

}
