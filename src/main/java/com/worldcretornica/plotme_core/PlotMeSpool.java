package com.worldcretornica.plotme_core;

import com.worldcretornica.plotme_core.api.IWorld;

public class PlotMeSpool implements Runnable {

    private final PlotMe_Core plugin;
    private Long[] currentClear;

    private PlotToClear plottoclear;
    private int taskId;

    public PlotMeSpool(PlotMe_Core instance, PlotToClear plotToClear) {
        plugin = instance;
        plottoclear = plotToClear;
    }

    /*private static String format(long count) {
        double buffer;

        if (count > 1000000000000L) {
            buffer = count / 1000000000000L;
            buffer = Math.round(buffer * 10) / 10;
            return buffer + "T";
        }
        if (count > 1000000000) {
            buffer = count / 1000000000;
            buffer = Math.round(buffer * 10) / 10;
            return buffer + "G";
        }
        if (count > 1000000) {
            buffer = count / 1000000;
            buffer = Math.round(buffer * 10) / 10;
            return buffer + "M";
        }
        if (count > 1000) {
            buffer = count / 1000;
            buffer = Math.round(buffer * 10) / 10;
            return buffer + "k";
        }
        return String.valueOf(count);
    }*/

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
                    PlotMeCoreManager.getGenManager(world).adjustPlotFor(world, getPlotToClear().getPlotId(), true, false, false, false);
                    if (plugin.getServerBridge().getUsinglwc()) {
                        plugin.getPlotMeCoreManager().removeLWC(world, getPlotToClear().getPlotId());
                    }
                    PlotMeCoreManager.getGenManager(world).refreshPlotChunks(world, getPlotToClear().getPlotId());

                    plottoclear.getRequester().sendMessage(plugin.getUtil().C("WordPlot") + " " + getPlotToClear().getPlotId() + " " + plugin.getUtil().C("WordCleared"));

                    plugin.removePlotToClear(getPlotToClear(), taskId);
                    plottoclear = null;
                }
            } else {
                plugin.removePlotToClear(getPlotToClear(), taskId);
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

    public void setTaskId(int taskId) {
        this.taskId = taskId;
    }
}
