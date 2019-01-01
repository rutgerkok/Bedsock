package nl.rutgerkok.bedsock.event;

import java.util.Objects;

import nl.rutgerkok.bedsock.logger.Logger;
import nl.rutgerkok.bedsock.plugin.ActivePlugin;

/**
 * Represents an event listener. Classes implementing this interface should have
 * at least one public method with the {@link EventHandler} annotation.
 *
 */
public abstract class Listener {

    private final ActivePlugin plugin;

    protected Listener(ActivePlugin plugin) {
        this.plugin = Objects.requireNonNull(plugin, "plugin");
    }

    /**
     * Gets the plugin logger.
     * @return The plugin logger.
     */
    public Logger getLogger() {
        return this.plugin.getLogger();
    }

    /**
     * Gets the plugin that owns this listener.
     *
     * @return The plugin.
     */
    public ActivePlugin getPlugin() {
        return this.plugin;
    }
}
