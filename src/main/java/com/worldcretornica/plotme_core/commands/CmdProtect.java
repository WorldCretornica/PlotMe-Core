package com.worldcretornica.plotme_core.commands;

import com.worldcretornica.plotme_core.PermissionNames;
import com.worldcretornica.plotme_core.Plot;
import com.worldcretornica.plotme_core.PlotId;
import com.worldcretornica.plotme_core.PlotMapInfo;
import com.worldcretornica.plotme_core.PlotMe_Core;
import com.worldcretornica.plotme_core.api.ICommandSender;
import com.worldcretornica.plotme_core.api.IPlayer;
import com.worldcretornica.plotme_core.api.IWorld;
import com.worldcretornica.plotme_core.api.event.PlotProtectChangeEvent;
import net.milkbowl.vault.economy.EconomyResponse;

public class CmdProtect extends PlotCommand {

    public CmdProtect(PlotMe_Core instance) {
        super(instance);
    }

    public String getName() {
        return "protect";
    }

    public boolean execute(ICommandSender sender, String[] args) throws Exception{
        if (args.length > 1) {
            throw new BadUsageException(getUsage());
        }
        IPlayer player = (IPlayer) sender;
        if (player.hasPermission(PermissionNames.ADMIN_PROTECT) || player.hasPermission(PermissionNames.USER_PROTECT)) {
            IWorld world = player.getWorld();
            PlotMapInfo pmi = manager.getMap(world);
            if (manager.isPlotWorld(world)) {
                PlotId id = manager.getPlotId(player);

                if (id == null) {
                    player.sendMessage(C("MsgNoPlotFound"));
                    return true;
                } else if (!manager.isPlotAvailable(id, pmi)) {
                    Plot plot = manager.getPlotById(id, pmi);

                    String name = player.getName();

                    if (player.getUniqueId().equals(plot.getOwnerId()) || player.hasPermission(PermissionNames.ADMIN_PROTECT)) {
                        PlotProtectChangeEvent event;

                        if (plot.isProtected()) {
                            event = new PlotProtectChangeEvent(world, plot, player, false);
                            serverBridge.getEventBus().post(event);

                            if (!event.isCancelled()) {
                                plot.setProtected(false);
                                manager.adjustWall(player);

                                plot.updateField("protected", false);

                                player.sendMessage(C("MsgPlotNoLongerProtected"));

                                if (isAdvancedLogging()) {
                                    serverBridge.getLogger().info(name + " " + C("MsgUnprotectedPlot") + " " + id);
                                }
                            }
                        } else {

                            double cost = pmi.getProtectPrice();

                            if (manager.isEconomyEnabled(pmi)) {
                                if (serverBridge.has(player, cost)) {
                                    player.sendMessage(C("MsgNotEnoughProtectPlot"));
                                    return true;
                                } else {
                                    event = new PlotProtectChangeEvent(world, plot, player, true);
                                    serverBridge.getEventBus().post(event);
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
                                event = new PlotProtectChangeEvent(world, plot, player, true);
                                serverBridge.getEventBus().post(event);
                            }

                            if (!event.isCancelled()) {
                                plot.setProtected(true);
                                manager.adjustWall(player);

                                plot.updateField("protected", true);

                                player.sendMessage(C("MsgPlotNowProtected") + " " + serverBridge.getEconomy().get().format(cost));

                                if (isAdvancedLogging()) {
                                    serverBridge.getLogger().info(name + " " + C("MsgProtectedPlot") + " " + id);
                                }
                            }
                        }
                    } else {
                        player.sendMessage(C("MsgDoNotOwnPlot"));
                    }
                } else {
                    player.sendMessage(C("MsgThisPlot") + "(" + id + ") " + C("MsgHasNoOwner"));
                }
            } else {
                player.sendMessage(C("MsgNotPlotWorld"));
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
