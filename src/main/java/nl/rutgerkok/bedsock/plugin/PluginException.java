package nl.rutgerkok.bedsock.plugin;

/**
 * Used to wrap exceptions thrown by plugins.
 *
 */
public final class PluginException extends Exception {

    private static final long serialVersionUID = 127479553863082835L;

    public PluginException(PluginDescription plugin, String reason, Throwable e) {

    }
}
