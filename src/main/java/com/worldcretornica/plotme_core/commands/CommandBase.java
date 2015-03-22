package com.worldcretornica.plotme_core.commands;

import com.worldcretornica.plotme_core.api.ICommandSender;

public interface CommandBase {


    String getName();

    boolean execute(ICommandSender sender, String[] args);

    String getUsage();
}
