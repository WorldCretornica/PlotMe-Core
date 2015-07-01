package com.worldcretornica.plotme_core;

import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import com.worldcretornica.plotme_core.api.event.PlotResetEvent;

import java.sql.Date;
import java.util.Calendar;
import java.util.Collection;

public class PlotExpireCleanup implements Runnable {

    private final PlotMe_Core plugin;

    public PlotExpireCleanup(PlotMe_Core plugin) {

        this.plugin = plugin;
    }

    @Override
    public void run() {
        plugin.getLogger().info("Beginning Expire Cleanup Task.");
        Collection<Plot> filter = Collections2.filter(plugin.getSqlManager().getPlots(), new Predicate<Plot>() {
            @Override public boolean apply(Plot plot) {
                Date temp = new Date(Calendar.getInstance().getTime().getTime());
                if (plot.getExpiredDate() != null) {
                    if (temp.after(plot.getExpiredDate())) {
                        return true;
                    }
                }
                return false;
            }
        });
        for (final Plot plot : filter) {
            PlotResetEvent event = new PlotResetEvent(plot, null);
            plugin.getEventBus().post(event);
            if (!event.isCancelled()) {
                plugin.getServerBridge().runTask(new Runnable() {
                    @Override public void run() {
                        PlotMeCoreManager.getInstance().clear(plot, null, ClearReason.Expired);
                        PlotMeCoreManager.getInstance().deletePlot(plot);
                    }
                });
            }
        }
        plugin.getLogger().info(filter.size() + " Expired Plots were deleted");
    }
}
