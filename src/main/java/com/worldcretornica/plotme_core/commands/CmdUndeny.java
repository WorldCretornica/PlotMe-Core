package com.worldcretornica.plotme_core.commands;

import com.worldcretornica.plotme_core.PermissionNames;
import com.worldcretornica.plotme_core.Plot;
import com.worldcretornica.plotme_core.PlotMapInfo;
import com.worldcretornica.plotme_core.PlotMe_Core;
import com.worldcretornica.plotme_core.api.CommandExBase;
import com.worldcretornica.plotme_core.api.ICommandSender;
import com.worldcretornica.plotme_core.api.IOfflinePlayer;
import com.worldcretornica.plotme_core.api.IPlayer;
import com.worldcretornica.plotme_core.api.IWorld;
import com.worldcretornica.plotme_core.api.event.PlotRemoveDeniedEvent;
import net.milkbowl.vault.economy.EconomyResponse;

public class CmdUndeny extends PlotCommand {

    public CmdUndeny(PlotMe_Core instance, CommandExBase commandExBase) {
        super(instance);
    }

    public String getName() {
        return "undeny";
    }

    public boolean execute(ICommandSender sender, String[] args) {
        if (args.length != 2) {
            sender.sendMessage(getUsage());
            return true;
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
                    player.sendMessage(C("NoPlotFound"));
                    return true;
                }
                PlotMapInfo pmi = manager.getMap(world);
                String denied = args[1];
                if (player.getUniqueId().equals(plot.getOwnerId()) || player.hasPermission(PermissionNames.ADMIN_DENY)) {
                    if ("*".equals(denied)) {
                        return undenyAll(plot, player, pmi);
                    }
                    IOfflinePlayer offlinePlayer = serverBridge.getOfflinePlayer(denied);
                    if (offlinePlayer == null) {
                        player.sendMessage("An error occured while trying to remove " + args[1]);
                        return true;
                    } else {
                        denied = offlinePlayer.getUniqueId().toString();
                    }
                    if (plot.isDenied(denied)) {
                        double price = 0.0;
                        PlotRemoveDeniedEvent event = new PlotRemoveDeniedEvent(plot, player, denied);

                        if (manager.isEconomyEnabled(pmi)) {
                            price = pmi.getUndenyPlayerPrice();

                            if (serverBridge.has(player, price)) {
                                plugin.getEventBus().post(event);
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
                                player.sendMessage("It costs " + serverBridge.getEconomy().get().format(price) + " to undeny a player from the "
                                        + "plot.");
                                return true;
                            }
                        } else {
                            plugin.getEventBus().post(event);
                        }

                        if (!event.isCancelled()) {
                            plot.removeDenied(denied);
                            plugin.getSqlManager().savePlot(plot);
                            player.sendMessage(args[1] + " " + C("MsgNowUndenied") + " " + serverBridge.getEconomy().get().format
                                            (price));

                            if (isAdvancedLogging()) {
                                if (price != 0) {
                                    serverBridge.getLogger()
                                            .info(player.getName() + " " + C("MsgUndeniedPlayer") + " " + args[1] + " " + C("MsgFromPlot") + " "
                                                    + plot.getId().getID()
                                                    + (" " + C("WordFor") + " " + price));
                                } else {
                                    serverBridge.getLogger()
                                            .info(player.getName() + " " + C("MsgUndeniedPlayer") + " " + args[1] + " " + C("MsgFromPlot") + " "
                                                    + plot.getId().getID());
                                }
                            }
                        }
                    } else {
                        player.sendMessage(args[1] + " " + C("MsgWasNotDenied"));
                    }
                } else {
                    player.sendMessage(C("MsgThisPlot") + " (" + plot.getId().getID() + ") " + C("MsgNotYoursNotAllowedUndeny"));
                }
            } else {
                player.sendMessage(C("NotPlotWorld"));
            }
        } else {
            return false;
        }
        return true;
    }

    private boolean undenyAll(Plot plot, IPlayer player, PlotMapInfo pmi) {
        if (!plot.getDenied().isEmpty()) {
            double price = pmi.getUndenyPlayerPrice();
            PlotRemoveDeniedEvent event = new PlotRemoveDeniedEvent(plot, player, "*");
            if (manager.isEconomyEnabled(pmi)) {

                //noinspection ConstantConditions
                if (serverBridge.has(player, price)) {
                    plugin.getEventBus().post(event);
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
                    player.sendMessage("It costs " + serverBridge.getEconomy().get().format(price) + " to undeny a player from the plot.");
                    return true;
                }
            } else {
                plugin.getEventBus().post(event);
            }

            if (!event.isCancelled()) {
                plot.removeAllDenied();
                plugin.getSqlManager().savePlot(plot);
                player.sendMessage("Undenied all players from the plot.");
                return true;
            }

        }
        return true;
    }

    @Override
    public String getUsage() {
        return C("CmdUndenyUsage");
    }

}
