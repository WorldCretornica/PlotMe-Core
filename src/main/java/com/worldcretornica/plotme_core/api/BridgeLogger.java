package com.worldcretornica.plotme_core.api;

import java.util.logging.LogRecord;
import java.util.logging.Logger;

public class BridgeLogger extends Logger {

    private final org.slf4j.Logger handle;

    public BridgeLogger(org.slf4j.Logger logger) {
        super(logger.getName(), null);
        this.handle = logger;
    }

    public org.slf4j.Logger getHandle() {
        return handle;
    }

    @Override
    public void log(LogRecord record) {
        record.setMessage("[PlotMe]");
        super.log(record);
    }
}