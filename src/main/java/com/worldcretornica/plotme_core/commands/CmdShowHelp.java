package com.worldcretornica.plotme_core.commands;

import com.worldcretornica.plotme_core.PermissionNames;
import com.worldcretornica.plotme_core.PlotMapInfo;
import com.worldcretornica.plotme_core.PlotMe_Core;
import com.worldcretornica.plotme_core.api.IPlayer;
import com.worldcretornica.plotme_core.api.IWorld;

import java.util.ArrayList;
import java.util.List;

public class CmdShowHelp extends PlotCommand {

    public CmdShowHelp(PlotMe_Core instance) {
        super(instance);
    }

    public boolean exec(IPlayer player, int page) {

        List<String> allowed_commands = new ArrayList<>();

        allowed_commands.add("limit");
        if (player.hasPermission(PermissionNames.USER_CLAIM)) {
            allowed_commands.add("claim");
        }
        if (player.hasPermission("PlotMe.use.claim.other")) {
            allowed_commands.add("claim.other");
        }
        if (player.hasPermission(PermissionNames.USER_AUTO)) {
            allowed_commands.add("auto");
        }
        if (player.hasPermission(PermissionNames.USER_HOME)) {
            allowed_commands.add("home");
        }
        if (player.hasPermission("PlotMe.use.home.other")) {
            allowed_commands.add("home.other");
        }
        if (player.hasPermission(PermissionNames.USER_INFO)) {
            allowed_commands.add("info");
            allowed_commands.add("biomeinfo");
        }
        if (player.hasPermission(PermissionNames.USER_LIST)) {
            allowed_commands.add("list");
        }
        if (player.hasPermission(PermissionNames.USER_BIOME)) {
            allowed_commands.add("biome");
            allowed_commands.add("biomes");
        }
        if (player.hasPermission(PermissionNames.USER_DONE) || player.hasPermission(PermissionNames.ADMIN_DONE)) {
            allowed_commands.add("done");
        }
        if (player.hasPermission(PermissionNames.ADMIN_DONE)) {
            allowed_commands.add("donelist");
        }
        if (player.hasPermission(PermissionNames.ADMIN_TP)) {
            allowed_commands.add("tp");
        }
        if (player.hasPermission(PermissionNames.USER_CLEAR) || player.hasPermission(PermissionNames.ADMIN_CLEAR)) {
            allowed_commands.add("clear");
        }
        if (player.hasPermission(PermissionNames.ADMIN_DISPOSE) || player.hasPermission(PermissionNames.USER_DISPOSE)) {
            allowed_commands.add("dispose");
        }
        if (player.hasPermission(PermissionNames.ADMIN_RESET)) {
            allowed_commands.add("reset");
        }
        if (player.hasPermission(PermissionNames.USER_ADD) || player.hasPermission(PermissionNames.ADMIN_ADD)) {
            allowed_commands.add("add");
        }
        if (player.hasPermission(PermissionNames.USER_REMOVE) || player.hasPermission(PermissionNames.ADMIN_REMOVE)) {
            allowed_commands.add("remove");
        }
        if (player.hasPermission(PermissionNames.USER_DENY) || player.hasPermission(PermissionNames.ADMIN_DENY)) {
            allowed_commands.add("deny");
        }
        if (player.hasPermission(PermissionNames.USER_UNDENY) || player.hasPermission(PermissionNames.ADMIN_UNDENY)) {
            allowed_commands.add("undeny");
        }
        if (player.hasPermission(PermissionNames.ADMIN_SETOWNER)) {
            allowed_commands.add("setowner");
        }
        if (player.hasPermission(PermissionNames.ADMIN_MOVE)) {
            allowed_commands.add("move");
        }
        if (player.hasPermission(PermissionNames.ADMIN_WEANYWHERE)) {
            allowed_commands.add("weanywhere");
        }
        if (player.hasPermission(PermissionNames.ADMIN_LIST)) {
            allowed_commands.add("listother");
        }
        if (player.hasPermission(PermissionNames.ADMIN_EXPIRED)) {
            allowed_commands.add("expired");
        }
        if (player.hasPermission(PermissionNames.ADMIN_ADDTIME)) {
            allowed_commands.add("addtime");
        }

        PlotMapInfo pmi = manager.getMap(player);

        boolean economyEnabled = pmi != null && manager.isEconomyEnabled(pmi);

        if (manager.isPlotWorld(player) && economyEnabled) {
            if (player.hasPermission(PermissionNames.USER_BUY)) {
                allowed_commands.add("buy");
            }
            if (player.hasPermission(PermissionNames.USER_SELL)) {
                allowed_commands.add("sell");
            }
            if (player.hasPermission(PermissionNames.USE_AUCTION)) {
                allowed_commands.add("auction");
            }
            if (player.hasPermission(PermissionNames.PLOT_ME_USE_BID)) {
                allowed_commands.add("bid");
            }
        }

        int maxPage = (int) Math.ceil(allowed_commands.size() / 4);

        if (page > maxPage) {
            page = 1;
        }

        player.sendMessage("§c ---==§b" + C("HelpTitle") + " " + page + "/" + maxPage + "§c==--- ");

        for (int ctr = (page - 1) * 4; ctr < (page * 4) && ctr < allowed_commands.size(); ctr++) {
            String allowedCommand = allowed_commands.get(ctr);

            if ("limit".equalsIgnoreCase(allowedCommand)) {
                int plotLimit = getPlotLimit(player);
                if (manager.isPlotWorld(player)) {

                    IWorld world = player.getWorld();

                    short ownedPlots = manager.getNbOwnedPlot(player.getUniqueId(), world.getName().toLowerCase());

                    if (plotLimit == -1) {
                        player.sendMessage(
                                "§a" + C("HelpYourPlotLimitWorld") + " : §b" + ownedPlots + "§a " + C("HelpUsedOf") + " §b" + C("WordInfinite"));
                    } else {
                        player.sendMessage("§a" + C("HelpYourPlotLimitWorld") + " : §b" + ownedPlots + "§a " + C("HelpUsedOf") + " §b" + plotLimit);
                    }
                } else if (serverBridge.getConfig().getBoolean("allowWorldTeleport")) {

                    IWorld world = manager.getFirstWorld();

                    if (world != null) {
                        short ownedPlots = manager.getNbOwnedPlot(player.getUniqueId(), world.getName().toLowerCase());

                        if (plotLimit == -1) {
                            player.sendMessage(
                                    "§a" + C("HelpYourPlotLimitWorld") + " : §b" + ownedPlots + "§a " + C("HelpUsedOf") + " §b" + C("WordInfinite"));
                        } else {
                            player.sendMessage("§a" + C("HelpYourPlotLimitWorld") + " : §b" + ownedPlots + "§a " + C("HelpUsedOf") + " §b" + plotLimit);
                        }
                    } else {
                        player.sendMessage("PlotMe can't find any plotworlds. Make sure you configured it correctly.");
                    }
                } else {
                    player.sendMessage("§a" + C("HelpYourPlotLimitWorld") + " : §b" + C("MsgNotPlotWorld"));
                }
            } else if ("claim".equalsIgnoreCase(allowedCommand)) {
                player.sendMessage("§a /plotme claim");
                if (economyEnabled && pmi != null && pmi.getClaimPrice() != 0) {
                    player.sendMessage("§b " + C("HelpClaim") + " " + C("WordPrice") + " : §r" + Math.round(pmi.getClaimPrice()));
                } else {
                    player.sendMessage("§b " + C("HelpClaim"));
                }
            } else if ("claim.other".equalsIgnoreCase(allowedCommand)) {
                player.sendMessage("§a /plotme claim <" + C("WordPlayer") + ">");
                if (economyEnabled && pmi != null && pmi.getClaimPrice() != 0) {
                    player.sendMessage("§b " + C("HelpClaimOther") + " " + C("WordPrice") + " : §r" + Math.round(pmi.getClaimPrice()));
                } else {
                    player.sendMessage("§b " + C("HelpClaimOther"));
                }
            } else if ("auto".equalsIgnoreCase(allowedCommand)) {
                if (serverBridge.getConfig().getBoolean("allowWorldTeleport")) {
                    player.sendMessage("§a /plotme auto [" + C("WordWorld") + "]");
                } else {
                    player.sendMessage("§a /plotme auto");
                }

                if (economyEnabled && pmi != null && pmi.getClaimPrice() != 0) {
                    player.sendMessage("§b " + C("HelpAuto") + " " + C("WordPrice") + " : §r" + Math.round(pmi.getClaimPrice()));
                } else {
                    player.sendMessage("§b " + C("HelpAuto"));
                }
            } else if ("home".equalsIgnoreCase(allowedCommand)) {
                if (serverBridge.getConfig().getBoolean("allowWorldTeleport")) {
                    player.sendMessage("§a /plotme home[:#] [" + C("WordWorld") + "]");
                } else {
                    player.sendMessage("§a /plotme home[:#]");
                }

                if (economyEnabled && pmi != null && pmi.getPlotHomePrice() != 0) {
                    player.sendMessage("§b " + C("HelpHome") + " " + C("WordPrice") + " : §r" + Math.round(pmi.getPlotHomePrice()));
                } else {
                    player.sendMessage("§b " + C("HelpHome"));
                }
            } else if ("home.other".equalsIgnoreCase(allowedCommand)) {
                if (serverBridge.getConfig().getBoolean("allowWorldTeleport")) {
                    player.sendMessage("§a /plotme home[:#] <" + C("WordPlayer") + "> [" + C("WordWorld") + "]");
                } else {
                    player.sendMessage("§a /plotme home[:#] <" + C("WordPlayer") + ">");
                }

                if (economyEnabled && pmi != null && pmi.getPlotHomePrice() != 0) {
                    player.sendMessage("§b " + C("HelpHomeOther") + " " + C("WordPrice") + " : §r" + Math.round(pmi.getPlotHomePrice()));
                } else {
                    player.sendMessage("§b " + C("HelpHomeOther"));
                }
            } else if ("info".equalsIgnoreCase(allowedCommand)) {
                player.sendMessage("§a /plotme info");
                player.sendMessage("§b " + C("HelpInfo"));
            } else if ("list".equalsIgnoreCase(allowedCommand)) {
                player.sendMessage("§a /plotme list");
                player.sendMessage("§b " + C("HelpList"));
            } else if ("listother".equalsIgnoreCase(allowedCommand)) {
                player.sendMessage("§a /plotme list <" + C("WordPlayer") + ">");
                player.sendMessage("§b " + C("HelpListOther"));
            } else if ("biome".equalsIgnoreCase(allowedCommand)) {
                player.sendMessage("§a /plotme biome <" + C("WordBiome") + ">");
                if (economyEnabled && pmi != null && pmi.getBiomeChangePrice() != 0) {
                    player.sendMessage("§b " + C("HelpBiome") + " " + C("WordPrice") + " : §r" + Math.round(pmi.getBiomeChangePrice()));
                } else {
                    player.sendMessage("§b " + C("HelpBiome"));
                }
            } else if ("biomes".equalsIgnoreCase(allowedCommand)) {
                player.sendMessage("§a /plotme biomes");
                player.sendMessage("§b " + C("HelpBiomeList"));
            } else if ("done".equalsIgnoreCase(allowedCommand)) {
                player.sendMessage("§a /plotme done");
                player.sendMessage("§b " + C("HelpDone"));
            } else if ("tp".equalsIgnoreCase(allowedCommand)) {
                if (serverBridge.getConfig().getBoolean("allowWorldTeleport")) {
                    player.sendMessage("§a /plotme tp <ID> [" + C("WordWorld") + "]");
                } else {
                    player.sendMessage("§a /plotme tp <ID>");
                }
                player.sendMessage("§b " + C("HelpTp"));
            } else if ("clear".equalsIgnoreCase(allowedCommand)) {
                player.sendMessage("§a /plotme clear");
                if (economyEnabled && pmi != null && pmi.getClearPrice() != 0) {
                    player.sendMessage("§b " + C("HelpId") + " " + C("WordPrice") + " : §r" + Math.round(pmi.getClearPrice()));
                } else {
                    player.sendMessage("§b " + C("HelpClear"));
                }
            } else if ("reset".equalsIgnoreCase(allowedCommand)) {
                player.sendMessage("§a /plotme reset");
                player.sendMessage("§b " + C("HelpReset"));
            } else if ("add".equalsIgnoreCase(allowedCommand)) {
                player.sendMessage("§a /plotme add <" + C("WordPlayer") + ">");
                if (economyEnabled && pmi != null && pmi.getAddPlayerPrice() != 0) {
                    player.sendMessage("§b " + C("HelpAdd") + " " + C("WordPrice") + " : §r" + Math.round(pmi.getAddPlayerPrice()));
                } else {
                    player.sendMessage("§b " + C("HelpAdd"));
                }
            } else if ("deny".equalsIgnoreCase(allowedCommand)) {
                player.sendMessage("§a /plotme deny <" + C("WordPlayer") + ">");
                if (economyEnabled && pmi != null && pmi.getDenyPlayerPrice() != 0) {
                    player.sendMessage("§b " + C("HelpDeny") + " " + C("WordPrice") + " : §r" + Math.round(pmi.getDenyPlayerPrice()));
                } else {
                    player.sendMessage("§b " + C("HelpDeny"));
                }
            } else if ("remove".equalsIgnoreCase(allowedCommand)) {
                player.sendMessage("§a /plotme remove <" + C("WordPlayer") + ">");
                if (economyEnabled && pmi != null && pmi.getRemovePlayerPrice() != 0) {
                    player.sendMessage("§b " + C("HelpRemove") + " " + C("WordPrice") + " : §r" + Math.round(pmi.getRemovePlayerPrice()));
                } else {
                    player.sendMessage("§b " + C("HelpRemove"));
                }
            } else if ("undeny".equalsIgnoreCase(allowedCommand)) {
                player.sendMessage("§a /plotme undeny <" + C("WordPlayer") + ">");
                if (economyEnabled && pmi != null && pmi.getUndenyPlayerPrice() != 0) {
                    player.sendMessage("§b " + C("HelpUndeny") + " " + C("WordPrice") + " : §r" + Math.round(pmi.getUndenyPlayerPrice()));
                } else {
                    player.sendMessage("§b " + C("HelpUndeny"));
                }
            } else if ("setowner".equalsIgnoreCase(allowedCommand)) {
                player.sendMessage("§a /plotme setowner <" + C("WordPlayer") + ">");
                player.sendMessage("§b " + C("HelpSetowner"));
            } else if ("move".equalsIgnoreCase(allowedCommand)) {
                player.sendMessage("§a /plotme move <" + C("WordIdFrom") + "> <" + C("WordIdTo") + ">");
                player.sendMessage("§b " + C("HelpMove"));
            } else if ("weanywhere".equalsIgnoreCase(allowedCommand)) {
                player.sendMessage("§a /plotme weanywhere");
                player.sendMessage("§b " + C("HelpWEAnywhere"));
            } else if ("expired".equalsIgnoreCase(allowedCommand)) {
                player.sendMessage("§a /plotme expired [page]");
                player.sendMessage("§b " + C("HelpExpired"));
            } else if ("donelist".equalsIgnoreCase(allowedCommand)) {
                player.sendMessage("§a /plotme donelist [page]");
                player.sendMessage("§b " + C("HelpDoneList"));
            } else if ("addtime".equalsIgnoreCase(allowedCommand)) {
                player.sendMessage("§a /plotme addtime");
                int days;
                if (pmi == null) {
                    days = 0;
                } else {
                    days = pmi.getDaysToExpiration();
                }
                if (days != 0) {
                    player.sendMessage("§b " + C("HelpAddTime1") + " §r" + days + "§b " + C("HelpAddTime2"));
                }
            } else if ("dispose".equalsIgnoreCase(allowedCommand)) {
                player.sendMessage("§a /plotme dispose");
                if (economyEnabled && pmi != null && pmi.getDisposePrice() != 0) {
                    player.sendMessage("§b " + C("HelpDispose") + " " + C("WordPrice") + " : §r" + Math.round(pmi.getDisposePrice()));
                } else {
                    player.sendMessage("§b " + C("HelpDispose"));
                }
            } else if ("buy".equalsIgnoreCase(allowedCommand)) {
                player.sendMessage("§a /plotme buy");
                player.sendMessage("§b " + C("HelpBuy"));
            } else if ("sell".equalsIgnoreCase(allowedCommand)) {
                player.sendMessage("§a /plotme sell [" + C("WordAmount") + "]");
                assert pmi != null;
                player.sendMessage("§b " + C("HelpSell") + " " + C("WordDefault") + " : §r" + Math.round(pmi.getSellToPlayerPrice()));
            } else if ("auction".equalsIgnoreCase(allowedCommand)) {
                player.sendMessage("§a /plotme auction [" + C("WordAmount") + "]");
                player.sendMessage("§b " + C("HelpAuction") + " " + C("WordDefault") + " : §r1");
            } else if ("bid".equalsIgnoreCase(allowedCommand)) {
                player.sendMessage("§a /plotme bid <" + C("WordAmount") + ">");
                player.sendMessage("§b " + C("HelpBid"));
            }
        }
        return true;
    }
}
