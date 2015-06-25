package com.worldcretornica.plotme_core.api;

import com.worldcretornica.plotme_core.PlotMe_Core;
import com.worldcretornica.plotme_core.commands.CmdAdd;
import com.worldcretornica.plotme_core.commands.CmdAddTime;
import com.worldcretornica.plotme_core.commands.CmdAuto;
import com.worldcretornica.plotme_core.commands.CmdBiome;
import com.worldcretornica.plotme_core.commands.CmdBiomes;
import com.worldcretornica.plotme_core.commands.CmdBuy;
import com.worldcretornica.plotme_core.commands.CmdClaim;
import com.worldcretornica.plotme_core.commands.CmdClear;
import com.worldcretornica.plotme_core.commands.CmdDeny;
import com.worldcretornica.plotme_core.commands.CmdDispose;
import com.worldcretornica.plotme_core.commands.CmdDone;
import com.worldcretornica.plotme_core.commands.CmdDoneList;
import com.worldcretornica.plotme_core.commands.CmdExpired;
import com.worldcretornica.plotme_core.commands.CmdHome;
import com.worldcretornica.plotme_core.commands.CmdInfo;
import com.worldcretornica.plotme_core.commands.CmdLike;
import com.worldcretornica.plotme_core.commands.CmdMiddle;
import com.worldcretornica.plotme_core.commands.CmdMove;
import com.worldcretornica.plotme_core.commands.CmdPlotList;
import com.worldcretornica.plotme_core.commands.CmdProtect;
import com.worldcretornica.plotme_core.commands.CmdReload;
import com.worldcretornica.plotme_core.commands.CmdRemove;
import com.worldcretornica.plotme_core.commands.CmdReset;
import com.worldcretornica.plotme_core.commands.CmdResetExpired;
import com.worldcretornica.plotme_core.commands.CmdSell;
import com.worldcretornica.plotme_core.commands.CmdSetOwner;
import com.worldcretornica.plotme_core.commands.CmdShowHelp;
import com.worldcretornica.plotme_core.commands.CmdTP;
import com.worldcretornica.plotme_core.commands.CmdTrust;
import com.worldcretornica.plotme_core.commands.CmdUndeny;
import com.worldcretornica.plotme_core.commands.CmdWEAnywhere;
import com.worldcretornica.plotme_core.commands.PlotCommand;

import java.util.HashMap;

public class CommandExBase {

    public final HashMap<String, PlotCommand> commandMap = new HashMap<>();
    protected final PlotMe_Core api;

    protected CommandExBase(PlotMe_Core api) {
        this.api = api;
        registerCommand(new CmdAdd(api, this));
        registerCommand(new CmdAddTime(api, this));
        registerCommand(new CmdAuto(api, this));
        registerCommand(new CmdBiome(api, this));
        registerCommand(new CmdBiomes(api, this));
        registerCommand(new CmdBuy(api, this));
        registerCommand(new CmdClaim(api, this));
        registerCommand(new CmdClear(api, this));
        registerCommand(new CmdDeny(api, this));
        registerCommand(new CmdDispose(api, this));
        registerCommand(new CmdDone(api, this));
        registerCommand(new CmdDoneList(api, this));
        registerCommand(new CmdExpired(api, this));
        registerCommand(new CmdHome(api, this));
        registerCommand(new CmdInfo(api, this));
        registerCommand(new CmdMove(api, this));
        registerCommand(new CmdPlotList(api, this));
        registerCommand(new CmdProtect(api, this));
        registerCommand(new CmdReload(api, this));
        registerCommand(new CmdRemove(api));
        registerCommand(new CmdReset(api, this));
        registerCommand(new CmdResetExpired(api, this));
        registerCommand(new CmdSell(api, this));
        registerCommand(new CmdTrust(api, this));
        registerCommand(new CmdLike(api, this));
        registerCommand(new CmdSetOwner(api, this));
        registerCommand(new CmdShowHelp(api, this));
        registerCommand(new CmdTP(api, this));
        registerCommand(new CmdUndeny(api, this));
        registerCommand(new CmdMiddle(api, this));
        registerCommand(new CmdWEAnywhere(api, this));
    }

    private void registerCommand(PlotCommand cmd) {
        for (String aliases : cmd.getAliases()) {
            commandMap.put(aliases, cmd);
        }

        this.commandMap.put(cmd.getName(), cmd);
    }

}
