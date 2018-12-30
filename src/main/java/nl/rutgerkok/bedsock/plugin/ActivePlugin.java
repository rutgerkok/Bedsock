package nl.rutgerkok.bedsock.plugin;

import java.nio.file.Path;

import nl.rutgerkok.bedsock.ServerFolders;
import nl.rutgerkok.bedsock.logger.Logger;

/**
 * Represents a plugin that is active on the server.
 *
 */
public interface ActivePlugin {

    /**
     * Gets the configuration folder for the given plugin.
     *
     * @param folders
     *            The folder structure of the server.
     * @return A configuration folder.
     */
    default Path getConfigFolder(ServerFolders folders) {
        return folders.getConfigFolder().resolve(getPluginDescription().getMachineName());
    }

    /**
     * A plugin-specific logger. Plugins should log all messages using this logger.
     * @return The logger.
     */
    Logger getLogger();

    /**
     * The main object of the plugin.
     * @return The plugin object.
     */
    Plugin getPlugin();

    /**
     * Name, version, etc. of the plugin.
     * 
     * @return Plugin description.
     */
    PluginDescription getPluginDescription();

}
