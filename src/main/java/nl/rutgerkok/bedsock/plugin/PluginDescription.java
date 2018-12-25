package nl.rutgerkok.bedsock.plugin;

public interface PluginDescription {

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
