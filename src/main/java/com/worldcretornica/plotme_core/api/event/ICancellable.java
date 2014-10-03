package com.worldcretornica.plotme_core.api.event;

public interface ICancellable {

    public boolean isCancelled();
    
    public void setCancelled(boolean cancel);
}
