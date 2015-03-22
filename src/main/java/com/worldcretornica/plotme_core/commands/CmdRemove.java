package com.worldcretornica.plotme_core.commands;

import com.worldcretornica.plotme_core.PermissionNames;
import com.worldcretornica.plotme_core.Plot;
import com.worldcretornica.plotme_core.PlotId;
import com.worldcretornica.plotme_core.PlotMapInfo;
import com.worldcretornica.plotme_core.PlotMe_Core;
import com.worldcretornica.plotme_core.api.ICommandSender;
import com.worldcretornica.plotme_core.api.IPlayer;
import com.worldcretornica.plotme_core.api.IWorld;
import com.worldcretornica.plotme_core.api.event.InternalPlotRemoveAllowedEvent;
import net.milkbowl.vault.economy.EconomyResponse;

import java.util.UUID;

public class CmdRemove extends PlotCommand implements CommandBase {

    public CmdRemove(PlotMe_Core instance) {
        super(instance);
    }

    public String getName() {
        return "remove";
    }

    public boolean execute(ICommandSender sender, String[] args) {
        IPlayer player = (IPlayer) sender;
        if (player.hasPermission(PermissionNames.ADMIN_REMOVE) || player.hasPermission(PermissionNames.USER_REMOVE)) {
            IWorld world = player.getWorld();
            PlotMapInfo pmi = manager.getMap(world);
            if (manager.isPlotWorld(world)) {
                PlotId id = manager.getPlotId(player);
                if (id == null) {
                    player.sendMessage(C("MsgNoPlotFound"));
                    return true;
                } else if (!manager.isPlotAvailable(id, pmi)) {
                    if (args.length < 2 || args[1].isEmpty()) {
                        player.sendMessage(getUsage());
                    } else {
                        Plot plot = manager.getPlotById(id, pmi);
                        UUID playerUniqueId = player.getUniqueId();
                        String allowed = args[1];

                        if (plot.getOwnerId().equals(playerUniqueId) || player.hasPermission(PermissionNames.ADMIN_REMOVE)) {
                            if (plot.isAllowedConsulting(allowed)) {

                                double price = 0.0;

                                InternalPlotRemoveAllowedEvent event;

                                if (manager.isEconomyEnabled(pmi)) {
                                    price = pmi.getRemovePlayerPrice();
                                    double balance = serverBridge.getBalance(player);

                                    if (balance >= price) {
                                        event = serverBridge.getEventFactory().callPlotRemoveAllowedEvent(world, plot, player, allowed);

                                        if (event.isCancelled()) {
                                            return true;
                                        } else {
                                            EconomyResponse er = serverBridge.withdrawPlayer(player, price);

                                            if (!er.transactionSuccess()) {
                                                player.sendMessage(er.errorMessage);
                                                serverBridge.getLogger().warning(er.errorMessage);
                                                return true;
                                            }
                                        }
                                    } else {
                                        player.sendMessage(C("MsgNotEnoughRemove") + " " + C("WordMissing") + " " + plugin.moneyFormat(price -
                                                balance, false));
                                        return true;
                                    }
                                } else {
                                    event = serverBridge.getEventFactory().callPlotRemoveAllowedEvent(world, plot, player, allowed);
                                }

                                if (!event.isCancelled()) {
                                    plot.removeAllowed(allowed);

                                    player.sendMessage(
                                            C("WordPlayer") + " " + allowed + " " + C("WordRemoved") + ". " + plugin.moneyFormat(-price, true));

                                    if (isAdvancedLogging()) {
                                        if (price == 0) {
                                            serverBridge.getLogger()
                                                    .info(allowed + " " + C("MsgRemovedPlayer") + " " + allowed + " " + C("MsgFromPlot") + " " + id);
                                        } else {
                                            serverBridge.getLogger()
                                                    .info(allowed + " " + C("MsgRemovedPlayer") + " " + allowed + " " + C("MsgFromPlot") + " " + id
                                                            + (" " + C("WordFor") + " " + price));
                                        }
                                    }
                                }
                            } else {
                                player.sendMessage(C("WordPlayer") + " " + args[1] + " " + C("MsgWasNotAllowed"));
                            }
                        } else {
                            player.sendMessage(C("MsgThisPlot") + "(" + id + ") " + C("MsgNotYoursNotAllowedRemove"));
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
        return C("WordUsage") + ": /plotme remove <" + C("WordPlayer") + ">";
    }
}
