package nl.rutgerkok.bedsock.impl.plugin;

import java.util.Objects;

import nl.rutgerkok.bedsock.Logger;

/**
 * Logger that prefixes all messages with the name of the plugin.
 *
 */
final class PluginLogger implements Logger {

    private final String prefix;
    private final Logger internal;

    public PluginLogger(String name, Logger logger) {
        this.prefix = "[" + Objects.requireNonNull(name, "name") + "] ";
        this.internal = Objects.requireNonNull(logger, "logger");
    }

    @Override
    public void chat(String name, String message) {
        internal.chat(name, message);
    }

    @Override
    public void error(String message, Throwable e) {
        internal.error(prefix + message, e);
    }

    @Override
    public void log(LogLevel level, String message) {
        internal.log(level, prefix + message);
    }

}
