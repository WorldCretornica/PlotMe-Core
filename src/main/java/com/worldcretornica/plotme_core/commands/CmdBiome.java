package com.worldcretornica.plotme_core.commands;

import com.google.common.base.Optional;
import com.worldcretornica.plotme_core.PermissionNames;
import com.worldcretornica.plotme_core.Plot;
import com.worldcretornica.plotme_core.PlotMapInfo;
import com.worldcretornica.plotme_core.PlotMe_Core;
import com.worldcretornica.plotme_core.api.ICommandSender;
import com.worldcretornica.plotme_core.api.IPlayer;
import com.worldcretornica.plotme_core.api.IWorld;
import com.worldcretornica.plotme_core.api.event.PlotBiomeChangeEvent;
import net.milkbowl.vault.economy.EconomyResponse;

public class CmdBiome extends PlotCommand {

    public CmdBiome(PlotMe_Core instance) {
        super(instance);
    }

    public String getName() {
        return "biome";
    }

    public boolean execute(ICommandSender sender, String[] args) {
        if (args.length > 4) {
            sender.sendMessage(getUsage());
            return true;
        }

        IPlayer player = (IPlayer) sender;
        if (player.hasPermission(PermissionNames.USER_BIOME)) {
            IWorld world = player.getWorld();
            PlotMapInfo pmi = manager.getMap(world);
            if (manager.isPlotWorld(world)) {
                Plot plot = manager.getPlot(player);
                if (plot != null) {
                    Optional<String> biome = Optional.absent();
                    if (args.length == 2) {
                        biome = serverBridge.getBiome(args[1]);
                    } else if (args.length == 3) {
                        biome = serverBridge.getBiome(args[1] + " " + args[2]);
                    } else if (args.length == 4) {
                        biome = serverBridge.getBiome(args[1] + " " + args[2] + " " + args[3]);
                    }
                    if (!biome.isPresent()) {
                        player.sendMessage(biome.get() + " " + C("MsgIsInvalidBiome"));
                        return true;
                    }

                    String playerName = player.getName();

                    if (player.getUniqueId().equals(plot.getOwnerId()) || player.hasPermission("PlotMe.admin")) {

                        double price = 0.0;

                        PlotBiomeChangeEvent event = new PlotBiomeChangeEvent(plot, player, biome.get());
                        plugin.getEventBus().post(event);

                        if (manager.isEconomyEnabled(pmi)) {
                            price = pmi.getBiomeChangePrice();

                            if (serverBridge.has(player, price)) {
                                player.sendMessage("It costs " + serverBridge.getEconomy().get().format(price) + " to change the biome.");
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
                            plot.setBiome(biome.get());
                            manager.setBiome(plot);
                            plugin.getSqlManager().savePlot(plot);

                            player.sendMessage(C("MsgBiomeSet") + " " + biome.get());

                            if (isAdvancedLogging()) {
                                if (price == 0) {
                                    serverBridge.getLogger()
                                            .info(playerName + " " + C("MsgChangedBiome") + " " + plot.getId() + " " + C("WordTo") + " "
                                                    + biome.get());
                                } else {
                                    serverBridge.getLogger()
                                            .info(playerName + " " + C("MsgChangedBiome") + " " + plot.getId() + " " + C("WordTo") + " "
                                                    + biome.get() + (" " + C("WordFor") + " " + price));
                                }
                            }
                        }
                    } else {
                        player.sendMessage(C("MsgThisPlot") + "(" + plot.getId() + ") " + C("MsgNotYoursNotAllowedBiome"));
                    }
                } else {
                    player.sendMessage(C("MsgThisPlot") + C("MsgHasNoOwner"));
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
        return C("CmdBiomeUsage");
    }

}