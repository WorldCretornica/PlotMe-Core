package com.worldcretornica.plotme_core.commands;

import com.worldcretornica.plotme_core.PlotMe_Core;
import com.worldcretornica.plotme_core.api.CommandExBase;
import com.worldcretornica.plotme_core.api.ICommandSender;

public class CmdShowHelp extends PlotCommand {

    private final CommandExBase commandExBase;

    public CmdShowHelp(PlotMe_Core instance, CommandExBase commandExBase) {
        super(instance);
        this.commandExBase = commandExBase;
    }

    public String getName() {
        return "help";
    }

    public boolean execute(ICommandSender player, String[] args) {
        return true;
    }

    @Override
    public String getUsage() {
        return C("WordUsage") + ": /plotme help <" + C("WordPage") + ">";
    }
}
