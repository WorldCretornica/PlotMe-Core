package com.worldcretornica.plotme_core;

import com.worldcretornica.plotme_core.event.PlotMeEventFactory;
import com.worldcretornica.plotme_core.event.PlotResetEvent;
import java.util.List;
import org.bukkit.ChatColor;
import org.bukkit.World;

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
            World w = plugin.getWorldCurrentlyProcessingExpired();
            List<Plot> expiredplots = sqlmanager.getExpiredPlots(w.getName(), 0, plugin.getNbPerDeletionProcessingExpired());

            if (expiredplots.isEmpty()) {
                plugin.setCounterExpired(0);
            } else {
                String ids = "";

                for (Plot expiredplot : expiredplots) {
                    PlotResetEvent event = PlotMeEventFactory.callPlotResetEvent(plugin, w, expiredplot, plugin.getCommandSenderCurrentlyProcessingExpired());

                    if (!event.isCancelled()) {
                        coremanager.clear(w, expiredplot, plugin.getCommandSenderCurrentlyProcessingExpired(), ClearReason.Expired);

                        String id = expiredplot.getId();
                        ids += ChatColor.RED + id + ChatColor.RESET + ", ";

                        coremanager.removePlot(w, id);
                        coremanager.removeOwnerSign(w, id);
                        coremanager.removeSellSign(w, id);

                        sqlmanager.deletePlot(coremanager.getIdX(id), coremanager.getIdZ(id), w.getName().toLowerCase());

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
