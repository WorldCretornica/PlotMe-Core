package com.worldcretornica.plotme_core.commands;

import com.worldcretornica.plotme_core.PermissionNames;
import com.worldcretornica.plotme_core.Plot;
import com.worldcretornica.plotme_core.PlotId;
import com.worldcretornica.plotme_core.PlotMapInfo;
import com.worldcretornica.plotme_core.PlotMe_Core;
import com.worldcretornica.plotme_core.api.CommandExBase;
import com.worldcretornica.plotme_core.api.ICommandSender;
import com.worldcretornica.plotme_core.api.IPlayer;
import com.worldcretornica.plotme_core.api.IWorld;
import com.worldcretornica.plotme_core.api.event.PlotAddDeniedEvent;
import net.milkbowl.vault.economy.EconomyResponse;

import java.util.List;

public class CmdDeny extends PlotCommand {

    public CmdDeny(PlotMe_Core instance, CommandExBase commandExBase) {
        super(instance);
    }

    public String getName() {
        return "deny";
    }

    public boolean execute(ICommandSender sender, String[] args) {
        if (args.length != 2) {
            sender.sendMessage(getUsage());
            return true;
        }
        if ("*".equals(args[1]) && plugin.getConfig().getBoolean("disableWildCard")) {
            sender.sendMessage(C("WildcardsDisabled"));
            return true;
        }
        IPlayer player = (IPlayer) sender;
        if (player.hasPermission(PermissionNames.ADMIN_DENY) || player.hasPermission(PermissionNames.USER_DENY)) {
            IWorld world = player.getWorld();
            if (manager.isPlotWorld(player)) {
                Plot plot = manager.getPlot(player);
                if (plot == null) {
                    player.sendMessage(C("NoPlotFound"));
                    return true;
                }
                PlotMapInfo pmi = manager.getMap(player);
                String denied;
                IPlayer deniedPlayer = serverBridge.getPlayer(args[1]);
                if ("*".equals(args[1])) {
                    denied = "*";
                } else if (deniedPlayer == null) {
                    player.sendMessage(args[1] + " was not found. Are they online?");
                    return true;
                } else {
                    denied = deniedPlayer.getUniqueId().toString();
                }

                if (player.getUniqueId().equals(plot.getOwnerId()) || player.hasPermission(PermissionNames.ADMIN_DENY)) {
                    if (!"*".equals(denied)) {
                        if (plot.getOwnerId().equals(deniedPlayer.getUniqueId())) {
                            player.sendMessage(C("MsgCannotDenyOwner"));
                            return true;
                        }
                    }

                    if (plot.isDenied(denied)) {
                        player.sendMessage(C("PlayerAlreadyDenied", args[1]));
                    } else {
                        double price;

                        PlotAddDeniedEvent event = new PlotAddDeniedEvent(plot, player, denied);

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
                                    plugin.getLogger().warning(er.errorMessage);
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
                                List<IPlayer> playersInPlot = manager.getPlayersInPlot(plot.getId(), plot.getWorld());

                                for (IPlayer iPlayer : playersInPlot) {
                                    if (plot.isMember(iPlayer.getUniqueId()).isPresent()) {
                                        continue;
                                    }
                                    iPlayer.setLocation(manager.getPlotHome(plot.getId(), player.getWorld()));
                                }
                                player.sendMessage(denied + " " + C("NowDenied", "*"));
                            } else if (deniedPlayer.getWorld().equals(plot.getWorld())) {
                                PlotId plotId = manager.getPlotId(deniedPlayer);

                                if (plot.getId().equals(plotId)) {
                                    deniedPlayer.setLocation(manager.getPlotHome(plot.getId(), player.getWorld()));
                                }
                                player.sendMessage(C("NowDenied", args[1]));
                            }
                            plugin.getSqlManager().savePlot(plot);

                            if (isAdvancedLogging()) {
                                plugin.getLogger()
                                        .info(player.getName() + " " + C("MsgDeniedPlayer") + " " + args[1] + " " + C("MsgToPlot") + " "
                                                + plot.getId().getID());
                            }
                        }
                    }
                } else {
                    player.sendMessage(C("MsgThisPlot") + "(" + plot.getId().getID() + ") " + C("MsgNotYoursNotAllowedDeny"));
                }
            } else {
                player.sendMessage(C("NotPlotWorld"));
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
