package com.worldcretornica.plotme_core;

import com.worldcretornica.plotme_core.api.ICommandSender;
import com.worldcretornica.plotme_core.api.IWorld;
import com.worldcretornica.plotme_core.api.event.PlotResetEvent;
import com.worldcretornica.plotme_core.storage.Database;

import java.util.List;

public class PlotRunnableDeleteExpire implements Runnable {

    private final PlotMe_Core plugin;
    private final ICommandSender sender;

    public PlotRunnableDeleteExpire(PlotMe_Core instance, ICommandSender sender) {
        plugin = instance;
        this.sender = sender;
    }

    @Override
    public void run() {
        Database sqlmanager = plugin.getSqlManager();
        PlotMeCoreManager plotMeCoreManager = PlotMeCoreManager.getInstance();

        if (plugin.getWorldCurrentlyProcessingExpired() != null) {
            IWorld world = plugin.getWorldCurrentlyProcessingExpired();
            List<Plot> expiredPlots = sqlmanager.getExpiredPlots(world);

            if (expiredPlots.isEmpty()) {
                plugin.setCounterExpired(0);
            } else {

                for (Plot expiredPlot : expiredPlots) {
                    if (!expiredPlot.isProtected()) {
                        PlotResetEvent event = new PlotResetEvent(expiredPlot, sender);
                        plugin.getEventBus().post(event);
                        if (!event.isCancelled()) {
                            plotMeCoreManager.clear(expiredPlot, sender, ClearReason.Expired);

                            plotMeCoreManager.deletePlot(expiredPlot);
                            plugin.setCounterExpired(plugin.getCounterExpired() - 1);
                        }
                    }
                }

                plugin.getLogger().info(plugin.C("DeletedExpiredPlots", expiredPlots.size()));
            }

            if (plugin.getCounterExpired() == 0) {
                plugin.getLogger().info(plugin.C("MsgDeleteSessionFinished"));
                plugin.setWorldCurrentlyProcessingExpired(null);
            }
        }
    }
}