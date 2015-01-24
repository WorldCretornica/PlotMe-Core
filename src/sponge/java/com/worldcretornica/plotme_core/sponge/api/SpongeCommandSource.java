package com.worldcretornica.plotme_core.sponge.api;

import org.spongepowered.api.util.command.CommandSource;

import com.worldcretornica.plotme_core.api.ICommandSender;

public class SpongeCommandSource implements ICommandSender {
    
    private final CommandSource commandsource;
    
    public SpongeCommandSource(CommandSource commandsource) {
        this.commandsource = commandsource;
    }

    @Override
    public void sendMessage(String message) {
        commandsource.sendMessage(message);
    }

    @Override
    public boolean hasPermission(String node) {
        return false; //TODO
    }

}
