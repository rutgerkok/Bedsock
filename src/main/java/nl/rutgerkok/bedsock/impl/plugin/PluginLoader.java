package nl.rutgerkok.bedsock.impl.plugin;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.charset.StandardCharsets;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.IdentityHashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import nl.rutgerkok.bedsock.ServerWrapper;
import nl.rutgerkok.bedsock.json.InvalidConfigException;
import nl.rutgerkok.bedsock.json.JsonConfigs;
import nl.rutgerkok.bedsock.plugin.Plugin;
import nl.rutgerkok.bedsock.plugin.PluginDescription;
import nl.rutgerkok.bedsock.plugin.PluginException;

public final class PluginLoader {

    static class ActivePlugin implements AutoCloseable {
        final PluginDescription description;
        final URLClassLoader classLoader;
        final Plugin plugin;
        boolean enabled = false;

        public ActivePlugin(PluginDescription description, URLClassLoader classLoader, Plugin plugin) {
            this.description = Objects.requireNonNull(description, "description");
            this.classLoader = Objects.requireNonNull(classLoader, "classLoader");
            this.plugin = Objects.requireNonNull(plugin, "plugin");
        }

        @Override
        public void close() throws PluginException {
            try {
                this.plugin.onDisable();
            } catch (Throwable t) {
                throw new PluginException(this.description, "Error disabling plugin", t);
            } finally {
                try {
                    this.classLoader.close();
                } catch (IOException e) {
                    throw new PluginException(this.description, "Error disabling class loader", e);
                }
            }
        }

        public void enable(ServerWrapper server) throws PluginException {
            PluginLogger pluginLogger = new PluginLogger(description.getName(), server.getLogger());
            plugin.onEnable(server, pluginLogger);
            pluginLogger.info("Plugin is enabled");
        }
    }

    private final Map<Plugin, ActivePlugin> plugins = new IdentityHashMap<>();

    /**
     * Enables all plugins.
     *
     * @param server
     *            The server instance.
     * @throws PluginException
     *             If enabling fails for at least a single plugin.
     */
    public void enablePlugins(ServerWrapper server) throws PluginException {
        PluginException thrown = null;
        for (ActivePlugin activePlugin : this.plugins.values()) {
            if (!activePlugin.enabled) {
                try {
                    activePlugin.enable(server);
                } catch (PluginException e) {
                    thrown = e;
                }
            }
        }
        if (thrown != null) {
            throw thrown;
        }
    }

    private void loadPlugin(Path jarFile) throws PluginException {
        // Create temporary description until actual one can be loaded
        PluginDescription description = () -> jarFile.getFileName().toString();

        try (JarFile jar = new JarFile(jarFile.toFile())) {
            JarEntry entry = jar.getJarEntry("plugin.json");
            try (BufferedReader reader = new BufferedReader(
                    new InputStreamReader(jar.getInputStream(entry), StandardCharsets.UTF_8))) {
                description = new PluginDescriptionImpl(JsonConfigs.loadFromReader(reader));
                URLClassLoader classLoader = new URLClassLoader(new URL[] { jarFile.toFile().toURI().toURL() },
                        ClassLoader.getSystemClassLoader());
                Plugin plugin = (Plugin) classLoader.loadClass(description.getMainClass()).newInstance();
                ActivePlugin activePlugin = new ActivePlugin(description, classLoader, plugin);
                this.plugins.put(plugin, activePlugin);
            } catch (InvalidConfigException e) {
                throw new PluginException(description, "Invalid plugin description file in " + jarFile.getFileName(),
                        e);
            } catch (ClassCastException e) {
                throw new PluginException(description, "Main class  must implement " + Plugin.class, e);
            } catch (ClassNotFoundException e) {
                throw new PluginException(description, "Main class not found.", e);
            } catch (InstantiationException e) {
                throw new PluginException(description, "Error instantiating plugin", e.getCause());
            } catch (IllegalAccessException e) {
                throw new PluginException(description, "Main class has no public no-args constructor.", e);
            }
        } catch (IOException e) {
            throw new PluginException(description,
                    "Failed to read file - is the file corrupted?", e);
        }
    }

    /**
     * Loads all plugins from a folder. If the folder does not exist yet, it is
     * created.
     *
     * @param folder
     *            The folder.
     * @throws PluginException
     *             If a plugin cannot be loaded.
     * @throws IOException
     *             If an IO error occurs reading the directory.
     */
    public void loadPlugins(Path folder) throws PluginException, IOException {
        if (!Files.exists(folder)) {
            Files.createDirectories(folder);
        }

        try (DirectoryStream<Path> stream = Files.newDirectoryStream(folder)) {
            for (Path file : stream) {
                if (file.getFileName().toString().toLowerCase(Locale.ROOT).endsWith(".jar")) {
                    loadPlugin(file);
                }
            }
        }
    }

    void unloadAllPlugins() throws PluginException {
        Throwable last = null;
        PluginDescription offender = null;

        for (ActivePlugin activePlugin : this.plugins.values()) {
            try {
                activePlugin.close();
            } catch (Throwable e) {
                // Don't fail-fast, unload other plugins first
                last = e;
                offender = activePlugin.description;
            }
        }
        this.plugins.clear();
        if (last != null) {
            throw new PluginException(offender, "Error disabling plugin", last);
        }
    }

    /**
     * Unloads a single plugin.
     *
     * @param plugin
     *            The plugin.
     * @throws PluginException
     *             If the plugin throws an exception.
     */
    void unloadPlugin(Plugin plugin) throws PluginException {
        ActivePlugin activePlugin = this.plugins.get(plugin);
        if (activePlugin != null) {
            try {
                activePlugin.close();
            } finally {
                this.plugins.remove(plugin);
            }
        }
    }
}