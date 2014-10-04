package com.worldcretornica.plotme_core.bukkit.api;

import org.bukkit.command.CommandSender;

import com.worldcretornica.plotme_core.api.ICommandSender;

public class BukkitCommandSender implements ICommandSender {

    CommandSender commandsender;
    
    public BukkitCommandSender(CommandSender cs) {
        this.commandsender = cs;
    }

    @Override
    public void sendMessage(String c) {
        commandsender.sendMessage(c);
    }

    @Override
    public boolean hasPermission(String node) {
        return commandsender.hasPermission(node);
    }

    public CommandSender getCommandSender() {
        return commandsender;
    }

    @Override
    public String getName() {
        return commandsender.getName();
    }
}
