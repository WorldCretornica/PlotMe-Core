package com.worldcretornica.plotme_core.api.event.eventbus;

import static com.google.common.base.Preconditions.checkNotNull;

import com.worldcretornica.plotme_core.api.event.Event;

import java.lang.reflect.InvocationTargetException;

public abstract class EventHandler implements Comparable<EventHandler> {


    private final Order priority;

    /**
     * Create a new event handler.
     *
     * @param priority the priority
     */
    protected EventHandler(Order priority) {
        checkNotNull(priority);
        this.priority = priority;
    }

    /**
     * Get the priority.
     *
     * @return the priority
     */
    public Order getPriority() {
        return priority;
    }

    /**
     * Dispatch the given event.
     *
     * <p>Subclasses should override {@link #dispatch(Event)}.</p>
     *
     * @param event the event
     * @throws InvocationTargetException thrown if an exception is thrown during dispatch
     */
    public final void handleEvent(Event event) throws InvocationTargetException {
        try {
            dispatch(event);
        } catch (Throwable t) {
            throw new InvocationTargetException(t);
        }
    }

    /**
     * Dispatch the event.
     *
     * @param event the event object
     * @throws Exception an exception that may be thrown
     */
    public abstract void dispatch(Event event) throws Exception;

    @Override
    public int compareTo(EventHandler o) {
        return getPriority().ordinal() - o.getPriority().ordinal();
    }

    @Override
    public abstract int hashCode();

    @Override
    public abstract boolean equals(Object obj);

    @Override
    public String toString() {
        return "EventHandler{" +
                "priority=" + priority +
                '}';
    }

}
