package com.worldcretornica.plotme_core.commands;

import com.worldcretornica.plotme_core.PermissionNames;
import com.worldcretornica.plotme_core.Plot;
import com.worldcretornica.plotme_core.PlotMapInfo;
import com.worldcretornica.plotme_core.PlotMe_Core;
import com.worldcretornica.plotme_core.api.CommandExBase;
import com.worldcretornica.plotme_core.api.ICommandSender;
import com.worldcretornica.plotme_core.api.IPlayer;
import com.worldcretornica.plotme_core.api.IWorld;
import com.worldcretornica.plotme_core.api.event.PlotDisposeEvent;
import net.milkbowl.vault.economy.EconomyResponse;

public class CmdDispose extends PlotCommand {

    public CmdDispose(PlotMe_Core instance, CommandExBase commandExBase) {
        super(instance);
    }

    public String getName() {
        return "dispose";
    }

    public boolean execute(ICommandSender sender, String[] args) {
        if (args.length > 1) {
            sender.sendMessage(getUsage());
            return true;
        }
        IPlayer player = (IPlayer) sender;
        if (player.hasPermission(PermissionNames.ADMIN_DISPOSE) || player.hasPermission(PermissionNames.USER_DISPOSE)) {
            IWorld world = player.getWorld();
            PlotMapInfo pmi = manager.getMap(world);
            if (manager.isPlotWorld(world)) {
                Plot plot = manager.getPlot(player);

                if (plot != null) {
                    if (plot.isProtected()) {
                        player.sendMessage(C("MsgPlotProtectedNotDisposed"));
                    } else if (player.getUniqueId().equals(plot.getOwnerId()) || player.hasPermission(PermissionNames.ADMIN_DISPOSE)) {

                        double cost = pmi.getDisposePrice();

                        PlotDisposeEvent event = new PlotDisposeEvent(plot, player);

                        if (manager.isEconomyEnabled(pmi)) {
                            if (serverBridge.has(player, cost)) {
                                player.sendMessage(C("MsgNotEnoughDispose"));
                                return true;
                            }

                            plugin.getEventBus().post(event);

                            if (event.isCancelled()) {
                                return true;
                            }
                            EconomyResponse economyResponse = serverBridge.withdrawPlayer(player, cost);

                            if (!economyResponse.transactionSuccess()) {
                                player.sendMessage(economyResponse.errorMessage);
                                plugin.getLogger().warning(economyResponse.errorMessage);
                                return true;
                            }
                        } else {
                            plugin.getEventBus().post(event);
                        }

                        if (!event.isCancelled()) {
                            manager.deletePlot(plot);

                            manager.adjustWall(plot, false);
                            player.sendMessage(C("PlotDisposed"));

                            if (isAdvancedLogging()) {
                                plugin.getLogger().info(player.getName() + " " + C("MsgDisposedPlot") + " " + plot.getId());
                            }
                        }
                    } else {
                        player.sendMessage(C("MsgThisPlot") + "(" + plot.getId() + ") " + C("MsgNotYoursCannotDispose"));
                    }
                }
            } else {
                player.sendMessage(C("NotPlotWorld"));
            }
        } else {
            return false;
        }
        return true;
    }

    @Override
    public String getUsage() {
        return C("WordUsage") + ": /plotme dispose";
    }
}
