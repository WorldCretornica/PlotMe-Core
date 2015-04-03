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

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;

class HandlerCache {

    private final List<EventHandler> handlers;
    private final EnumMap<Order, List<EventHandler>> orderGrouped;

    @SuppressWarnings("unchecked")
    HandlerCache(List<MethodEventHandler> registrations) {
        this.handlers = Lists.newArrayList();
        for (MethodEventHandler reg : registrations) {
            this.handlers.add(reg);
        }

        this.orderGrouped = Maps.newEnumMap(Order.class);
        for (Order order : Order.values()) {
            this.orderGrouped.put(order, new ArrayList<EventHandler>());
        }
        for (MethodEventHandler reg : registrations) {
            this.orderGrouped.get(reg.getPriority()).add(reg);
        }
    }

    public List<EventHandler> getHandlers() {
        return this.handlers;
    }

    public List<EventHandler> getHandlersByOrder(Order order) {
        return this.orderGrouped.get(order);
    }

}
