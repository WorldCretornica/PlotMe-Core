package com.worldcretornica.plotme_core.api;

import com.google.common.eventbus.EventBus;

public class PlotMeAPI {

    EventBus getEventBus() {
        return new EventBus();
    }

    String getPlotMeVersion() {
        return "0.17-Snapshot"; //Todo Update this with Maven/Gradle build information
    }

    


}
