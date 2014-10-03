package com.worldcretornica.plotme_core.api;

public interface ICommandSender {

    void sendMessage(String c);

    boolean hasPermission(String node);

    String getName();

}
