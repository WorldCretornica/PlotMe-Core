package com.worldcretornica.plotme_core.commands;

import com.worldcretornica.plotme_core.PermissionNames;
import com.worldcretornica.plotme_core.Plot;
import com.worldcretornica.plotme_core.PlotMapInfo;
import com.worldcretornica.plotme_core.PlotMe_Core;
import com.worldcretornica.plotme_core.api.ICommandSender;
import com.worldcretornica.plotme_core.api.IPlayer;
import com.worldcretornica.plotme_core.api.IWorld;
import com.worldcretornica.plotme_core.api.event.PlotDisposeEvent;
import net.milkbowl.vault.economy.EconomyResponse;

public class CmdDispose extends PlotCommand {

    public CmdDispose(PlotMe_Core instance) {
        super(instance);
    }

    public String getName() {
        return "dispose";
    }

    public boolean execute(ICommandSender sender, String[] args) throws Exception{
        if (args.length > 1) {
            throw new BadUsageException(getUsage());
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
                    } else {
                        String name = player.getName();

                        if (player.getUniqueId().equals(plot.getOwnerId()) || player.hasPermission(PermissionNames.ADMIN_DISPOSE)) {

                            double cost = pmi.getDisposePrice();

                            PlotDisposeEvent event = new PlotDisposeEvent(world, plot, player);

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
                                    serverBridge.getLogger().warning(economyResponse.errorMessage);
                                    return true;
                                }
                            } else {
                                plugin.getEventBus().post(event);
                            }

                            if (!event.isCancelled()) {
                                manager.deletePlot(pmi, plot);

                                manager.removeOwnerSign(plot, world);
                                manager.removeSellSign(plot, world);

                                plugin.getSqlManager().deletePlot(plot.getInternalID());
                                manager.adjustWall(plot, world, false);
                                player.sendMessage(C("MsgPlotDisposedAnyoneClaim"));

                                if (isAdvancedLogging()) {
                                    serverBridge.getLogger().info(name + " " + C("MsgDisposedPlot") + " " + plot.getId());
                                }
                            }
                        } else {
                            player.sendMessage(C("MsgThisPlot") + "(" + plot.getId() + ") " + C("MsgNotYoursCannotDispose"));
                        }
                    }
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
