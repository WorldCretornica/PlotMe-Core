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

import java.util.Collections;
import java.util.List;

public class CmdAdd extends PlotCommand {

    public CmdAdd(PlotMe_Core instance) {
        super(instance);
    }

    public String getName() {
        return "add";
    }

    public boolean execute(ICommandSender sender, String[] args) {
        if (args[1].length() > 16 || !validUserPattern.matcher(args[1]).matches()) {
            throw new IllegalArgumentException(C("InvalidCommandInput"));
        }
        IPlayer player = (IPlayer) sender;
        if (player.hasPermission(PermissionNames.ADMIN_ADD) || player.hasPermission(PermissionNames.USER_ADD)) {
            IWorld world = player.getWorld();
            PlotMapInfo pmi = manager.getMap(world);
            if (manager.isPlotWorld(world)) {
                PlotId id = manager.getPlotId(player);
                if (id == null) {
                    player.sendMessage(C("MsgNoPlotFound"));
                    return true;
                } else if (!manager.isPlotAvailable(id, pmi)) {
                    if (args.length < 2 && args.length >= 3) {
                        player.sendMessage(getUsage());
                        return true;
                    } else {
                        Plot plot = manager.getPlotById(id, pmi);
                        if (plot == null) {
                            player.sendMessage("Something is terribly wrong. Report to admin.");
                            return true;
                        }
                        String allowed = args[1];
                        if (player.getUniqueId().equals(plot.getOwnerId()) || player.hasPermission(PermissionNames.ADMIN_ADD)) {
                            if (plot.isAllowedConsulting(allowed)) {
                                player.sendMessage(C("WordPlayer") + " " + allowed + " " + C("MsgAlreadyAllowed"));
                            } else {
                                InternalPlotAddAllowedEvent event;
                                double price = 0.0;
                                if (manager.isEconomyEnabled(pmi)) {
                                    price = pmi.getAddPlayerPrice();
                                    double balance = serverBridge.getBalance(player);

                                    if (balance >= pmi.getAddPlayerPrice()) {
                                        event = serverBridge.getEventFactory().callPlotAddAllowedEvent(world, plot, player, allowed);

                                        if (!event.isCancelled()) {
                                            EconomyResponse er = serverBridge.withdrawPlayer(player, price);

                                            if (!er.transactionSuccess()) {
                                                player.sendMessage(er.errorMessage);
                                                serverBridge.getLogger().warning(er.errorMessage);
                                                return true;
                                            }
                                        }
                                    } else {
                                        player.sendMessage(
                                                C("MsgNotEnoughAdd") + " " + C("WordMissing") + " " + plugin.moneyFormat(price - balance, false));
                                        return true;
                                    }
                                } else {
                                    event = serverBridge.getEventFactory().callPlotAddAllowedEvent(world, plot, player, allowed);
                                }

                                if (!event.isCancelled()) {
                                    IPlayer allowed2 = plugin.getServerBridge().getPlayerExact(allowed);
                                    if (allowed2 != null) {
                                        plot.addAllowed(allowed2.getUniqueId().toString(), 0);
                                        plot.removeDenied(allowed2.getUniqueId().toString());
                                    } else {
                                        plot.addAllowed(allowed, 0);
                                        plot.removeDenied(allowed);
                                    }
                                    player.sendMessage(C("WordPlayer") + " " + allowed + " " + C("MsgNowAllowed"));

                                    if (isAdvancedLogging()) {
                                        if (price == 0) {
                                            serverBridge.getLogger()
                                                    .info(player.getName() + " " + C("MsgAddedPlayer") + " " + allowed + " " + C("MsgToPlot") + " "
                                                            + id);
                                        } else {
                                            serverBridge.getLogger()
                                                    .info(player.getName() + " " + C("MsgAddedPlayer") + " " + allowed + " " + C("MsgToPlot") + " "
                                                            + id + (" " + C("WordFor") + " " + price));
                                        }
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
                return true;
            }
        } else {
            return false;
        }
        return true;
    }

    @Override
    public List getAliases() {
        return Collections.singletonList("+");
    }

    @Override
    public String getUsage() {
        return C("CmdAddUsage");
    }

}
