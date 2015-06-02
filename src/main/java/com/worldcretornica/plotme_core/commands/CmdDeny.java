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
                Plot plot = manager.getPlot(player);
                if (plot == null) {
                    player.sendMessage(C("MsgNoPlotFound"));
                    return true;
                }
                PlotMapInfo pmi = manager.getMap(world);
                String denied;
                IPlayer deniedPlayer = serverBridge.getPlayer(args[1]);
                if ("*".equals(args[1])) {
                    denied = "*";
                } else if (deniedPlayer != null) {
                    denied = deniedPlayer.getUniqueId().toString();
                } else {
                    player.sendMessage(args[1] + " was not found. Are they online?");
                    return true;
                }

                if (player.getUniqueId().equals(plot.getOwnerId()) || player.hasPermission(PermissionNames.ADMIN_DENY)) {
                    if (!"*".equals(denied)) {
                        if (plot.getOwnerId().equals(deniedPlayer.getUniqueId())) {
                            player.sendMessage(C("MsgCannotDenyOwner"));
                            return true;
                        }
                    }

                    if (plot.isDeniedConsulting(denied)) {
                        player.sendMessage(C("WordPlayer") + " " + args[1] + " " + C("MsgAlreadyDenied"));
                    } else {

                        double price = 0.0;

                        PlotAddDeniedEvent event = new PlotAddDeniedEvent(world, plot, player, denied);

                        if (manager.isEconomyEnabled(pmi)) {
                            price = pmi.getDenyPlayerPrice();
                            if (serverBridge.has(player, price)) {
                                plugin.getEventBus().post(event);
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
                                        C("MsgNotEnoughDeny") + " " + C("WordMissing") + " " + serverBridge.getEconomy().get().format(price));
                                return true;
                            }
                        } else {
                            plugin.getEventBus().post(event);
                        }

                        if (!event.isCancelled()) {
                            plot.addDenied(denied);
                            plot.removeMember(denied);

                            if ("*".equals(denied)) {
                                List<IPlayer> playersInPlot = manager.getPlayersInPlot(plot.getId(), world);

                                for (IPlayer iPlayer : playersInPlot) {
                                    if (!plot.isAllowed(iPlayer.getUniqueId())) {
                                        iPlayer.setLocation(manager.getPlotHome(plot.getId(), player.getWorld()));
                                    }
                                }
                            } else {
                                if (deniedPlayer != null && deniedPlayer.getWorld().equals(world)) {
                                    PlotId plotId = manager.getPlotId(deniedPlayer);

                                    if (plot.getId().equals(plotId)) {
                                        deniedPlayer.setLocation(manager.getPlotHome(plot.getId(), player.getWorld()));
                                    }
                                }
                            }

                            player.sendMessage(
                                    C("WordPlayer") + " " + denied + " " + C("MsgNowDenied") + " " + serverBridge.getEconomy().get().format(price));

                            if (isAdvancedLogging()) {
                                if (price == 0) {
                                    serverBridge.getLogger()
                                            .info(player.getName() + " " + C("MsgDeniedPlayer") + " " + denied + " " + C("MsgToPlot") + " "
                                                    + plot.getId().getID());
                                } else {
                                    serverBridge.getLogger()
                                            .info(player.getName() + " " + C("MsgDeniedPlayer") + " " + denied + " " + C("MsgToPlot") + " "
                                                    + plot.getId().getID() + (" " + C("WordFor") + " " + price));
                                }
                            }
                        }
                    }
                } else {
                    player.sendMessage(C("MsgThisPlot") + "(" + plot.getId().getID() + ") " + C("MsgNotYoursNotAllowedDeny"));
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
