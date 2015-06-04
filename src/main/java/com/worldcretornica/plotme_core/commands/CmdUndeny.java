package com.worldcretornica.plotme_core.commands;

import com.worldcretornica.plotme_core.PermissionNames;
import com.worldcretornica.plotme_core.Plot;
import com.worldcretornica.plotme_core.PlotId;
import com.worldcretornica.plotme_core.PlotMapInfo;
import com.worldcretornica.plotme_core.PlotMe_Core;
import com.worldcretornica.plotme_core.api.ICommandSender;
import com.worldcretornica.plotme_core.api.IPlayer;
import com.worldcretornica.plotme_core.api.IWorld;
import com.worldcretornica.plotme_core.api.event.PlotRemoveDeniedEvent;
import net.milkbowl.vault.economy.EconomyResponse;

public class CmdUndeny extends PlotCommand {

    public CmdUndeny(PlotMe_Core instance) {
        super(instance);
    }

    public String getName() {
        return "undeny";
    }

    public boolean execute(ICommandSender sender, String[] args) throws Exception {
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
                if (!manager.isPlotAvailable(id, world)) {
                    Plot plot = manager.getPlotById(id, world);
                    String denied = args[1];
                    if (player.getUniqueId().equals(plot.getOwnerId()) || player.hasPermission(PermissionNames.ADMIN_DENY)) {
                        if ("*".equals(denied)) {
                            return undenyAll(plot, player, pmi);
                        }
                        if (plot.isDeniedConsulting(denied)) {
                            double price = 0.0;
                            PlotRemoveDeniedEvent event = new PlotRemoveDeniedEvent(world, plot, player, denied);

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
                                if ("*".equals(denied)) {
                                    plot.removeAllAllowed();
                                } else {
                                    plot.removeDenied(denied);
                                }
                                player.sendMessage(
                                        C("WordPlayer") + " " + denied + " " + C("MsgNowUndenied") + " " + serverBridge.getEconomy().get().format
                                                (price));

                                if (isAdvancedLogging()) {
                                    if (price != 0) {
                                        serverBridge.getLogger()
                                                .info(player.getName() + " " + C("MsgUndeniedPlayer") + " " + denied + " " + C("MsgFromPlot") + " "
                                                        + id
                                                        + (" " + C("WordFor") + " " + price));
                                    } else {
                                        serverBridge.getLogger()
                                                .info(player.getName() + " " + C("MsgUndeniedPlayer") + " " + denied + " " + C("MsgFromPlot") + " "
                                                        + id);
                                    }
                                }
                            }
                        } else {
                            player.sendMessage(C("WordPlayer") + " " + args[1] + " " + C("MsgWasNotDenied"));
                        }
                    } else {
                        player.sendMessage(C("MsgThisPlot") + " (" + id + ") " + C("MsgNotYoursNotAllowedUndeny"));
                    }
                } else {
                    player.sendMessage(C("MsgThisPlot") + " (" + id + ") " + C("MsgHasNoOwner"));
                }
            } else {
                player.sendMessage(C("MsgNotPlotWorld"));
            }
        } else {
            return false;
        }
        return true;
    }

    private boolean undenyAll(Plot plot, IPlayer player, PlotMapInfo pmi) {
        if (plot.getDenied().size() > 0) {
            double price = pmi.getUndenyPlayerPrice();
            PlotRemoveDeniedEvent event = new PlotRemoveDeniedEvent(player.getWorld(), plot, player, "*");
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
                player.sendMessage("Undenied all players from the plot.");
                return true;
            }

        }
        return true;
    }

    @Override
    public String getUsage() {
        return C("WordUsage") + ": /plotme undeny <" + C("WordPlayer") + ">";
    }

}
