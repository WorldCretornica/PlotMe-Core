package com.worldcretornica.plotme_core.api;

public interface ICommandSender extends IActor {

    void sendMessage(String msg);

    @Override
    boolean hasPermission(String node);

    String getName();

}
