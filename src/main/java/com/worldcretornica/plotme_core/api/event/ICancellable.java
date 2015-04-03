package com.worldcretornica.plotme_core.api.event;

/**
 * An interface for events that can be canceled
 */
public interface ICancellable {

    /**
     * Checks if the {@link InternalPlotEvent} is cancelled.
     *
     * @return is the event cancelled
     */
    boolean isCancelled();

    /**
     * Sets the cancellation state of the {@link InternalPlotEvent}.
     **
     * @param cancel If the event will be canceled
     */

    void setCanceled(boolean cancel);
}
