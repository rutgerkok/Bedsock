package nl.rutgerkok.bedsock.impl.event;

import static nl.rutgerkok.bedsock.util.NullAnnotation.nullable;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.function.Consumer;

import org.eclipse.jdt.annotation.Nullable;

import nl.rutgerkok.bedsock.event.Cancellable;
import nl.rutgerkok.bedsock.event.Event;
import nl.rutgerkok.bedsock.event.EventHandler;
import nl.rutgerkok.bedsock.event.EventPriority;
import nl.rutgerkok.bedsock.event.EventRegistry;
import nl.rutgerkok.bedsock.event.Listener;
import nl.rutgerkok.bedsock.plugin.ActivePlugin;

public class EventRegistryImpl implements EventRegistry {

    private static abstract class Handler {
        final ActivePlugin plugin;
        final EventPriority priority;

        Handler(ActivePlugin plugin, EventPriority priority) {
            this.plugin = Objects.requireNonNull(plugin, "plugin");
            this.priority = Objects.requireNonNull(priority, "priority");
        }

        EventPriority priority() {
            return priority;
        }

        abstract void run(Event event);
    }

    private static class HandlerList {
        private @Nullable List<Handler> sorted;
        private List<Handler> unsorted = new ArrayList<>();

        void add(Handler handler) {
            synchronized (this) {
                this.unsorted.add(handler);
            }
            this.sorted = null;
        }

        void run(Event event) {
            List<Handler> sorted = this.sorted;
            if (sorted == null) {
                // Need to sort
                synchronized (this) {
                    sorted = this.sorted;
                    if (sorted == null) { // Check again after lock has been acquired
                        Handler[] handlers = unsorted.toArray(new Handler[0]);
                        Arrays.sort(handlers, Comparator.comparing(Handler::priority));
                        sorted = Arrays.asList(handlers);
                        this.sorted = sorted;
                    }
                }
            }

            for (Handler handler : sorted) {
                handler.run(event);
            }
        }
    }

    private ConcurrentMap<Class<? extends Event>, HandlerList> handlers = new ConcurrentHashMap<>();

    @Override
    public <E extends Event> E callEvent(E event) {
        HandlerList list = this.handlers.get(event.getClass());
        if (list != null) {
            list.run(event);
        }
        return event;
    }

    @Override
    public <E extends Event> void registerHandler(ActivePlugin plugin, Class<E> event, Consumer<? super E> handler,
            EventPriority priority) {
        Handler internalHandler = new Handler(plugin, priority) {
            @SuppressWarnings("unchecked") // Save as long as handler is put in the right list
            @Override
            void run(Event event) {
                try {
                    handler.accept((E) event);
                } catch (Throwable t) {
                    this.plugin.getLogger().error("Error in event " + event.getClass().getSimpleName(), t);
                }
            }
        };

        handlers.computeIfAbsent(event, e -> new HandlerList()).add(internalHandler);
    }

    @Override
    public void registerHandler(ActivePlugin plugin, Listener listener) {
        for (Method method : listener.getClass().getMethods()) {
            EventHandler annotation = nullable(method.getAnnotation(EventHandler.class));
            if (annotation == null) {
                continue;
            }

            // Get event handler
            Class<?>[] parameters = method.getParameterTypes();
            if (parameters.length != 1) {
                throw new IllegalArgumentException("Event handler " + method.getName() + " on class "
                        + listener.getClass() + " must have 1 parameter, but has " + parameters.length);
            }
            Class<?> eventType = parameters[0];
            if (!Event.class.isAssignableFrom(eventType) || Event.class.equals(eventType)) {
                throw new IllegalArgumentException(
                        "Event handler " + method.getName() + " on class " + listener.getClass()
                                + " must listen to a subclass of Event, but is listening to " + eventType);
            }

            Handler internalHandler = new Handler(plugin, annotation.priority()) {
                @Override
                void run(Event event) {
                    if (annotation.ignoreCancelled() && event instanceof Cancellable) {
                        if (((Cancellable) event).isCancelled()) {
                            return;
                        }
                    }

                    try {
                        method.invoke(listener, event);
                    } catch (IllegalAccessException e) {
                        // Private methods should already be filtered out during registration
                        throw new RuntimeException(e);
                    } catch (InvocationTargetException e) {
                        plugin.getLogger().error("Error in event " + event.getClass().getSimpleName(),
                                e.getCause());
                    }
                }
            };

            handlers.computeIfAbsent(eventType.asSubclass(Event.class), e -> new HandlerList()).add(internalHandler);
        }

    }

}
