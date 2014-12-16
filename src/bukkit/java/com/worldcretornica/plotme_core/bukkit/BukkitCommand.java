package com.worldcretornica.plotme_core.bukkit;

import com.worldcretornica.plotme_core.PlotMe_Core;
import com.worldcretornica.plotme_core.api.IPlayer;
import com.worldcretornica.plotme_core.bukkit.api.BukkitCommandSender;
import com.worldcretornica.plotme_core.bukkit.api.BukkitPlayer;
import com.worldcretornica.plotme_core.commands.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class BukkitCommand implements CommandExecutor {

    private final PlotMe_Core api;
    private final CmdAdd add;
    private final CmdAddTime addtime;
    private final CmdAuction auction;
    private final CmdAuto auto;
    private final CmdBid bid;
    private final CmdBiome biome;
    private final CmdBiomes biomes;
    private final CmdBuy buy;
    private final CmdClaim claim;
    private final CmdClear clear;
    private final CmdDeny deny;
    private final CmdDispose dispose;
    private final CmdDone done;
    private final CmdDoneList donelist;
    private final CmdExpired expired;
    private final CmdHome home;
    private final CmdInfo info;
    private final CmdMove move;
    private final CmdPlotList plotlist;
    private final CmdProtect protect;
    private final CmdReload reload;
    private final CmdRemove remove;
    private final CmdReset reset;
    private final CmdResetExpired resetexpired;
    private final CmdSell sell;
    private final CmdSetOwner setowner;
    private final CmdShowHelp showhelp;
    private final CmdTP tp;
    private final CmdUndeny undeny;
    private final CmdWEAnywhere weanywhere;

    public BukkitCommand(PlotMe_CorePlugin instance) {
        api = instance.getAPI();
        add = new CmdAdd(api);
        addtime = new CmdAddTime(api);
        auction = new CmdAuction(api);
        auto = new CmdAuto(api);
        bid = new CmdBid(api);
        biome = new CmdBiome(api);
        biomes = new CmdBiomes(api);
        buy = new CmdBuy(api);
        claim = new CmdClaim(api);
        clear = new CmdClear(api);
        deny = new CmdDeny(api);
        dispose = new CmdDispose(api);
        done = new CmdDone(api);
        donelist = new CmdDoneList(api);
        expired = new CmdExpired(api);
        home = new CmdHome(api);
        info = new CmdInfo(api);
        move = new CmdMove(api);
        plotlist = new CmdPlotList(api);
        protect = new CmdProtect(api);
        reload = new CmdReload(api);
        remove = new CmdRemove(api);
        reset = new CmdReset(api);
        resetexpired = new CmdResetExpired(api);
        sell = new CmdSell(api);
        setowner = new CmdSetOwner(api);
        showhelp = new CmdShowHelp(api);
        tp = new CmdTP(api);
        undeny = new CmdUndeny(api);
        weanywhere = new CmdWEAnywhere(api);
    }

    private String C(String caption) {
        return api.getUtil().C(caption);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            if (args.length == 0) {
                sender.sendMessage(C("ConsoleHelpMain"));
                sender.sendMessage("- /plotme reload");
                sender.sendMessage(C("ConsoleHelpReload"));
                return true;
            } else {
                if ("reload".equalsIgnoreCase(args[0])) {
                    return reload.exec();
                }
                if ("resetexpired".equalsIgnoreCase(args[0])) {
                    return resetexpired.exec(new BukkitCommandSender(sender), args);
                }
            }
        } else {
            IPlayer player = new BukkitPlayer((Player) sender);

            if (args.length == 0) {
                return showhelp.exec(player, 1);
            } else {
                int ipage = -1;

                try {
                    ipage = Integer.parseInt(args[0]);
                } catch (NumberFormatException ignored) {
                }

                if (ipage == -1) {
                    if ("help".equalsIgnoreCase(args[0])) {
                        if (args.length > 1) {
                            String a1 = args[1];
                            ipage = -1;

                            try {
                                ipage = Integer.parseInt(a1);
                            } catch (NumberFormatException ignored) {
                            }
                        }

                        if (ipage == -1) {
                            return showhelp.exec(player, 1);
                        } else {
                            return showhelp.exec(player, ipage);
                        }
                    }
                    if ("claim".equalsIgnoreCase(args[0])) {
                        return claim.exec(player, args);
                    }
                    if ("auto".equalsIgnoreCase(args[0])) {
                        return auto.exec(player, args);
                    }
                    if ("info".equalsIgnoreCase(args[0]) || "i".equalsIgnoreCase(args[0])) {
                        return info.exec(player);
                    }
                    if ("biome".equalsIgnoreCase(args[0])) {
                        return biome.exec(player, args);
                    }
                    if ("biomes".equalsIgnoreCase(args[0])) {
                        return biomes.exec(player, args);
                    }
                    if ("tp".equalsIgnoreCase(args[0])) {
                        return tp.exec(player, args);
                    }
                    if ("clear".equalsIgnoreCase(args[0])) {
                        return clear.exec(player);
                    }
                    if ("reset".equalsIgnoreCase(args[0])) {
                        return reset.exec(player);
                    }
                    if ("add".equalsIgnoreCase(args[0]) || "+".equalsIgnoreCase(args[0])) {
                        return add.exec(player, args);
                    }
                    if ("deny".equalsIgnoreCase(args[0])) {
                        return deny.exec(player, args);
                    }
                    if ("undeny".equalsIgnoreCase(args[0])) {
                        return undeny.exec(player, args);
                    }
                    if ("remove".equalsIgnoreCase(args[0]) || "-".equalsIgnoreCase(args[0])) {
                        return remove.exec(player, args);
                    }
                    if ("setowner".equalsIgnoreCase(args[0])) {
                        return setowner.exec(player, args);
                    }
                    if ("move".equalsIgnoreCase(args[0])) {
                        return move.exec(player, args);
                    }
                    if ("weanywhere".equalsIgnoreCase(args[0])) {
                        return weanywhere.exec(player);
                    }
                    if ("list".equalsIgnoreCase(args[0])) {
                        return plotlist.exec(player, args);
                    }
                    if ("expired".equalsIgnoreCase(args[0])) {
                        return expired.exec(player, args);
                    }
                    if ("addtime".equalsIgnoreCase(args[0])) {
                        return addtime.exec(player);
                    }
                    if ("done".equalsIgnoreCase(args[0])) {
                        return done.exec(player);
                    }
                    if ("donelist".equalsIgnoreCase(args[0])) {
                        return donelist.exec(player, args);
                    }
                    if ("protect".equalsIgnoreCase(args[0])) {
                        return protect.exec(player);
                    }
                    if ("sell".equalsIgnoreCase(args[0])) {
                        return sell.exec(player, args);
                    }
                    if ("dispose".equalsIgnoreCase(args[0])) {
                        return dispose.exec(player);
                    }
                    if ("auction".equalsIgnoreCase(args[0])) {
                        return auction.exec(player, args);
                    }
                    if ("buy".equalsIgnoreCase(args[0])) {
                        return buy.exec(player);
                    }
                    if ("bid".equalsIgnoreCase(args[0])) {
                        return bid.exec(player, args);
                    }
                    if (args[0].startsWith("home") || args[0].startsWith("h")) {
                        return home.exec(player, args);
                    }
                } else {
                    return showhelp.exec(player, ipage);
                }
            }
        }
        return false;
    }
}
