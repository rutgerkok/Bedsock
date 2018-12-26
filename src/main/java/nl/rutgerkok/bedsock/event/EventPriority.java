package nl.rutgerkok.bedsock.event;

/**
 * Controls when your event handler is called.
 *
 */
public enum EventPriority {

    /**
     * Should not be used under normal circumstances. Useful if you need to look at
     * the unmodified event.
     */
    EARLIEST,
    /**
     * Suitable for plugins that want to make their action known to other plugins. A
     * land protection plugin would register here, so that all other
     */
    EARLY,
    /**
     * Suitable for most plugins.
     */
    NORMAL,
    /**
     * Suitable to plugins that override another plugin.
     */
    LATE,
    /**
     * Should not be used under normal circumstances. Useful if you need to override
     * a plugin that has the {@link #LATE} priority.
     */
    LATEST,
    /**
     * Suitable for plugins that want to know the outcome of the event. Do not
     * modify the cancelled state here.
     */
    MONITOR
}
