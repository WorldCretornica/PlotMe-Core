package com.worldcretornica.plotme_core.commands;

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
        boolean ecoon = plugin.getPlotMeCoreManager().isEconomyEnabled(player);

        List<String> allowed_commands = new ArrayList<>();

        allowed_commands.add("limit");
        if (PlotMe_Core.cPerms(player, "PlotMe.use.claim")) {
            allowed_commands.add("claim");
        }
        if (PlotMe_Core.cPerms(player, "PlotMe.use.claim.other")) {
            allowed_commands.add("claim.other");
        }
        if (PlotMe_Core.cPerms(player, "PlotMe.use.auto")) {
            allowed_commands.add("auto");
        }
        if (PlotMe_Core.cPerms(player, "PlotMe.use.home")) {
            allowed_commands.add("home");
        }
        if (PlotMe_Core.cPerms(player, "PlotMe.use.home.other")) {
            allowed_commands.add("home.other");
        }
        if (PlotMe_Core.cPerms(player, "PlotMe.use.info")) {
            allowed_commands.add("info");
            allowed_commands.add("biomeinfo");
        }
        if (PlotMe_Core.cPerms(player, "PlotMe.use.comment")) {
            allowed_commands.add("comment");
        }
        if (PlotMe_Core.cPerms(player, "PlotMe.use.comments")) {
            allowed_commands.add("comments");
        }
        if (PlotMe_Core.cPerms(player, "PlotMe.use.list")) {
            allowed_commands.add("list");
        }
        if (PlotMe_Core.cPerms(player, "PlotMe.use.biome")) {
            allowed_commands.add("biome");
            allowed_commands.add("biomelist");
        }
        if (PlotMe_Core.cPerms(player, "PlotMe.use.done") || PlotMe_Core.cPerms(player, "PlotMe.admin.done")) {
            allowed_commands.add("done");
        }
        if (PlotMe_Core.cPerms(player, "PlotMe.admin.done")) {
            allowed_commands.add("donelist");
        }
        if (PlotMe_Core.cPerms(player, "PlotMe.admin.tp")) {
            allowed_commands.add("tp");
        }
        if (PlotMe_Core.cPerms(player, "PlotMe.use.clear") || PlotMe_Core.cPerms(player, "PlotMe.admin.clear")) {
            allowed_commands.add("clear");
        }
        if (PlotMe_Core.cPerms(player, "PlotMe.admin.dispose") || PlotMe_Core.cPerms(player, "PlotMe.use.dispose")) {
            allowed_commands.add("dispose");
        }
        if (PlotMe_Core.cPerms(player, "PlotMe.admin.reset")) {
            allowed_commands.add("reset");
        }
        if (PlotMe_Core.cPerms(player, "PlotMe.use.add") || PlotMe_Core.cPerms(player, "PlotMe.admin.add")) {
            allowed_commands.add("add");
        }
        if (PlotMe_Core.cPerms(player, "PlotMe.use.remove") || PlotMe_Core.cPerms(player, "PlotMe.admin.remove")) {
            allowed_commands.add("remove");
        }
        if (sob.getConfig().getBoolean("allowToDeny")) {
            if (PlotMe_Core.cPerms(player, "PlotMe.use.deny") || PlotMe_Core.cPerms(player, "PlotMe.admin.deny")) {
                allowed_commands.add("deny");
            }
            if (PlotMe_Core.cPerms(player, "PlotMe.use.undeny") || PlotMe_Core.cPerms(player, "PlotMe.admin.undeny")) {
                allowed_commands.add("undeny");
            }
        }
        if (PlotMe_Core.cPerms(player, "PlotMe.admin.setowner")) {
            allowed_commands.add("setowner");
        }
        if (PlotMe_Core.cPerms(player, "PlotMe.admin.move")) {
            allowed_commands.add("move");
        }
        if (PlotMe_Core.cPerms(player, "PlotMe.admin.weanywhere")) {
            allowed_commands.add("weanywhere");
        }
        if (PlotMe_Core.cPerms(player, "PlotMe.admin.reload")) {
            allowed_commands.add("reload");
        }
        if (PlotMe_Core.cPerms(player, "PlotMe.admin.list")) {
            allowed_commands.add("listother");
        }
        if (PlotMe_Core.cPerms(player, "PlotMe.admin.expired")) {
            allowed_commands.add("expired");
        }
        if (PlotMe_Core.cPerms(player, "PlotMe.admin.addtime")) {
            allowed_commands.add("addtime");
        }

        PlotMapInfo pmi = plugin.getPlotMeCoreManager().getMap(player);

        if (plugin.getPlotMeCoreManager().isPlotWorld(player) && ecoon) {
            if (PlotMe_Core.cPerms(player, "PlotMe.use.buy")) {
                allowed_commands.add("buy");
            }
            if (PlotMe_Core.cPerms(player, "PlotMe.use.sell")) {
                allowed_commands.add("sell");
                if (pmi.isCanSellToBank()) {
                    allowed_commands.add("sellbank");
                }
            }
            if (PlotMe_Core.cPerms(player, "PlotMe.use.auction")) {
                allowed_commands.add("auction");
            }
            if (PlotMe_Core.cPerms(player, "PlotMe.use.bid")) {
                allowed_commands.add("bid");
            }
        }

        int maxpage = (int) Math.ceil((double) allowed_commands.size() / 4);

        if (page > maxpage) {
            page = 1;
        }

        player.sendMessage(RED + " ---==" + AQUA + C("HelpTitle") + " " + page + "/" + maxpage + RED + "==--- ");

        for (int ctr = (page - 1) * 4; ctr < (page * 4) && ctr < allowed_commands.size(); ctr++) {
            String allowedcmd = allowed_commands.get(ctr);

            if (allowedcmd.equalsIgnoreCase("limit")) {
                if (plugin.getPlotMeCoreManager().isPlotWorld(player) || sob.getConfig().getBoolean("allowWorldTeleport")) {
                    IWorld w = null;

                    if (plugin.getPlotMeCoreManager().isPlotWorld(player)) {
                        w = player.getWorld();
                    } else if (sob.getConfig().getBoolean("allowWorldTeleport")) {
                        w = plugin.getPlotMeCoreManager().getFirstWorld();
                    }

                    int maxplots = plugin.getPlotLimit(player);
                    int ownedplots = plugin.getPlotMeCoreManager().getNbOwnedPlot(player, w);

                    if (maxplots == -1) {
                        player.sendMessage(GREEN + C("HelpYourPlotLimitWorld") + " : " + AQUA + ownedplots + GREEN + " " + C("HelpUsedOf") + " " + AQUA + C("WordInfinite"));
                    } else {
                        player.sendMessage(GREEN + C("HelpYourPlotLimitWorld") + " : " + AQUA + ownedplots + GREEN + " " + C("HelpUsedOf") + " " + AQUA + maxplots);
                    }
                } else {
                    player.sendMessage(GREEN + C("HelpYourPlotLimitWorld") + " : " + AQUA + C("MsgNotPlotWorld"));
                }
            } else if (allowedcmd.equalsIgnoreCase("claim")) {
                player.sendMessage(GREEN + " /plotme claim");
                if (ecoon && pmi != null && pmi.getClaimPrice() != 0) {
                    player.sendMessage(AQUA + " " + C("HelpClaim") + " " + C("WordPrice") + " : " + RESET + Util().round(pmi.getClaimPrice()));
                } else {
                    player.sendMessage(AQUA + " " + C("HelpClaim"));
                }
            } else if (allowedcmd.equalsIgnoreCase("claim.other")) {
                player.sendMessage(GREEN + " /plotme claim" + " <" + C("WordPlayer") + ">");
                if (ecoon && pmi != null && pmi.getClaimPrice() != 0) {
                    player.sendMessage(AQUA + " " + C("HelpClaimOther") + " " + C("WordPrice") + " : " + RESET + Util().round(pmi.getClaimPrice()));
                } else {
                    player.sendMessage(AQUA + " " + C("HelpClaimOther"));
                }
            } else if (allowedcmd.equalsIgnoreCase("auto")) {
                if (sob.getConfig().getBoolean("allowWorldTeleport")) {
                    player.sendMessage(GREEN + " /plotme " + C("CommandAuto") + " [" + C("WordWorld") + "]");
                } else {
                    player.sendMessage(GREEN + " /plotme " + C("CommandAuto"));
                }

                if (ecoon && pmi != null && pmi.getClaimPrice() != 0) {
                    player.sendMessage(AQUA + " " + C("HelpAuto") + " " + C("WordPrice") + " : " + RESET + Util().round(pmi.getClaimPrice()));
                } else {
                    player.sendMessage(AQUA + " " + C("HelpAuto"));
                }
            } else if (allowedcmd.equalsIgnoreCase("home")) {
                if (sob.getConfig().getBoolean("allowWorldTeleport")) {
                    player.sendMessage(GREEN + " /plotme " + C("CommandHome") + "[:#] [" + C("WordWorld") + "]");
                } else {
                    player.sendMessage(GREEN + " /plotme " + C("CommandHome") + "[:#]");
                }

                if (ecoon && pmi != null && pmi.getPlotHomePrice() != 0) {
                    player.sendMessage(AQUA + " " + C("HelpHome") + " " + C("WordPrice") + " : " + RESET + Util().round(pmi.getPlotHomePrice()));
                } else {
                    player.sendMessage(AQUA + " " + C("HelpHome"));
                }
            } else if (allowedcmd.equalsIgnoreCase("home.other")) {
                if (sob.getConfig().getBoolean("allowWorldTeleport")) {
                    player.sendMessage(GREEN + " /plotme " + C("CommandHome") + "[:#] <" + C("WordPlayer") + "> [" + C("WordWorld") + "]");
                } else {
                    player.sendMessage(GREEN + " /plotme " + C("CommandHome") + "[:#] <" + C("WordPlayer") + ">");
                }

                if (ecoon && pmi != null && pmi.getPlotHomePrice() != 0) {
                    player.sendMessage(AQUA + " " + C("HelpHomeOther") + " " + C("WordPrice") + " : " + RESET + Util().round(pmi.getPlotHomePrice()));
                } else {
                    player.sendMessage(AQUA + " " + C("HelpHomeOther"));
                }
            } else if (allowedcmd.equalsIgnoreCase("info")) {
                player.sendMessage(GREEN + " /plotme " + C("CommandInfo"));
                player.sendMessage(AQUA + " " + C("HelpInfo"));
            } else if (allowedcmd.equalsIgnoreCase("comment")) {
                player.sendMessage(GREEN + " /plotme " + C("CommandComment") + " <" + C("WordComment") + ">");
                if (ecoon && pmi != null && pmi.getAddCommentPrice() != 0) {
                    player.sendMessage(AQUA + " " + C("HelpComment") + " " + C("WordPrice") + " : " + RESET + Util().round(pmi.getAddCommentPrice()));
                } else {
                    player.sendMessage(AQUA + " " + C("HelpComment"));
                }
            } else if (allowedcmd.equalsIgnoreCase("comments")) {
                player.sendMessage(GREEN + " /plotme " + C("CommandComments"));
                player.sendMessage(AQUA + " " + C("HelpComments"));
            } else if (allowedcmd.equalsIgnoreCase("list")) {
                player.sendMessage(GREEN + " /plotme " + C("CommandList"));
                player.sendMessage(AQUA + " " + C("HelpList"));
            } else if (allowedcmd.equalsIgnoreCase("listother")) {
                player.sendMessage(GREEN + " /plotme " + C("CommandList") + " <" + C("WordPlayer") + ">");
                player.sendMessage(AQUA + " " + C("HelpListOther"));
            } else if (allowedcmd.equalsIgnoreCase("biomeinfo")) {
                player.sendMessage(GREEN + " /plotme " + C("CommandBiome"));
                player.sendMessage(AQUA + " " + C("HelpBiomeInfo"));
            } else if (allowedcmd.equalsIgnoreCase("biome")) {
                player.sendMessage(GREEN + " /plotme " + C("CommandBiome") + " <" + C("WordBiome") + ">");
                if (ecoon && pmi != null && pmi.getBiomeChangePrice() != 0) {
                    player.sendMessage(AQUA + " " + C("HelpBiome") + " " + C("WordPrice") + " : " + RESET + Util().round(pmi.getBiomeChangePrice()));
                } else {
                    player.sendMessage(AQUA + " " + C("HelpBiome"));
                }
            } else if (allowedcmd.equalsIgnoreCase("biomelist")) {
                player.sendMessage(GREEN + " /plotme " + C("CommandBiomelist"));
                player.sendMessage(AQUA + " " + C("HelpBiomeList"));
            } else if (allowedcmd.equalsIgnoreCase("done")) {
                player.sendMessage(GREEN + " /plotme " + C("CommandDone"));
                player.sendMessage(AQUA + " " + C("HelpDone"));
            } else if (allowedcmd.equalsIgnoreCase("tp")) {
                if (sob.getConfig().getBoolean("allowWorldTeleport")) {
                    player.sendMessage(GREEN + " /plotme " + C("CommandTp") + " <ID> [" + C("WordWorld") + "]");
                } else {
                    player.sendMessage(GREEN + " /plotme " + C("CommandTp") + " <ID>");
                }

                player.sendMessage(AQUA + " " + C("HelpTp"));
            } else if (allowedcmd.equalsIgnoreCase("clear")) {
                player.sendMessage(GREEN + " /plotme " + C("CommandClear"));
                if (ecoon && pmi != null && pmi.getClearPrice() != 0) {
                    player.sendMessage(AQUA + " " + C("HelpId") + " " + C("WordPrice") + " : " + RESET + Util().round(pmi.getClearPrice()));
                } else {
                    player.sendMessage(AQUA + " " + C("HelpClear"));
                }
            } else if (allowedcmd.equalsIgnoreCase("reset")) {
                player.sendMessage(GREEN + " /plotme " + C("CommandReset"));
                player.sendMessage(AQUA + " " + C("HelpReset"));
            } else if (allowedcmd.equalsIgnoreCase("add")) {
                player.sendMessage(GREEN + " /plotme " + C("CommandAdd") + " <" + C("WordPlayer") + ">");
                if (ecoon && pmi != null && pmi.getAddPlayerPrice() != 0) {
                    player.sendMessage(AQUA + " " + C("HelpAdd") + " " + C("WordPrice") + " : " + RESET + Util().round(pmi.getAddPlayerPrice()));
                } else {
                    player.sendMessage(AQUA + " " + C("HelpAdd"));
                }
            } else if (allowedcmd.equalsIgnoreCase("deny")) {
                player.sendMessage(GREEN + " /plotme " + C("CommandDeny") + " <" + C("WordPlayer") + ">");
                if (ecoon && pmi != null && pmi.getDenyPlayerPrice() != 0) {
                    player.sendMessage(AQUA + " " + C("HelpDeny") + " " + C("WordPrice") + " : " + RESET + Util().round(pmi.getDenyPlayerPrice()));
                } else {
                    player.sendMessage(AQUA + " " + C("HelpDeny"));
                }
            } else if (allowedcmd.equalsIgnoreCase("remove")) {
                player.sendMessage(GREEN + " /plotme " + C("CommandRemove") + " <" + C("WordPlayer") + ">");
                if (ecoon && pmi != null && pmi.getRemovePlayerPrice() != 0) {
                    player.sendMessage(AQUA + " " + C("HelpRemove") + " " + C("WordPrice") + " : " + RESET + Util().round(pmi.getRemovePlayerPrice()));
                } else {
                    player.sendMessage(AQUA + " " + C("HelpRemove"));
                }
            } else if (allowedcmd.equalsIgnoreCase("undeny")) {
                player.sendMessage(GREEN + " /plotme " + C("CommandUndeny") + " <" + C("WordPlayer") + ">");
                if (ecoon && pmi != null && pmi.getUndenyPlayerPrice() != 0) {
                    player.sendMessage(AQUA + " " + C("HelpUndeny") + " " + C("WordPrice") + " : " + RESET + Util().round(pmi.getUndenyPlayerPrice()));
                } else {
                    player.sendMessage(AQUA + " " + C("HelpUndeny"));
                }
            } else if (allowedcmd.equalsIgnoreCase("setowner")) {
                player.sendMessage(GREEN + " /plotme " + C("CommandSetowner") + " <" + C("WordPlayer") + ">");
                player.sendMessage(AQUA + " " + C("HelpSetowner"));
            } else if (allowedcmd.equalsIgnoreCase("move")) {
                player.sendMessage(GREEN + " /plotme " + C("CommandMove") + " <" + C("WordIdFrom") + "> <" + C("WordIdTo") + ">");
                player.sendMessage(AQUA + " " + C("HelpMove"));
            } else if (allowedcmd.equalsIgnoreCase("weanywhere")) {
                player.sendMessage(GREEN + " /plotme " + C("CommandWEAnywhere"));
                player.sendMessage(AQUA + " " + C("HelpWEAnywhere"));
            } else if (allowedcmd.equalsIgnoreCase("expired")) {
                player.sendMessage(GREEN + " /plotme " + C("CommandExpired") + " [page]");
                player.sendMessage(AQUA + " " + C("HelpExpired"));
            } else if (allowedcmd.equalsIgnoreCase("donelist")) {
                player.sendMessage(GREEN + " /plotme " + C("CommandDoneList") + " [page]");
                player.sendMessage(AQUA + " " + C("HelpDoneList"));
            } else if (allowedcmd.equalsIgnoreCase("addtime")) {
                player.sendMessage(GREEN + " /plotme " + C("CommandAddtime"));
                int days = (pmi == null) ? 0 : pmi.getDaysToExpiration();
                if (days == 0) {
                    player.sendMessage(AQUA + " " + C("HelpAddTime1") + " " + RESET + C("WordNever"));
                } else {
                    player.sendMessage(AQUA + " " + C("HelpAddTime1") + " " + RESET + days + AQUA + " " + C("HelpAddTime2"));
                }
            } else if (allowedcmd.equalsIgnoreCase("reload")) {
                player.sendMessage(GREEN + " /plotme reload");
                player.sendMessage(AQUA + " " + C("HelpReload"));
            } else if (allowedcmd.equalsIgnoreCase("dispose")) {
                player.sendMessage(GREEN + " /plotme " + C("CommandDispose"));
                if (ecoon && pmi != null && pmi.getDisposePrice() != 0) {
                    player.sendMessage(AQUA + " " + C("HelpDispose") + " " + C("WordPrice") + " : " + RESET + Util().round(pmi.getDisposePrice()));
                } else {
                    player.sendMessage(AQUA + " " + C("HelpDispose"));
                }
            } else if (allowedcmd.equalsIgnoreCase("buy")) {
                player.sendMessage(GREEN + " /plotme " + C("CommandBuy"));
                player.sendMessage(AQUA + " " + C("HelpBuy"));
            } else if (allowedcmd.equalsIgnoreCase("sell")) {
                player.sendMessage(GREEN + " /plotme " + C("CommandSell") + " [" + C("WordAmount") + "]");
                player.sendMessage(AQUA + " " + C("HelpSell") + " " + C("WordDefault") + " : " + RESET + Util().round(pmi.getSellToPlayerPrice()));
            } else if (allowedcmd.equalsIgnoreCase("sellbank")) {
                player.sendMessage(GREEN + " /plotme " + C("CommandSellBank"));
                player.sendMessage(AQUA + " " + C("HelpSellBank") + " " + RESET + Util().round(pmi.getSellToBankPrice()));
            } else if (allowedcmd.equalsIgnoreCase("auction")) {
                player.sendMessage(GREEN + " /plotme " + C("CommandAuction") + " [" + C("WordAmount") + "]");
                player.sendMessage(AQUA + " " + C("HelpAuction") + " " + C("WordDefault") + " : " + RESET + "1");
            } else if (allowedcmd.equalsIgnoreCase("bid")) {
                player.sendMessage(GREEN + " /plotme " + C("CommandBid") + " <" + C("WordAmount") + ">");
                player.sendMessage(AQUA + " " + C("HelpBid"));
            }
        }
        return true;
    }
}
