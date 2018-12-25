package nl.rutgerkok.bedsock.plugin;

import nl.rutgerkok.bedsock.Logger;
import nl.rutgerkok.bedsock.ServerWrapper;

/**
 * Any plugin must implement this interface.
 *
 */
public interface Plugin {

    /**
     * Called when the plugin is getting disabled.
     */
    default void onDisable() {
        // Empty!
    }

    /**
     * Called when the server starts. You should store the server instance
     * somewhere, and use it to listen for events.
     *
     * @param server
     *            The server.
     * @param pluginLogger
     *            Logger that automatically adds your plugin name before anything.
     */
    void onEnable(ServerWrapper server, Logger pluginLogger);
}
