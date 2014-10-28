package com.worldcretornica.plotme_core.bukkit.api;

import com.worldcretornica.plotme_core.api.ICommandSender;
import org.bukkit.command.CommandSender;

public class BukkitCommandSender implements ICommandSender {

    private CommandSender commandsender;
    
    public BukkitCommandSender(CommandSender cs) {
        this.commandsender = cs;
    }

    @Override
    public void sendMessage(String msg) {
        commandsender.sendMessage(msg);
    }

    @Override
    public boolean hasPermission(String node) {
        return true;
    }

    public CommandSender getCommandSender() {
        return commandsender;
    }

    @Override
    public String getName() {
        return commandsender.getName();
    }
}
