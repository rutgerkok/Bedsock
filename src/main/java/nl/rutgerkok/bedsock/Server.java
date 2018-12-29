package nl.rutgerkok.bedsock;

import nl.rutgerkok.bedsock.command.CommandRegistry;
import nl.rutgerkok.bedsock.event.EventRegistry;

/**
 * Represents a Bedrock server. It is unspecified whether the server is already
 * booted up.
 *
 */
public interface Server {

    /**
     * Gets a registry to register commands for the wrapper.
     *
     * @return The registry.
     */
    CommandRegistry getCommandRegistry();

    /**
     * Gets the event registry, used to fire and listen for events.
     *
     * @return The event registry.
     */
    EventRegistry getEventRegistry();

    /**
     * Gets the logger of the server.
     *
     * @return The logger.
     */
    Logger getLogger();
}
