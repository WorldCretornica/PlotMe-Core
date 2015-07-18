package com.worldcretornica.plotme_core;

import com.worldcretornica.plotme_core.api.IPlotMe_GeneratorManager;
import com.worldcretornica.plotme_core.utils.ClearEntry;

import java.util.ArrayDeque;

public class PlotMeSpool implements Runnable {

    public static ArrayDeque<ClearEntry> clearList = new ArrayDeque<>();
    private final PlotMe_Core plugin;


    public PlotMeSpool(PlotMe_Core plotMe_core) {
        this.plugin = plotMe_core;
    }

    @Override
    public void run() {
        ClearEntry first = clearList.getFirst();
        IPlotMe_GeneratorManager genmanager = PlotMeCoreManager.getInstance().getGenManager(first.getPlot().getWorld());
        genmanager.clear(first.getPlot().getPlotBottomLoc(), first.getPlot().getPlotTopLoc(), first.getPlot().getId(), first);
        if (first.chunkqueue.isEmpty()) {
            if (first.getReason().equals(ClearReason.Clear)) {
                genmanager.adjustPlotFor(first.getPlot(), true, false, false);
            } else {
                genmanager.adjustPlotFor(first.getPlot(), false, false, false);
            }
            clearList.removeFirst();
            if (first.getSender() != null) {
                first.getSender().sendMessage(plugin.C("WordPlot") + " " + first.getPlot().getId().getID() + " " + plugin.C("WordCleared"));
            }
        } else {
            first.chunkqueue.poll().run();
        }


    }


}