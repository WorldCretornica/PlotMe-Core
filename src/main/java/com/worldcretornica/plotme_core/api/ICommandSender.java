package com.worldcretornica.plotme_core.api;

public interface ICommandSender {

    /**
     * Sends this sender a message
     *
     * @param message Message to be displayed
     */

    void sendMessage(String message);

    /**
     * Checks if the sender has the given permission.
     * @param permission The permission string
     * @return true if sender has permission
     */
    boolean hasPermission(String permission);
}
