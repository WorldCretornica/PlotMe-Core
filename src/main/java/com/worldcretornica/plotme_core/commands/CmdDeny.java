package com.worldcretornica.plotme_core.commands;

import com.worldcretornica.plotme_core.PermissionNames;
import com.worldcretornica.plotme_core.Plot;
import com.worldcretornica.plotme_core.PlotId;
import com.worldcretornica.plotme_core.PlotMapInfo;
import com.worldcretornica.plotme_core.PlotMe_Core;
import com.worldcretornica.plotme_core.api.ICommandSender;
import com.worldcretornica.plotme_core.api.IPlayer;
import com.worldcretornica.plotme_core.api.IWorld;
import com.worldcretornica.plotme_core.api.event.PlotAddDeniedEvent;
import net.milkbowl.vault.economy.EconomyResponse;

import java.util.List;

public class CmdDeny extends PlotCommand {

    public CmdDeny(PlotMe_Core instance) {
        super(instance);
    }

    public String getName() {
        return "deny";
    }

    public boolean execute(ICommandSender sender, String[] args) throws Exception{
        if (args.length < 2 && args.length >= 3) {
            throw new BadUsageException(getUsage());
        }
        if (args[1].length() > 16 || !validUserPattern.matcher(args[1]).matches()) {
            throw new IllegalArgumentException(C("InvalidCommandInput"));
        }
        if ("*".equals(args[1]) && plugin.getConfig().getBoolean("disableWildCard")) {
            sender.sendMessage("Wildcards are disabled.");
            return true;
        }
        IPlayer player = (IPlayer) sender;
        if (player.hasPermission(PermissionNames.ADMIN_DENY) || player.hasPermission(PermissionNames.USER_DENY)) {
            IWorld world = player.getWorld();
            if (manager.isPlotWorld(world)) {
                PlotId id = manager.getPlotId(player);
                if (id == null) {
                    player.sendMessage(C("MsgNoPlotFound"));
                    return true;
                }
                PlotMapInfo pmi = manager.getMap(world);
                if (!manager.isPlotAvailable(id, pmi)) {
                    Plot plot = manager.getPlotById(id, pmi);
                    String denied = args[1];

                    if (player.getUniqueId().equals(plot.getOwnerId()) || player.hasPermission(PermissionNames.ADMIN_DENY)) {
                        if (plot.getOwner().equals(denied)) {
                            player.sendMessage(C("MsgCannotDenyOwner"));
                            return true;
                        }

                        if (plot.isDeniedConsulting(denied)) {
                            player.sendMessage(C("WordPlayer") + " " + args[1] + " " + C("MsgAlreadyDenied"));
                        } else {

                            double price = 0.0;

                            PlotAddDeniedEvent event = new PlotAddDeniedEvent(world, plot, player, denied);

                            if (manager.isEconomyEnabled(pmi)) {
                                price = pmi.getDenyPlayerPrice();
                                if (serverBridge.has(player, price)) {
                                    serverBridge.getEventBus().post(event);
                                    if (event.isCancelled()) {
                                        return true;
                                    }
                                    EconomyResponse er = serverBridge.withdrawPlayer(player, price);

                                    if (!er.transactionSuccess()) {
                                        player.sendMessage(er.errorMessage);
                                        serverBridge.getLogger().warning(er.errorMessage);
                                        return true;
                                    }
                                } else {
                                    player.sendMessage(
                                            C("MsgNotEnoughDeny") + " " + C("WordMissing") + " " + serverBridge.getEconomy().format(price));
                                    return true;
                                }
                            } else {
                                serverBridge.getEventBus().post(event);
                            }

                            if (!event.isCancelled()) {
                                plot.addDenied(denied);
                                plot.removeAllowed(denied);

                                if ("*".equals(denied)) {
                                    List<IPlayer> playersInPlot = manager.getPlayersInPlot(id, world);

                                    for (IPlayer iPlayer : playersInPlot) {
                                        if (!plot.isAllowed(iPlayer.getUniqueId())) {
                                            iPlayer.setLocation(manager.getPlotHome(plot.getId(), player.getWorld()));
                                        }
                                    }
                                } else {
                                    IPlayer deniedPlayer = serverBridge.getPlayer(denied);

                                    if (deniedPlayer != null && deniedPlayer.getWorld().equals(world)) {
                                        PlotId plotId = manager.getPlotId(deniedPlayer);

                                        if (plotId.equals(id)) {
                                            deniedPlayer.setLocation(manager.getPlotHome(plot.getId(), player.getWorld()));
                                        }
                                    }
                                }

                                player.sendMessage(
                                        C("WordPlayer") + " " + denied + " " + C("MsgNowDenied") + " " + serverBridge.getEconomy().format(price));

                                if (isAdvancedLogging()) {
                                    if (price == 0) {
                                        serverBridge.getLogger()
                                                .info(player.getName() + " " + C("MsgDeniedPlayer") + " " + denied + " " + C("MsgToPlot") + " "
                                                        + id);
                                    } else {
                                        serverBridge.getLogger()
                                                .info(player.getName() + " " + C("MsgDeniedPlayer") + " " + denied + " " + C("MsgToPlot") + " "
                                                        + id + (" " + C("WordFor") + " " + price));
                                    }
                                }
                            }
                        }
                    } else {
                        player.sendMessage(C("MsgThisPlot") + "(" + id + ") " + C("MsgNotYoursNotAllowedDeny"));
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
        return C("WordUsage") + ": /plotme deny <" + C("WordPlayer") + ">";
    }
}
