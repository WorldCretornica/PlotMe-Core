package com.worldcretornica.plotme_core.bukkit.api;

import com.worldcretornica.plotme_core.api.CommandSender;

public class BukkitCommandSender implements CommandSender {

    private final org.bukkit.command.CommandSender commandsender;

    public BukkitCommandSender(org.bukkit.command.CommandSender sender) {
        commandsender = sender;
    }

    @Override
    public void sendMessage(String message) {
        commandsender.sendMessage(message);
    }

    public org.bukkit.command.CommandSender getCommandSender() {
        return commandsender;
    }

    public String getName() {
        return commandsender.getName();
    }

    @Override
    public boolean hasPermission(String node) {
        return true;
    }
}
