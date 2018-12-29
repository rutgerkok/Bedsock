package nl.rutgerkok.bedsock.impl.plugin;

import java.io.IOException;
import java.net.URLClassLoader;
import java.util.Objects;

import nl.rutgerkok.bedsock.InactiveServer;
import nl.rutgerkok.bedsock.logger.Logger;
import nl.rutgerkok.bedsock.plugin.ActivePlugin;
import nl.rutgerkok.bedsock.plugin.Plugin;
import nl.rutgerkok.bedsock.plugin.PluginDescription;
import nl.rutgerkok.bedsock.plugin.PluginException;

class ActivePluginImpl implements ActivePlugin, AutoCloseable {
    final PluginDescription description;
    final URLClassLoader classLoader;
    final Plugin plugin;
    final Logger logger;
    private boolean enabled = false;

    public ActivePluginImpl(PluginDescription description, URLClassLoader classLoader, Plugin plugin,
            Logger serverLogger) {
        this.description = Objects.requireNonNull(description, "description");
        this.classLoader = Objects.requireNonNull(classLoader, "classLoader");
        this.plugin = Objects.requireNonNull(plugin, "plugin");

        this.logger = new PluginLogger(description.getName(), serverLogger);
    }

    @Override
    public void close() throws PluginException {
        try {
            this.plugin.onDisable();
            this.logger.info("Plugin is disabled");
        } catch (Throwable t) {
            throw new PluginException(this.description, "Error in plugin.onDisable", t);
        } finally {
            enabled = false;
            try {
                this.classLoader.close();
            } catch (IOException e) {
                throw new PluginException(this.description, "Error disabling class loader", e);
            }
        }
    }

    public void enable(InactiveServer server) throws PluginException {
        try {
            plugin.onEnable(server, this);
            logger.info("Plugin is enabled");
            enabled = true;
        } catch (Throwable t) {
            throw new PluginException(description, "Error on plugin.onEnable", t);
        }
    }

    @Override
    public Logger getLogger() {
        return logger;
    }

    @Override
    public Plugin getPlugin() {
        return plugin;
    }

    @Override
    public PluginDescription getPluginDescription() {
        return description;
    }

    boolean isEnabled() {
        return enabled;
    }
}