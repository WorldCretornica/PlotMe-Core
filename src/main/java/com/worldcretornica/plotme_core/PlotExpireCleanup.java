package com.worldcretornica.plotme_core;

import com.worldcretornica.plotme_core.api.IWorld;
import com.worldcretornica.plotme_core.api.event.PlotResetEvent;

public class PlotExpireCleanup implements Runnable {

    private final PlotMe_Core plugin;

    public PlotExpireCleanup(PlotMe_Core plugin) {

        this.plugin = plugin;
    }

    @Override
    public void run() {
        plugin.getLogger().info("Beginning Expire Cleanup Task.");
        int i = 0;
        for (IWorld world : PlotMeCoreManager.getInstance().getPlotMaps().keySet()) {
            if (PlotMeCoreManager.getInstance().getMap(world).getDaysToExpiration() == 0) {
                break;
            }
            for (final Plot plot : plugin.getSqlManager().getExpiredPlots(world)) {
                if (!plot.isProtected()) {
                    i++;
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
            }
        }
        plugin.getLogger().info(i + " Expired Plots were deleted");
    }
}
