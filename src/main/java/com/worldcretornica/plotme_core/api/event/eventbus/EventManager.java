/*
 * This file contains code which has been inspired by or modified from
 * the following projects:
 *
 * Sponge, licensed under the MIT License (MIT).
 * Copyright (c) SpongePowered.org <http://www.spongepowered.org>
 * Copyright (c) contributors
 *
 * WorldEdit, licensed under under the terms of the GNU Lesser General Public License version 3.
 * Copyright (C) sk89q <http://www.sk89q.com>
 * Copyright (C) WorldEdit team and contributors
 *
 * Contributors of this repository do not claim ownership of this code and all rights belong to the works
 * this is derived from.
 */

package com.worldcretornica.plotme_core.api.event.eventbus;


import com.worldcretornica.plotme_core.api.event.Event;

/**
 * Manages the registration of event handlers and the dispatching of events.
 */
public interface EventManager {

    /**
     * Registers an object to receive {@link Event}s.
     *
     * @param obj The object
     */
    void register(Object obj);

    /**
     * Un-registers an object from receiving {@link Event}s.
     *
     * @param obj The object
     */
    void unregister(Object obj);

    /**
     * Calls a {@link Event} to all handlers that handle it.
     *
     * @param event The event
     * @return True if canceled, false if not
     */
    boolean post(Event event);

}
