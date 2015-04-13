package com.worldcretornica.plotme_core.commands;

import com.worldcretornica.plotme_core.PermissionNames;
import com.worldcretornica.plotme_core.Plot;
import com.worldcretornica.plotme_core.PlotId;
import com.worldcretornica.plotme_core.PlotMapInfo;
import com.worldcretornica.plotme_core.PlotMe_Core;
import com.worldcretornica.plotme_core.api.ICommandSender;
import com.worldcretornica.plotme_core.api.IPlayer;
import com.worldcretornica.plotme_core.api.IWorld;
import com.worldcretornica.plotme_core.api.event.PlotAddTrustedEvent;
import net.milkbowl.vault.economy.EconomyResponse;

//TODO DOES NOT WORK. CODE NEEDS TO BE MODIFIED FOR TRUST
public class CmdTrust extends PlotCommand {

    public CmdTrust(PlotMe_Core instance) {
        super(instance);
    }

    public String getName() {
        return "trust";
    }

    public boolean execute(ICommandSender sender, String[] args) throws Exception{
        if (args.length < 2 && args.length >= 3) {
            throw new BadUsageException(getUsage());
        }
        if (args[1].length() > 16 || !validUserPattern.matcher(args[1]).matches() || "*".equalsIgnoreCase(args[1])) {
            throw new IllegalArgumentException(C("InvalidCommandInput"));
        }
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
                    Plot plot = manager.getPlotById(id, pmi);
                    if (plot == null) {
                        return true;
                    }
                    String trust = args[1];

                    if (player.getUniqueId().equals(plot.getOwnerId()) || player.hasPermission(PermissionNames.ADMIN_TRUST)) {
                        if (plot.isAllowedConsulting(trust)) {
                            player.sendMessage(C("WordPlayer") + " " + trust + " " + C("MsgAlreadyAllowed"));
                        } else {

                            PlotAddTrustedEvent event = new PlotAddTrustedEvent(world, plot, player, trust);
                            serverBridge.getEventBus().post(event);

                            serverBridge.getEventBus().post(event);
                            if (manager.isEconomyEnabled(pmi)) {
                                double price = pmi.getAddPlayerPrice();

                                if (serverBridge.has(player, price)) {
                                    player.sendMessage(C("MsgNotEnoughAdd") + " " + C("WordMissing") + " " + serverBridge.getEconomy().format(price));
                                    return true;
                                } else if (!event.isCancelled()) {
                                    EconomyResponse er = serverBridge.withdrawPlayer(player, price);

                                    if (!er.transactionSuccess()) {
                                        player.sendMessage(er.errorMessage);
                                        serverBridge.getLogger().warning(er.errorMessage);
                                        return true;
                                    }
                                } else {
                                    return true;
                                }
                            }

                            if (!event.isCancelled()) {
                                IPlayer allowed2 = plugin.getServerBridge().getPlayerExact(trust);
                                if (allowed2 != null) {
                                    plot.addAllowed(allowed2.getUniqueId().toString());
                                    plot.removeDenied(allowed2.getUniqueId().toString());
                                } else {
                                    plot.addAllowed(trust);
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
        return C("CmdTrustUsage");
    }

}