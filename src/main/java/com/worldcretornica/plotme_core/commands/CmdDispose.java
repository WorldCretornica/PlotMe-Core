package com.worldcretornica.plotme_core.commands;

import com.worldcretornica.plotme_core.PermissionNames;
import com.worldcretornica.plotme_core.Plot;
import com.worldcretornica.plotme_core.PlotId;
import com.worldcretornica.plotme_core.PlotMapInfo;
import com.worldcretornica.plotme_core.PlotMe_Core;
import com.worldcretornica.plotme_core.api.ICommandSender;
import com.worldcretornica.plotme_core.api.IPlayer;
import com.worldcretornica.plotme_core.api.IWorld;
import com.worldcretornica.plotme_core.api.event.InternalPlotDisposeEvent;
import net.milkbowl.vault.economy.EconomyResponse;

public class CmdDispose extends PlotCommand {

    public CmdDispose(PlotMe_Core instance) {
        super(instance);
    }

    public String getName() {
        return "dispose";
    }

    public boolean execute(ICommandSender sender, String[] args) throws Exception{
        IPlayer player = (IPlayer) sender;
        if (player.hasPermission(PermissionNames.ADMIN_DISPOSE) || player.hasPermission(PermissionNames.USER_DISPOSE)) {
            IWorld world = player.getWorld();
            PlotMapInfo pmi = manager.getMap(world);
            if (manager.isPlotWorld(world)) {
                PlotId id = manager.getPlotId(player);
                if (id == null) {
                    player.sendMessage(C("MsgNoPlotFound"));
                    return true;
                } else if (!manager.isPlotAvailable(id, pmi)) {
                    Plot plot = manager.getPlotById(id, pmi);

                    if (plot.isProtected()) {
                        player.sendMessage(C("MsgPlotProtectedNotDisposed"));
                    } else {
                        String name = player.getName();

                        if (player.getUniqueId().equals(plot.getOwnerId()) || player.hasPermission(PermissionNames.ADMIN_DISPOSE)) {

                            double cost = pmi.getDisposePrice();

                            InternalPlotDisposeEvent event = new InternalPlotDisposeEvent(world, plot, player);

                            if (manager.isEconomyEnabled(pmi)) {
                                if (cost != 0 && serverBridge.getBalance(player) < cost) {
                                    player.sendMessage(C("MsgNotEnoughDispose"));
                                    return true;
                                }

                                serverBridge.getEventBus().post(event);

                                if (event.isCancelled()) {
                                    return true;
                                }
                                EconomyResponse economyResponse = serverBridge.withdrawPlayer(player, cost);

                                if (!economyResponse.transactionSuccess()) {
                                    player.sendMessage(economyResponse.errorMessage);
                                    serverBridge.getLogger().warning(economyResponse.errorMessage);
                                    return true;
                                }
                            } else {
                                serverBridge.getEventBus().post(event);
                            }

                            if (!event.isCancelled()) {
                                if (!manager.isPlotAvailable(id, pmi)) {
                                    manager.removePlot(pmi, id);
                                }

                                manager.removeOwnerSign(id);
                                manager.removeSellSign(id);

                                plugin.getSqlManager().deletePlot(plot.getInternalID());
                                manager.adjustWall(id, false);
                                player.sendMessage(C("MsgPlotDisposedAnyoneClaim"));

                                if (isAdvancedLogging()) {
                                    serverBridge.getLogger().info(name + " " + C("MsgDisposedPlot") + " " + id);
                                }
                            }
                        } else {
                            player.sendMessage(C("MsgThisPlot") + "(" + id + ") " + C("MsgNotYoursCannotDispose"));
                        }
                    }
                } else {
                    player.sendMessage(C("MsgThisPlot") + "(" + id + ") " + C("MsgHasNoOwner"));
                }
            } else {
                player.sendMessage(C("MsgNotPlotWorld"));
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
