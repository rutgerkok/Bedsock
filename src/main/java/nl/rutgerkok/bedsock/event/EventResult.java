package nl.rutgerkok.bedsock.event;

/**
 * Represents the outcome of an event.
 *
 */
public enum EventResult {
    /**
     * Event is cancelled: the default action is prevent.
     */
    CANCELLED,
    /**
     * The default action continues.
     */
    DEFAULT,
    /**
     * The default action is cancelled, but a plugin will do something different
     * instead. So the event is not cancelled, just the default action.
     */
    CUSTOM_HANDLING;

    /**
     * Returns true if the event was cancelled, for example because the player has
     * no permission. If the default action of the server is prevented, but a plugin
     * is doing something else (see {@link #CUSTOM_HANDLING}) then this will still
     * return {@code false}.
     *
     * @return True if the event was cancelled, false otherwise.
     */
    public boolean isCancelled() {
        return this == CANCELLED;
    }

    /**
     * Returns true if the default action is prevented.
     *
     * @return True if the default action is prevented, false otherwise.
     */
    public boolean preventsDefault() {
        return this != DEFAULT;
    }
}
