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
        
        this.plottoclear = plotToClear;
    }

    @Override
    public void run() {
        if (this.getPlotToClear() != null) {
            IWorld w = plugin.getServerBridge().getWorld(this.getPlotToClear().getWorld());

            if (w != null) {
                if (currentClear == null)
                    currentClear = plugin.getGenManager(w).clear(w, getPlotToClear().getPlotId(), plugin.getServerBridge().getConfig().getInt("NbBlocksPerClearStep"), true, null);
                else {
                    currentClear = plugin.getGenManager(w).clear(w, getPlotToClear().getPlotId(), plugin.getServerBridge().getConfig().getInt("NbBlocksPerClearStep"), false, currentClear);
                }

                ShowProgress();

                if (currentClear == null) {
                    plugin.getGenManager(getPlotToClear().getWorld()).adjustPlotFor(w, getPlotToClear().getPlotId(), true, false, false, false);
                    plugin.getPlotMeCoreManager().RemoveLWC(w, getPlotToClear().getPlotId());
                    plugin.getGenManager(getPlotToClear().getWorld()).refreshPlotChunks(w, getPlotToClear().getPlotId());

                    Msg(plugin.getUtil().C("WordPlot") + " " + getPlotToClear().getPlotId() + " " + plugin.getUtil().C("WordCleared"));

                    plugin.removePlotToClear(this.getPlotToClear(), this.taskid);
                    plottoclear = null;
                }
            } else {
                plugin.removePlotToClear(this.getPlotToClear(), this.taskid);
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
        double percent = (double) done / (double) total * 100;

        String green = plugin.getServerBridge().getColor("GREEN");
        String reset = plugin.getServerBridge().getColor("RESET");
        
        Msg(plugin.getUtil().C("WordPlot") + " " + green + getPlotToClear().getPlotId() + reset + " " + plugin.getUtil().C("WordIn") + " "
                + green + getPlotToClear().getWorld() + reset + " "
                    + plugin.getUtil().C("WordIs") + " " + green + (double) Math.round(percent * 10) / 10 + "% " + reset + plugin.getUtil().C("WordCleared")
                + " (" + green + format(done) + reset + "/" + green + format(total) + reset + " " + plugin.getUtil().C("WordBlocks") + ")");
    }

    private long getTotalPlotBlocks() {
        IWorld w = plugin.getServerBridge().getWorld(getPlotToClear().getWorld());
        ILocation bottom = plugin.getGenManager(w).getPlotBottomLoc(w, getPlotToClear().getPlotId());
        ILocation top = plugin.getGenManager(w).getPlotTopLoc(w, getPlotToClear().getPlotId());

        return (top.getBlockX() - bottom.getBlockX() + 1) * (top.getBlockY() - bottom.getBlockY() + 1) * (top.getBlockZ() - bottom.getBlockZ() + 1);
    }

    private long getDoneBlocks() {
        return currentClear[3];
    }

    private String format(Long count) {
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
        } else if (count > 1000000) {
            buffer = (double) count / 1000000;
            buffer = (double) Math.round(buffer * 10) / 10;
            return buffer + M;
        } else if (count > 1000) {
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
