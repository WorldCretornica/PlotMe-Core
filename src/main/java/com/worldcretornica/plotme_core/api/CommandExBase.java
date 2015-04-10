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
import com.worldcretornica.plotme_core.commands.CmdUndeny;
import com.worldcretornica.plotme_core.commands.CmdWEAnywhere;
import com.worldcretornica.plotme_core.commands.PlotCommand;

import java.util.HashMap;

public class CommandExBase {

    protected final HashMap<String, PlotCommand> commandMap = new HashMap<>();
    protected final PlotMe_Core api;

    protected CommandExBase(PlotMe_Core api) {
        this.api = api;
        registerCommand(new CmdAdd(api));
        registerCommand(new CmdAddTime(api));
        registerCommand(new CmdAuto(api));
        registerCommand(new CmdBiome(api));
        registerCommand(new CmdBiomes(api));
        registerCommand(new CmdBuy(api));
        registerCommand(new CmdClaim(api));
        registerCommand(new CmdClear(api));
        registerCommand(new CmdDeny(api));
        registerCommand(new CmdDispose(api));
        registerCommand(new CmdDone(api));
        registerCommand(new CmdDoneList(api));
        registerCommand(new CmdExpired(api));
        registerCommand(new CmdHome(api));
        registerCommand(new CmdInfo(api));
        registerCommand(new CmdMove(api));
        registerCommand(new CmdPlotList(api));
        registerCommand(new CmdProtect(api));
        registerCommand(new CmdReload(api));
        registerCommand(new CmdRemove(api));
        registerCommand(new CmdReset(api));
        registerCommand(new CmdResetExpired(api));
        registerCommand(new CmdSell(api));
        registerCommand(new CmdSetOwner(api));
        registerCommand(new CmdShowHelp(api));
        registerCommand(new CmdTP(api));
        registerCommand(new CmdUndeny(api));
        registerCommand(new CmdMiddle(api));
        registerCommand(new CmdWEAnywhere(api));
    }

    private void registerCommand(PlotCommand cmd) {
        if (cmd.getName().equalsIgnoreCase("home")) {
            commandMap.put("h", cmd);
        }
        this.commandMap.put(cmd.getName(), cmd);
    }

}
