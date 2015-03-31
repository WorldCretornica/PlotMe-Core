package com.worldcretornica.plotme_core.api;

import com.google.common.eventbus.EventBus;

public class PlotMeAPI {

    EventBus getEventBus() {
        return new EventBus();
    }

}
