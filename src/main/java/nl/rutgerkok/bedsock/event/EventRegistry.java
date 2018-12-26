package nl.rutgerkok.bedsock.event;

import java.util.function.Consumer;

import nl.rutgerkok.bedsock.plugin.ActivePlugin;

/**
 * Used to register event handlers.
 *
 */
public interface EventRegistry {

    /**
     * Calls the given event, then returns it.
     * @param event The event.
     * @return The same event, after all event listeners have been called.
     */
    <E extends Event> E callEvent(E event);

    /**
     * Registers an event handler manually.
     *
     * @param plugin
     *            The plugin that registered the event. In case the event handler
     *            throws an event, you will be blamed. <code>;)</code>
     * @param event
     *            The event to register for.
     * @param handler
     *            The event handler.
     * @param priority
     *            The event priority.
     */
    <E extends Event> void registerHandler(ActivePlugin plugin, Class<E> event, Consumer<? super E> handler,
            EventPriority priority);

    /**
     * Registers all public methods annotated with {@link EventHandler} in the given
     * object. Methods from any parent classes are included.
     *
     * @param plugin
     *            The plugin that registered the event. In case the event handler
     *            throws an event, you will be blamed. <code>;)</code>
     * @param listener
     *            The object.
     * @throws IllegalArgumentException
     *             If the class contains no methods annotated with
     *             {@link EventHandler}.
     * @throws IllegalArgumentException
     *             If one of the annotations is invalid (method has not one single
     *             {@link Event} arg).
     */
    void registerHandler(ActivePlugin plugin, Listener listener);

}
