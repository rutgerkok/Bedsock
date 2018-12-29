package nl.rutgerkok.bedsock.plugin;

import java.util.List;

/**
 * Used to query the active plugins.
 *
 */
public interface PluginRegistry {

    /**
     * Gets an (immutable) list of all active plugins.
     * 
     * @return List of all active plugins.
     */
    List<? extends PluginDescription> getActivePlugins();
}
