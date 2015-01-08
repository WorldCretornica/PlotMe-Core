package com.worldcretornica.plotme_core.api;

public interface ICommandSender {

    /**
     * Sends this sender a message
     *
     * @param message Message to be displayed
     */

    void sendMessage(String message);

    String getName();

}
