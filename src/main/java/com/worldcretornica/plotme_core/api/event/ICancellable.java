package com.worldcretornica.plotme_core.api.event;

public interface ICancellable {

    boolean isCancelled();

    void setCancelled(boolean cancel);
}
