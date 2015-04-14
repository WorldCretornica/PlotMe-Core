package com.worldcretornica.plotme_core.sponge.listener;

import com.worldcretornica.plotme_core.PlotMeCoreManager;
import com.worldcretornica.plotme_core.PlotMe_Core;
import com.worldcretornica.plotme_core.sponge.PlotMe_Sponge;

public class SpongePlotListener {

    private final PlotMe_Sponge plugin;
    private final PlotMe_Core api;
    private final PlotMeCoreManager manager;

    public SpongePlotListener(PlotMe_Sponge instance) {
        //noinspection ConstantConditions
        api = instance.getAPI();
        this.plugin = instance;
        manager = PlotMeCoreManager.getInstance();
    }

}
