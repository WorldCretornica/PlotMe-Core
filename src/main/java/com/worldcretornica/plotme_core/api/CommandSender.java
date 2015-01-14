package com.worldcretornica.plotme_core.api;

public interface CommandSender {

    /**
     * Sends this sender a message
     *
     * @param message Message to be displayed
     */

    void sendMessage(String message);

    /**
     * Checks if the sender has the given permission node.
     * @param node permission
     * @return true if sender has permission
     */
    boolean hasPermission(String node);
}
