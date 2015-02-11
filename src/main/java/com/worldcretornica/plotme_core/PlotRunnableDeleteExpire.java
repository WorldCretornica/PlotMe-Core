package com.worldcretornica.plotme_core;

import com.worldcretornica.plotme_core.api.ICommandSender;
import com.worldcretornica.plotme_core.api.IWorld;
import com.worldcretornica.plotme_core.api.event.InternalPlotResetEvent;

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
        SqlManager sqlmanager = plugin.getSqlManager();
        PlotMeCoreManager plotMeCoreManager = PlotMeCoreManager.getInstance();

        if (plugin.getWorldCurrentlyProcessingExpired() != null) {
            IWorld world = plugin.getWorldCurrentlyProcessingExpired();
            List<Plot> expiredPlots = sqlmanager.getExpiredPlots(world.getName(), 1, 5);

            if (expiredPlots.isEmpty()) {
                plugin.setCounterExpired(0);
            } else {
                String ids = "";

                for (Plot expiredPlot : expiredPlots) {
                    InternalPlotResetEvent event = plugin.getServerBridge().getEventFactory().callPlotResetEvent(plugin, world, expiredPlot, sender);

                    if (!event.isCancelled()) {
                        plotMeCoreManager.clear(world, expiredPlot, sender, ClearReason.Expired);

                        PlotId id = expiredPlot.getId();
                        ids += id + ", ";

                        plotMeCoreManager.removePlot(world, id);
                        plotMeCoreManager.removeOwnerSign(world, id);
                        plotMeCoreManager.removeSellSign(world, id);

                        sqlmanager.deletePlot(id, world.getName());

                        plugin.setCounterExpired(plugin.getCounterExpired() - 1);
                    }
                }

                if (", ".equals(ids.substring(ids.length() - 2))) {
                    ids = ids.substring(0, ids.length() - 2);
                }

                plugin.getLogger().info(plugin.getUtil().C("MsgDeletedExpiredPlots") + " " + ids);
            }

            if (plugin.getCounterExpired() == 0) {
                plugin.getLogger().info(plugin.getUtil().C("MsgDeleteSessionFinished"));
                plugin.setWorldCurrentlyProcessingExpired(null);
            }
        }
    }
}