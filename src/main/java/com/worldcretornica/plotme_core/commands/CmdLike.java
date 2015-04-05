package com.worldcretornica.plotme_core.commands;

import com.worldcretornica.plotme_core.PlotMe_Core;
import com.worldcretornica.plotme_core.api.ICommandSender;

public class CmdLike extends PlotCommand {

    public CmdLike(PlotMe_Core instance) {
        super(instance);
    }

    public String getName() {
        return "like";
    }

    public boolean execute(ICommandSender sender, String[] args) throws Exception{
        return true;
    }

    @Override
    public String getUsage() {
        return C("WordUsage") + ": /plotme like";
    }

}