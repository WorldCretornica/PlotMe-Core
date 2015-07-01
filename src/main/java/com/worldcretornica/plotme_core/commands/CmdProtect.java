package com.worldcretornica.plotme_core.commands;

import com.worldcretornica.plotme_core.PermissionNames;
import com.worldcretornica.plotme_core.Plot;
import com.worldcretornica.plotme_core.PlotMapInfo;
import com.worldcretornica.plotme_core.PlotMe_Core;
import com.worldcretornica.plotme_core.api.CommandExBase;
import com.worldcretornica.plotme_core.api.ICommandSender;
import com.worldcretornica.plotme_core.api.IPlayer;
import com.worldcretornica.plotme_core.api.IWorld;
import com.worldcretornica.plotme_core.api.event.PlotProtectChangeEvent;
import net.milkbowl.vault.economy.EconomyResponse;

public class CmdProtect extends PlotCommand {

    public CmdProtect(PlotMe_Core instance, CommandExBase commandExBase) {
        super(instance);
    }

    public String getName() {
        return "protect";
    }

    public boolean execute(ICommandSender sender, String[] args) {
        if (args.length > 1) {
            sender.sendMessage(getUsage());
            return true;
        }
        IPlayer player = (IPlayer) sender;
        if (player.hasPermission(PermissionNames.ADMIN_PROTECT) || player.hasPermission(PermissionNames.USER_PROTECT)) {
            IWorld world = player.getWorld();
            PlotMapInfo pmi = manager.getMap(world);
            if (manager.isPlotWorld(world)) {
                Plot plot = manager.getPlot(player);

                if (plot == null) {
                    player.sendMessage(C("NoPlotFound"));
                    return true;
                } else if (player.getUniqueId().equals(plot.getOwnerId()) || player.hasPermission(PermissionNames.ADMIN_PROTECT)) {
                    PlotProtectChangeEvent event;

                    if (plot.isProtected()) {
                        event = new PlotProtectChangeEvent(plot, player, false);
                        plugin.getEventBus().post(event);

                        if (!event.isCancelled()) {
                            plot.setProtected(false);
                            manager.adjustWall(player);
                            plugin.getSqlManager().savePlot(plot);

                            player.sendMessage(C("MsgPlotNoLongerProtected"));

                            if (isAdvancedLogging()) {
                                serverBridge.getLogger().info(player.getName() + " " + C("MsgUnprotectedPlot") + " " + plot.getId().getID());
                            }
                        }
                    } else {
                        double cost = pmi.getProtectPrice();
                        if (manager.isEconomyEnabled(pmi)) {
                            if (serverBridge.has(player, cost)) {
                                player.sendMessage(C("MsgNotEnoughProtectPlot"));
                                return true;
                            } else {
                                event = new PlotProtectChangeEvent(plot, player, true);
                                plugin.getEventBus().post(event);
                                if (!event.isCancelled()) {
                                    EconomyResponse er = serverBridge.withdrawPlayer(player, cost);

                                    if (!er.transactionSuccess()) {
                                        player.sendMessage(er.errorMessage);
                                        serverBridge.getLogger().warning(er.errorMessage);
                                        return true;
                                    }
                                } else {
                                    return true;
                                }
                            }

                        } else {
                            event = new PlotProtectChangeEvent(plot, player, true);
                            plugin.getEventBus().post(event);
                        }

                        if (!event.isCancelled()) {
                            plot.setProtected(true);
                            manager.adjustWall(player);

                            plugin.getSqlManager().savePlot(plot);
                            player.sendMessage(C("MsgPlotNowProtected"));

                            if (isAdvancedLogging()) {
                                serverBridge.getLogger().info(player.getName() + " " + C("MsgProtectedPlot") + " " + plot.getId());
                            }
                        }
                    }
                } else {
                    player.sendMessage(C("MsgDoNotOwnPlot"));
                }
            } else {
                player.sendMessage(C("NotPlotWorld"));
                return true;
            }
        } else {
            return false;
        }
        return true;
    }

    @Override
    public String getUsage() {
        return C("WordUsage") + ": /plotme protect";
    }
}
