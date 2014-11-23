package com.worldcretornica.plotme_core.commands;

import com.worldcretornica.plotme_core.PlotMapInfo;
import com.worldcretornica.plotme_core.PlotMe_Core;
import com.worldcretornica.plotme_core.api.IPlayer;
import com.worldcretornica.plotme_core.api.IWorld;
import com.worldcretornica.plotme_core.utils.Util;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("SameReturnValue")
public class CmdShowHelp extends PlotCommand {

    public CmdShowHelp(PlotMe_Core instance) {
        super(instance);
    }

    public boolean exec(IPlayer player, int page) {

        List<String> allowed_commands = new ArrayList<>();

        allowed_commands.add("limit");
        if (player.hasPermission("PlotMe.use.claim")) {
            allowed_commands.add("claim");
        }
        if (player.hasPermission("PlotMe.use.claim.other")) {
            allowed_commands.add("claim.other");
        }
        if (player.hasPermission("PlotMe.use.auto")) {
            allowed_commands.add("auto");
        }
        if (player.hasPermission("PlotMe.use.home")) {
            allowed_commands.add("home");
        }
        if (player.hasPermission("PlotMe.use.home.other")) {
            allowed_commands.add("home.other");
        }
        if (player.hasPermission("PlotMe.use.info")) {
            allowed_commands.add("info");
            allowed_commands.add("biomeinfo");
        }
        if (player.hasPermission("PlotMe.use.list")) {
            allowed_commands.add("list");
        }
        if (player.hasPermission("PlotMe.use.biome")) {
            allowed_commands.add("biome");
            allowed_commands.add("biomelist");
        }
        if (player.hasPermission("PlotMe.use.done") || player.hasPermission("PlotMe.admin.done")) {
            allowed_commands.add("done");
        }
        if (player.hasPermission("PlotMe.admin.done")) {
            allowed_commands.add("donelist");
        }
        if (player.hasPermission("PlotMe.admin.tp")) {
            allowed_commands.add("tp");
        }
        if (player.hasPermission("PlotMe.use.clear") || player.hasPermission("PlotMe.admin.clear")) {
            allowed_commands.add("clear");
        }
        if (player.hasPermission("PlotMe.admin.dispose") || player.hasPermission("PlotMe.use.dispose")) {
            allowed_commands.add("dispose");
        }
        if (player.hasPermission("PlotMe.admin.reset")) {
            allowed_commands.add("reset");
        }
        if (player.hasPermission("PlotMe.use.add") || player.hasPermission("PlotMe.admin.add")) {
            allowed_commands.add("add");
        }
        if (player.hasPermission("PlotMe.use.remove") || player.hasPermission("PlotMe.admin.remove")) {
            allowed_commands.add("remove");
        }
        if (player.hasPermission("PlotMe.use.deny") || player.hasPermission("PlotMe.admin.deny")) {
            allowed_commands.add("deny");
        }
        if (player.hasPermission("PlotMe.use.undeny") || player.hasPermission("PlotMe.admin.undeny")) {
            allowed_commands.add("undeny");
        }
        if (player.hasPermission("PlotMe.admin.setowner")) {
            allowed_commands.add("setowner");
        }
        if (player.hasPermission("PlotMe.admin.move")) {
            allowed_commands.add("move");
        }
        if (player.hasPermission("PlotMe.admin.weanywhere")) {
            allowed_commands.add("weanywhere");
        }
        if (player.hasPermission("PlotMe.admin.list")) {
            allowed_commands.add("listother");
        }
        if (player.hasPermission("PlotMe.admin.expired")) {
            allowed_commands.add("expired");
        }
        if (player.hasPermission("PlotMe.admin.addtime")) {
            allowed_commands.add("addtime");
        }

        PlotMapInfo pmi = plugin.getPlotMeCoreManager().getMap(player);
        boolean ecoon = plugin.getPlotMeCoreManager().isEconomyEnabled(pmi);

        if (plugin.getPlotMeCoreManager().isPlotWorld(player) && ecoon) {
            if (player.hasPermission("PlotMe.use.buy")) {
                allowed_commands.add("buy");
            }
            if (player.hasPermission("PlotMe.use.sell")) {
                allowed_commands.add("sell");
                if (pmi.isCanSellToBank()) {
                    allowed_commands.add("sellbank");
                }
            }
            if (player.hasPermission("PlotMe.use.auction")) {
                allowed_commands.add("auction");
            }
            if (player.hasPermission("PlotMe.use.bid")) {
                allowed_commands.add("bid");
            }
        }

        int maxpage = (int) Math.ceil((double) allowed_commands.size() / 4);

        if (page > maxpage) {
            page = 1;
        }

        player.sendMessage("§c ---==§b" + C("HelpTitle") + " " + page + "/" + maxpage + "§c==--- ");

        for (int ctr = (page - 1) * 4; ctr < (page * 4) && ctr < allowed_commands.size(); ctr++) {
            String allowedcmd = allowed_commands.get(ctr);

            if ("limit".equalsIgnoreCase(allowedcmd)) {
                boolean plotWorld = plugin.getPlotMeCoreManager().isPlotWorld(player);
                if (plotWorld || serverBridge.getConfig().getBoolean("allowWorldTeleport")) {
                    IWorld world = null;

                    if (plotWorld) {
                        world = player.getWorld();
                    } else if (serverBridge.getConfig().getBoolean("allowWorldTeleport")) {
                        world = plugin.getPlotMeCoreManager().getFirstWorld();
                    }

                    int maxplots = getPlotLimit(player);
                    int ownedplots = plugin.getPlotMeCoreManager().getNbOwnedPlot(player, world);

                    if (maxplots == -1) {
                        player.sendMessage("§a" + C("HelpYourPlotLimitWorld") + " : §b" + ownedplots + "§a " + C("HelpUsedOf") + " §b" + C("WordInfinite"));
                    } else {
                        player.sendMessage("§a" + C("HelpYourPlotLimitWorld") + " : §b" + ownedplots + "§a " + C("HelpUsedOf") + " §b" + maxplots);
                    }
                } else {
                    player.sendMessage("§a" + C("HelpYourPlotLimitWorld") + " : §b" + C("MsgNotPlotWorld"));
                }
            } else if ("claim".equalsIgnoreCase(allowedcmd)) {
                player.sendMessage("§a /plotme claim");
                if (ecoon && pmi != null && pmi.getClaimPrice() != 0) {
                    player.sendMessage("§b " + C("HelpClaim") + " " + C("WordPrice") + " : §r" + Util.round(pmi.getClaimPrice()));
                } else {
                    player.sendMessage("§b " + C("HelpClaim"));
                }
            } else if ("claim.other".equalsIgnoreCase(allowedcmd)) {
                player.sendMessage("§a /plotme claim <" + C("WordPlayer") + ">");
                if (ecoon && pmi != null && pmi.getClaimPrice() != 0) {
                    player.sendMessage("§b " + C("HelpClaimOther") + " " + C("WordPrice") + " : §r" + Util.round(pmi.getClaimPrice()));
                } else {
                    player.sendMessage("§b " + C("HelpClaimOther"));
                }
            } else if ("auto".equalsIgnoreCase(allowedcmd)) {
                if (serverBridge.getConfig().getBoolean("allowWorldTeleport")) {
                    player.sendMessage("§a /plotme auto [" + C("WordWorld") + "]");
                } else {
                    player.sendMessage("§a /plotme auto");
                }

                if (ecoon && pmi != null && pmi.getClaimPrice() != 0) {
                    player.sendMessage("§b " + C("HelpAuto") + " " + C("WordPrice") + " : §r" + Util.round(pmi.getClaimPrice()));
                } else {
                    player.sendMessage("§b " + C("HelpAuto"));
                }
            } else if ("home".equalsIgnoreCase(allowedcmd)) {
                if (serverBridge.getConfig().getBoolean("allowWorldTeleport")) {
                    player.sendMessage("§a /plotme home[:#] [" + C("WordWorld") + "]");
                } else {
                    player.sendMessage("§a /plotme home[:#]");
                }

                if (ecoon && pmi != null && pmi.getPlotHomePrice() != 0) {
                    player.sendMessage("§b " + C("HelpHome") + " " + C("WordPrice") + " : §r" + Util.round(pmi.getPlotHomePrice()));
                } else {
                    player.sendMessage("§b " + C("HelpHome"));
                }
            } else if ("home.other".equalsIgnoreCase(allowedcmd)) {
                if (serverBridge.getConfig().getBoolean("allowWorldTeleport")) {
                    player.sendMessage("§a /plotme home[:#] <" + C("WordPlayer") + "> [" + C("WordWorld") + "]");
                } else {
                    player.sendMessage("§a /plotme home[:#] <" + C("WordPlayer") + ">");
                }

                if (ecoon && pmi != null && pmi.getPlotHomePrice() != 0) {
                    player.sendMessage("§b " + C("HelpHomeOther") + " " + C("WordPrice") + " : §r" + Util.round(pmi.getPlotHomePrice()));
                } else {
                    player.sendMessage("§b " + C("HelpHomeOther"));
                }
            } else if ("info".equalsIgnoreCase(allowedcmd)) {
                player.sendMessage("§a /plotme info");
                player.sendMessage("§b " + C("HelpInfo"));
            } else if ("list".equalsIgnoreCase(allowedcmd)) {
                player.sendMessage("§a /plotme list");
                player.sendMessage("§b " + C("HelpList"));
            } else if ("listother".equalsIgnoreCase(allowedcmd)) {
                player.sendMessage("§a /plotme list <" + C("WordPlayer") + ">");
                player.sendMessage("§b " + C("HelpListOther"));
            } else if ("biomeinfo".equalsIgnoreCase(allowedcmd)) {
                player.sendMessage("§a /plotme biome");
                player.sendMessage("§b " + C("HelpBiomeInfo"));
            } else if ("biome".equalsIgnoreCase(allowedcmd)) {
                player.sendMessage("§a /plotme biome <" + C("WordBiome") + ">");
                if (ecoon && pmi != null && pmi.getBiomeChangePrice() != 0) {
                    player.sendMessage("§b " + C("HelpBiome") + " " + C("WordPrice") + " : §r" + Util.round(pmi.getBiomeChangePrice()));
                } else {
                    player.sendMessage("§b " + C("HelpBiome"));
                }
            } else if ("biomelist".equalsIgnoreCase(allowedcmd)) {
                player.sendMessage("§a /plotme biomelist");
                player.sendMessage("§b " + C("HelpBiomeList"));
            } else if ("done".equalsIgnoreCase(allowedcmd)) {
                player.sendMessage("§a /plotme done");
                player.sendMessage("§b " + C("HelpDone"));
            } else if ("tp".equalsIgnoreCase(allowedcmd)) {
                if (serverBridge.getConfig().getBoolean("allowWorldTeleport")) {
                    player.sendMessage("§a /plotme tp <ID> [" + C("WordWorld") + "]");
                } else {
                    player.sendMessage("§a /plotme tp <ID>");
                }

                player.sendMessage("§b " + C("HelpTp"));
            } else if ("clear".equalsIgnoreCase(allowedcmd)) {
                player.sendMessage("§a /plotme clear");
                if (ecoon && pmi != null && pmi.getClearPrice() != 0) {
                    player.sendMessage("§b " + C("HelpId") + " " + C("WordPrice") + " : §r" + Util.round(pmi.getClearPrice()));
                } else {
                    player.sendMessage("§b " + C("HelpClear"));
                }
            } else if ("reset".equalsIgnoreCase(allowedcmd)) {
                player.sendMessage("§a /plotme reset");
                player.sendMessage("§b " + C("HelpReset"));
            } else if ("add".equalsIgnoreCase(allowedcmd)) {
                player.sendMessage("§a /plotme add <" + C("WordPlayer") + ">");
                if (ecoon && pmi != null && pmi.getAddPlayerPrice() != 0) {
                    player.sendMessage("§b " + C("HelpAdd") + " " + C("WordPrice") + " : §r" + Util.round(pmi.getAddPlayerPrice()));
                } else {
                    player.sendMessage("§b " + C("HelpAdd"));
                }
            } else if ("deny".equalsIgnoreCase(allowedcmd)) {
                player.sendMessage("§a /plotme deny <" + C("WordPlayer") + ">");
                if (ecoon && pmi != null && pmi.getDenyPlayerPrice() != 0) {
                    player.sendMessage("§b " + C("HelpDeny") + " " + C("WordPrice") + " : §r" + Util.round(pmi.getDenyPlayerPrice()));
                } else {
                    player.sendMessage("§b " + C("HelpDeny"));
                }
            } else if ("remove".equalsIgnoreCase(allowedcmd)) {
                player.sendMessage("§a /plotme remove <" + C("WordPlayer") + ">");
                if (ecoon && pmi != null && pmi.getRemovePlayerPrice() != 0) {
                    player.sendMessage("§b " + C("HelpRemove") + " " + C("WordPrice") + " : §r" + Util.round(pmi.getRemovePlayerPrice()));
                } else {
                    player.sendMessage("§b " + C("HelpRemove"));
                }
            } else if ("undeny".equalsIgnoreCase(allowedcmd)) {
                player.sendMessage("§a /plotme undeny <" + C("WordPlayer") + ">");
                if (ecoon && pmi != null && pmi.getUndenyPlayerPrice() != 0) {
                    player.sendMessage("§b " + C("HelpUndeny") + " " + C("WordPrice") + " : §r" + Util.round(pmi.getUndenyPlayerPrice()));
                } else {
                    player.sendMessage("§b " + C("HelpUndeny"));
                }
            } else if ("setowner".equalsIgnoreCase(allowedcmd)) {
                player.sendMessage("§a /plotme setowner <" + C("WordPlayer") + ">");
                player.sendMessage("§b " + C("HelpSetowner"));
            } else if ("move".equalsIgnoreCase(allowedcmd)) {
                player.sendMessage("§a /plotme move <" + C("WordIdFrom") + "> <" + C("WordIdTo") + ">");
                player.sendMessage("§b " + C("HelpMove"));
            } else if ("weanywhere".equalsIgnoreCase(allowedcmd)) {
                player.sendMessage("§a /plotme weanywhere");
                player.sendMessage("§b " + C("HelpWEAnywhere"));
            } else if ("expired".equalsIgnoreCase(allowedcmd)) {
                player.sendMessage("§a /plotme expired [page]");
                player.sendMessage("§b " + C("HelpExpired"));
            } else if ("donelist".equalsIgnoreCase(allowedcmd)) {
                player.sendMessage("§a /plotme donelist [page]");
                player.sendMessage("§b " + C("HelpDoneList"));
            } else if ("addtime".equalsIgnoreCase(allowedcmd)) {
                player.sendMessage("§a /plotme addtime");
                int days = (pmi == null) ? 0 : pmi.getDaysToExpiration();
                if (days == 0) {
                    player.sendMessage("§b " + C("HelpAddTime1") + " §r" + C("WordNever"));
                } else {
                    player.sendMessage("§b " + C("HelpAddTime1") + " §r" + days + "§b " + C("HelpAddTime2"));
                }
            } else if ("dispose".equalsIgnoreCase(allowedcmd)) {
                player.sendMessage("§a /plotme dispose");
                if (ecoon && pmi != null && pmi.getDisposePrice() != 0) {
                    player.sendMessage("§b " + C("HelpDispose") + " " + C("WordPrice") + " : §r" + Util.round(pmi.getDisposePrice()));
                } else {
                    player.sendMessage("§b " + C("HelpDispose"));
                }
            } else if ("buy".equalsIgnoreCase(allowedcmd)) {
                player.sendMessage("§a /plotme buy");
                player.sendMessage("§b " + C("HelpBuy"));
            } else if ("sell".equalsIgnoreCase(allowedcmd)) {
                player.sendMessage("§a /plotme sell [" + C("WordAmount") + "]");
                player.sendMessage("§b " + C("HelpSell") + " " + C("WordDefault") + " : §r" + Util.round(pmi.getSellToPlayerPrice()));
            } else if ("sellbank".equalsIgnoreCase(allowedcmd)) {
                player.sendMessage("§a /plotme sell bank");
                player.sendMessage("§b " + C("HelpSellBank") + " §r" + Util.round(pmi.getSellToBankPrice()));
            } else if ("auction".equalsIgnoreCase(allowedcmd)) {
                player.sendMessage("§a /plotme auction [" + C("WordAmount") + "]");
                player.sendMessage("§b " + C("HelpAuction") + " " + C("WordDefault") + " : §r1");
            } else if ("bid".equalsIgnoreCase(allowedcmd)) {
                player.sendMessage("§a /plotme bid <" + C("WordAmount") + ">");
                player.sendMessage("§b " + C("HelpBid"));
            }
        }
        return true;
    }
}
