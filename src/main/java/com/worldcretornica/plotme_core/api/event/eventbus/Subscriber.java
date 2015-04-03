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

class Subscriber {

    private final Class<?> eventClass;
    private final MethodEventHandler handler;

    Subscriber(Class<?> eventClass, MethodEventHandler handler) {
        this.eventClass = eventClass;
        this.handler = handler;
    }

    public Class<?> getEventClass() {
        return this.eventClass;
    }

    public MethodEventHandler getHandler() {
        return this.handler;
    }
}
