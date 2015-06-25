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
        player.sendMessage("Command is broken and will be fixed soon! Thank you for understanding!");
        player.sendMessage("Go to http://plotme.worldcretornica.com/cmdsandperms.html for a list of commands.");
        return true;
    }

    @Override
    public String getUsage() {
        return C("WordUsage") + ": /plotme help <" + C("WordPage") + ">";
    }
}
