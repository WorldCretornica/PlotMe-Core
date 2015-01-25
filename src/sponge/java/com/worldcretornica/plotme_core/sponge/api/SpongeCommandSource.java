package com.worldcretornica.plotme_core.sponge.api;

import com.worldcretornica.plotme_core.api.ICommandSender;
import org.spongepowered.api.util.command.CommandSource;

public class SpongeCommandSource implements ICommandSender {

    private final CommandSource commandSource;

    public SpongeCommandSource(CommandSource commandSource) {
        this.commandSource = commandSource;
    }

    @Override
    public void sendMessage(String message) {
        commandSource.sendMessage(message);
    }

    @Override
    public boolean hasPermission(String node) {
        return commandSource.hasPermission(node);
    }

}
