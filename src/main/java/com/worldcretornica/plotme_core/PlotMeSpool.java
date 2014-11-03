package com.worldcretornica.plotme_core;

import com.worldcretornica.plotme_core.api.ILocation;
import com.worldcretornica.plotme_core.api.IWorld;

public class PlotMeSpool implements Runnable {

    private final PlotMe_Core plugin;
    private Long[] currentClear;
    private PlotToClear plottoclear;

    private int taskid;

    private static String T;
    private static String G;
    private static String M;
    private static String k;

    public PlotMeSpool(PlotMe_Core instance, PlotToClear plotToClear) {
        plugin = instance;

        T = plugin.getUtil().C("Unit_1000000000000");
        G = plugin.getUtil().C("Unit_1000000000");
        M = plugin.getUtil().C("Unit_1000000");
        k = plugin.getUtil().C("Unit_1000");

        plottoclear = plotToClear;
    }

    @Override
    public void run() {
        if (getPlotToClear() != null) {
            IWorld world = plugin.getServerBridge().getWorld(getPlotToClear().getWorld());

            if (world != null) {
                if (currentClear == null)
                    currentClear = PlotMe_Core.getGenManager(world).clear(world, getPlotToClear().getPlotId(), plugin.getServerBridge().getConfig().getInt("NbBlocksPerClearStep"), null);
                else {
                    currentClear = PlotMe_Core.getGenManager(world).clear(world, getPlotToClear().getPlotId(), plugin.getServerBridge().getConfig().getInt("NbBlocksPerClearStep"), currentClear);
                }

                ShowProgress();

                if (currentClear == null) {
                    PlotMe_Core.getGenManager(world).adjustPlotFor(world, getPlotToClear().getPlotId(), true, false, false, false);
                    plugin.getPlotMeCoreManager().RemoveLWC(world, getPlotToClear().getPlotId());
                    PlotMe_Core.getGenManager(world).refreshPlotChunks(world, getPlotToClear().getPlotId());

                    Msg(plugin.getUtil().C("WordPlot") + " " + getPlotToClear().getPlotId() + " " + plugin.getUtil().C("WordCleared"));

                    plugin.removePlotToClear(getPlotToClear(), taskid);
                    plottoclear = null;
                }
            } else {
                plugin.removePlotToClear(getPlotToClear(), taskid);
                plottoclear = null;
            }
        }
    }

    private void Msg(String text) {
        getPlotToClear().getCommandSender().sendMessage(text);
    }

    private void ShowProgress() {
        long done = getDoneBlocks();
        long total = getTotalPlotBlocks();
        double percent = done / total * 100;

        String green = plugin.getServerBridge().getColor("GREEN");
        String reset = plugin.getServerBridge().getColor("RESET");
        
        Msg(plugin.getUtil().C("WordPlot") + " " + green + getPlotToClear().getPlotId() + reset + " " + plugin.getUtil().C("WordIn") + " "
                + green + getPlotToClear().getWorld() + reset + " "
                    + plugin.getUtil().C("WordIs") + " " + green + (double) Math.round(percent * 10) / 10 + "% " + reset + plugin.getUtil().C("WordCleared")
                + " (" + green + format(done) + reset + "/" + green + format(total) + reset + " " + plugin.getUtil().C("WordBlocks") + ")");
    }

    private long getTotalPlotBlocks() {
        IWorld world = plugin.getServerBridge().getWorld(getPlotToClear().getWorld());
        ILocation bottom = PlotMe_Core.getGenManager(world).getPlotBottomLoc(world, getPlotToClear().getPlotId());
        ILocation top = PlotMe_Core.getGenManager(world).getPlotTopLoc(world, getPlotToClear().getPlotId());

        return (top.getBlockX() - bottom.getBlockX() + 1) * (top.getBlockY() - bottom.getBlockY() + 1) * (top.getBlockZ() - bottom.getBlockZ() + 1);
    }

    private long getDoneBlocks() {
        return currentClear[3];
    }

    private static String format(Long count) {
        double buffer;

        if (count > 1000000000000L) {
            buffer = (double) count / 1000000000000L;
            buffer = (double) Math.round(buffer * 10) / 10;
            return buffer + T;
        }
        if (count > 1000000000) {
            buffer = (double) count / 1000000000;
            buffer = (double) Math.round(buffer * 10) / 10;
            return buffer + G;
        }
        if (count > 1000000) {
            buffer = (double) count / 1000000;
            buffer = (double) Math.round(buffer * 10) / 10;
            return buffer + M;
        }
        if (count > 1000) {
            buffer = (double) count / 1000;
            buffer = (double) Math.round(buffer * 10) / 10;
            return buffer + k;
        }
        return count.toString();
    }

    public PlotToClear getPlotToClear() {
        return plottoclear;
    }

    public void setTaskId(int taskid) {
        this.taskid = taskid;
    }
}
