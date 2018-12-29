package nl.rutgerkok.bedsock;

import nl.rutgerkok.bedsock.command.CommandRegistry;
import nl.rutgerkok.bedsock.event.EventRegistry;
import nl.rutgerkok.bedsock.logger.ForwardingLogger;
import nl.rutgerkok.bedsock.plugin.PluginRegistry;

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
     * Gets the plugin registry, which holds all loaded plugins.
     * 
     * @return The plugin registry.
     */
    PluginRegistry getPluginRegistry();

    /**
     * Gets the logger of the server. The methods in this class can be called from
     * any thread.
     *
     * @return The logger.
     */
    ForwardingLogger getServerLogger();
}
