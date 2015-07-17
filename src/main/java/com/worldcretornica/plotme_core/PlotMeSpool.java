package com.worldcretornica.plotme_core;

import com.worldcretornica.plotme_core.api.ICommandSender;
import com.worldcretornica.plotme_core.api.IPlotMe_GeneratorManager;

public class PlotMeSpool implements Runnable {

    private final PlotMe_Core plugin;
    private final Plot plot;
    private final ClearReason reason;
    private final ICommandSender sender;


    public PlotMeSpool(PlotMe_Core plotMe_core, Plot plot, ClearReason reason, ICommandSender sender) {
        this.plugin = plotMe_core;
        this.plot = plot;
        this.reason = reason;
        this.sender = sender;
    }

    @Override
    public void run() {
        if (sender != null) {
            sender.sendMessage("Clearing Plot " + plot.getId().getID());
        }
        IPlotMe_GeneratorManager genmanager = PlotMeCoreManager.getInstance().getGenManager(plot.getWorld());

        genmanager.clear(plot.getPlotBottomLoc(), plot.getPlotTopLoc());
        if (reason.equals(ClearReason.Clear)) {
            genmanager.adjustPlotFor(plot, true, false, false);
        } else {
            genmanager.adjustPlotFor(plot, false, false, false);
        }
        if (sender != null) {
            sender.sendMessage(plugin.C("WordPlot") + " " + plot.getId().getID() + " " + plugin.C("WordCleared"));
        }

    }


}