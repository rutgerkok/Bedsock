package nl.rutgerkok.bedsock;

import nl.rutgerkok.bedsock.command.CommandRegistry;

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
     * Gets the logger of the server.
     *
     * @return The logger.
     */
    Logger getLogger();
}
