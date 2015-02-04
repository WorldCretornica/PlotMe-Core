package com.worldcretornica.plotme_core.bukkit.api;

import com.worldcretornica.plotme_core.api.ICommandSender;
import org.bukkit.command.*;

public class BukkitCommandSender implements ICommandSender {

    private final CommandSender commandsender;

    public BukkitCommandSender(CommandSender sender) {
        commandsender = sender;
    }

    @Override
    public void sendMessage(String message) {
        commandsender.sendMessage(message);
    }

    public CommandSender getCommandSender() {
        return commandsender;
    }

    public String getName() {
        return commandsender.getName();
    }

    @Override
    public boolean hasPermission(String permission) {
        return true;
    }
}
