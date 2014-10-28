package com.worldcretornica.plotme_core;

import com.worldcretornica.plotme_core.api.IWorld;
import com.worldcretornica.plotme_core.api.event.InternalPlotResetEvent;

import java.util.List;

public class PlotRunnableDeleteExpire implements Runnable {

    private final PlotMe_Core plugin;

    public PlotRunnableDeleteExpire(PlotMe_Core instance) {
        plugin = instance;
    }

    @Override
    public void run() {
        SqlManager sqlmanager = plugin.getSqlManager();
        PlotMeCoreManager coremanager = plugin.getPlotMeCoreManager();

        if (plugin.getWorldCurrentlyProcessingExpired() != null) {
            IWorld world = plugin.getWorldCurrentlyProcessingExpired();
            List<Plot> expiredplots = sqlmanager.getExpiredPlots(world.getName(), 0, plugin.getNbPerDeletionProcessingExpired());

            if (expiredplots.isEmpty()) {
                plugin.setCounterExpired(0);
            } else {
                String ids = "";

                for (Plot expiredplot : expiredplots) {
                    InternalPlotResetEvent event = plugin.getServerBridge().getEventFactory().callPlotResetEvent(plugin, world, expiredplot, plugin.getCommandSenderCurrentlyProcessingExpired());

                    if (!event.isCancelled()) {
                        coremanager.clear(world, expiredplot, plugin.getCommandSenderCurrentlyProcessingExpired(), ClearReason.Expired);

                        String id = expiredplot.getId();
                        ids += plugin.getServerBridge().getColor("RED") + id + plugin.getServerBridge().getColor("RESET") + ", ";

                        coremanager.removePlot(world, id);
                        coremanager.removeOwnerSign(world, id);
                        coremanager.removeSellSign(world, id);

                        sqlmanager.deletePlot(PlotMeCoreManager.getIdX(id), PlotMeCoreManager.getIdZ(id), world.getName().toLowerCase());

                        plugin.setCounterExpired(plugin.getCounterExpired() - 1);
                    }
                }

                if (ids.substring(ids.length() - 2).equals(", ")) {
                    ids = ids.substring(0, ids.length() - 2);
                }

                plugin.getCommandSenderCurrentlyProcessingExpired().sendMessage(plugin.getUtil().C("MsgDeletedExpiredPlots") + " " + ids);
            }

            if (plugin.getCounterExpired() == 0) {
                plugin.getCommandSenderCurrentlyProcessingExpired().sendMessage(plugin.getUtil().C("MsgDeleteSessionFinished"));
                plugin.setWorldCurrentlyProcessingExpired(null);
                plugin.setCommandSenderCurrentlyProcessingExpired(null);
            }
        }
    }
}
