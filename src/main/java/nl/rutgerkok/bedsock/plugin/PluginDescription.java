package nl.rutgerkok.bedsock.plugin;

import java.util.Locale;

public interface PluginDescription {

    /**
     * Gets a name suitable for directories, commands, etc. For example, "My Plugin" becomes "my_plugin".
     * @return A machine name.
     */
    default String getMachineName() {
        return getName().replaceAll("[^A-Za-z0-9]", "_").toLowerCase(Locale.ROOT);
    }

    /**
     * Gets the fully-qualified class name of the startup class of the plugin.
     * @return The main class.
     */
    default String getMainClass() {
        throw new UnsupportedOperationException();
    }

    /**
     * Gets the name of the plugin.
     *
     * @return The plugin name.
     */
    String getName();

    /**
     * Gets the version of the plugin.
     *
     * @return The version.
     */
    default String getVersion() {
        return "?";
    }
}
