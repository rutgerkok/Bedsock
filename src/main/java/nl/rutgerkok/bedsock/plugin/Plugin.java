package nl.rutgerkok.bedsock.plugin;

import nl.rutgerkok.bedsock.InactiveServer;

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
     * Called before the server starts. You can register commands, events, resource
     * packs, behavior packs and other things here.
     *
     * @param server
     *            The server.
     * @param plugin
     *            The active plugin, which provides access to the the logger, the
     *            plugin name and version.
     */
    default void onEnable(InactiveServer server, ActivePlugin plugin) {
        // Empty!
    }
}
