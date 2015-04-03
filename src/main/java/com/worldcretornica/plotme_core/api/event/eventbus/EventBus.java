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

import static com.google.common.base.Preconditions.checkNotNull;
import static java.util.logging.Level.SEVERE;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;
import com.google.common.reflect.TypeToken;
import com.worldcretornica.plotme_core.api.event.Event;
import com.worldcretornica.plotme_core.api.event.ICancellable;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

public class EventBus implements EventManager {

    private final Object lock = new Object();
    private final Multimap<Class<?>, MethodEventHandler> handlersByEvent = HashMultimap.create();

    /**
     * A cache of all the handlers for an event type for quick event posting.
     *
     *
     * The cache is currently entirely invalidated if handlers are added or
     * removed.
     *
     */
    private final LoadingCache<Class<?>, HandlerCache> handlersCache =
            CacheBuilder.newBuilder().build(new CacheLoader<Class<?>, HandlerCache>() {

                @Override
                public HandlerCache load(Class<?> type) throws Exception {
                    return bakeHandlers(type);
                }
            });
    private final Logger logger;

    public EventBus(Logger logger) {

        this.logger = logger;
    }

    private static boolean isValidHandler(Method method) {
        Class<?>[] paramTypes = method.getParameterTypes();
        return !Modifier.isStatic(method.getModifiers())
                && !Modifier.isAbstract(method.getModifiers())
                && !Modifier.isInterface(method.getDeclaringClass().getModifiers())
                && method.getReturnType() == void.class
                && paramTypes.length == 1
                && Event.class.isAssignableFrom(paramTypes[0]);
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    private HandlerCache bakeHandlers(Class<?> rootType) {
        List<MethodEventHandler> registrations = Lists.newArrayList();
        Set<Class<?>> types = (Set) TypeToken.of(rootType).getTypes().rawTypes();

        synchronized (this.lock) {
            for (Class<?> type : types) {
                if (Event.class.isAssignableFrom(type)) {
                    registrations.addAll(this.handlersByEvent.get(type));
                }
            }
        }

        Collections.sort(registrations);

        return new HandlerCache(registrations);
    }

    private HandlerCache getHandlerCache(Class<?> type) {
        return this.handlersCache.getUnchecked(type);
    }

    @SuppressWarnings("unchecked")
    private List<Subscriber> findAllSubscribers(Object object) {
        List<Subscriber> subscribers = Lists.newArrayList();
        Class<?> type = object.getClass();

        for (Method method : type.getMethods()) {
            Subscribe subscribe = method.getAnnotation(Subscribe.class);

            if (subscribe != null) {
                Class<?>[] paramTypes = method.getParameterTypes();

                if (isValidHandler(method)) {
                    Class<Event> eventType = (Class<Event>) paramTypes[0];
                    MethodEventHandler handler = new MethodEventHandler(subscribe.order(), object, method);
                    subscribers.add(new Subscriber(eventType, handler));
                } else {
                    logger.log(SEVERE, "The method {} has @Subscribe but has the wrong signature", method);
                }
            }
        }

        return subscribers;
    }

    public boolean register(Class<?> type, MethodEventHandler handler) {
        return register(new Subscriber(type, handler));
    }

    public boolean register(Subscriber subscriber) {
        return registerAll(Lists.newArrayList(subscriber));
    }

    private boolean registerAll(List<Subscriber> subscribers) {
        synchronized (this.lock) {
            boolean changed = false;

            for (Subscriber sub : subscribers) {
                if (this.handlersByEvent.put(sub.getEventClass(), sub.getHandler())) {
                    changed = true;
                }
            }

            if (changed) {
                this.handlersCache.invalidateAll();
            }

            return changed;
        }
    }

    public boolean unregister(Class<?> type, MethodEventHandler handler) {
        return unregister(new Subscriber(type, handler));
    }

    public boolean unregister(Subscriber subscriber) {
        return unregisterAll(Lists.newArrayList(subscriber));
    }

    public boolean unregisterAll(List<Subscriber> subscribers) {
        synchronized (this.lock) {
            boolean changed = false;

            for (Subscriber sub : subscribers) {
                if (this.handlersByEvent.remove(sub.getEventClass(), sub.getHandler())) {
                    changed = true;
                }
            }

            if (changed) {
                this.handlersCache.invalidateAll();
            }

            return changed;
        }
    }

    private void callListener(EventHandler handler, Event event) {
        try {
            handler.handleEvent(event);
        } catch (Throwable t) {
            logger.log(Level.SEVERE, "A handler raised an error when handling an event", t);
        }
    }

    @Override
    public void register(Object object) {
        checkNotNull(object, "object");

        registerAll(findAllSubscribers(object));
    }

    @Override
    public void unregister(Object object) {
        unregisterAll(findAllSubscribers(object));
    }

    @Override
    public boolean post(Event event) {
        for (Order order : Order.values()) {
            for (EventHandler handler : getHandlerCache(event.getClass()).getHandlersByOrder(order)) {
                callListener(handler, event);
            }
        }

        return event instanceof ICancellable && ((ICancellable) event).isCancelled();
    }

}
