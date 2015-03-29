package com.worldcretornica.plotme_core.commands;

import com.worldcretornica.plotme_core.PermissionNames;
import com.worldcretornica.plotme_core.Plot;
import com.worldcretornica.plotme_core.PlotId;
import com.worldcretornica.plotme_core.PlotMapInfo;
import com.worldcretornica.plotme_core.PlotMe_Core;
import com.worldcretornica.plotme_core.api.ICommandSender;
import com.worldcretornica.plotme_core.api.IPlayer;
import com.worldcretornica.plotme_core.api.IWorld;
import com.worldcretornica.plotme_core.api.event.InternalPlotAddAllowedEvent;
import net.milkbowl.vault.economy.EconomyResponse;

//TODO DOES NOT WORK. CODE NEEDS TO BE MODIFIED FOR TRUST
public class CmdTrust extends PlotCommand {

    public CmdTrust(PlotMe_Core instance) {
        super(instance);
    }

    public String getName() {
        return "trust";
    }

    public boolean execute(ICommandSender sender, String[] args) {
        IPlayer player = (IPlayer) sender;
        if (player.hasPermission(PermissionNames.ADMIN_TRUST) || player.hasPermission(PermissionNames.USER_TRUST)) {
            IWorld world = player.getWorld();
            PlotMapInfo pmi = manager.getMap(world);
            if (manager.isPlotWorld(world)) {
                PlotId id = manager.getPlotId(player);
                if (id == null) {
                    player.sendMessage(C("MsgNoPlotFound"));
                    return true;
                } else if (!manager.isPlotAvailable(id, pmi)) {
                    if (args.length < 2) {
                        player.sendMessage(C("WordUsage") + " /plotme add <" + C("WordPlayer") + ">");
                    } else {
                        Plot plot = manager.getPlotById(id, pmi);

                        String trust = args[1];

                        if (player.getUniqueId().equals(plot.getOwnerId()) || player.hasPermission(PermissionNames.ADMIN_TRUST)) {
                            if (plot.isAllowedConsulting(trust)) {
                                player.sendMessage(C("WordPlayer") + " " + trust + " " + C("MsgAlreadyAllowed"));
                            } else {

                                InternalPlotAddAllowedEvent event;
                                double advancedPrice = 0.0;
                                if (manager.isEconomyEnabled(pmi)) {
                                    double price = pmi.getAddPlayerPrice();
                                    advancedPrice = price;
                                    double balance = serverBridge.getBalance(player);

                                    if (balance >= price) {
                                        event = serverBridge.getEventFactory().callPlotAddAllowedEvent(world, plot, player, trust);

                                        if (!event.isCancelled()) {
                                            EconomyResponse er = serverBridge.withdrawPlayer(player, price);

                                            if (!er.transactionSuccess()) {
                                                player.sendMessage(er.errorMessage);
                                                serverBridge.getLogger().warning(er.errorMessage);
                                                return true;
                                            }
                                        } else {
                                            return true;
                                        }
                                    } else {
                                        player.sendMessage(C("MsgNotEnoughAdd") + " " + C("WordMissing") + " " + plugin.moneyFormat(price - balance,
                                                false));
                                        return true;
                                    }
                                } else {
                                    event = serverBridge.getEventFactory().callPlotAddAllowedEvent(world, plot, player, trust);
                                }

                                if (!event.isCancelled()) {
                                    IPlayer allowed2 = plugin.getServerBridge().getPlayerExact(trust);
                                    if (allowed2 != null) {
                                        plot.addAllowed(allowed2.getUniqueId().toString(), 0);
                                        plot.removeDenied(allowed2.getUniqueId().toString());
                                    } else {
                                        plot.addAllowed(trust, 0);
                                        plot.removeDenied(trust);
                                    }
                                    player.sendMessage(C("WordPlayer") + " " + trust + " " + C("MsgNowAllowed"));

                                    if (isAdvancedLogging()) {
                                        serverBridge.getLogger()
                                                .info(player.getName() + " " + C("MsgAddedPlayer") + " " + trust + " " + C("MsgToPlot") + " "
                                                        + id);
                                    }
                                }
                            }
                        } else {
                            player.sendMessage(C("MsgThisPlot") + "(" + id + ") " + C("MsgNotYoursNotAllowedAdd"));
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
        return C("WordUsage") + ": /plotme trust <" + C("WordPlayer") + ">";
    }

}