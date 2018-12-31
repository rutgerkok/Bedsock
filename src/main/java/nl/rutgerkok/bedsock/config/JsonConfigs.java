package nl.rutgerkok.bedsock.config;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;

import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import nl.rutgerkok.bedsock.ServerFolders;
import nl.rutgerkok.bedsock.plugin.ActivePlugin;

/**
 * Used to save/load JSON configs.
 *
 */
public final class JsonConfigs {

    private static final String JSON_CONFIG_FILE = "config.json";

    /**
     * Loads the JSON configuration file of a plugin from disk. If no such file
     * exists an empty configuration object is returned.
     *
     * @param folders
     *            The folder structure of the server.
     * @param plugin
     *            The plugin to load the configuration file for.
     * @return The configuration object, or an empty object if that file doesn't
     *         exist yet.
     */
    public static ConfigObject loadForPlugin(ServerFolders folders, ActivePlugin plugin) {
        Path file = plugin.getConfigFolder(folders).resolve(JSON_CONFIG_FILE);
        if (Files.exists(file)) {
            try (BufferedReader reader = Files.newBufferedReader(file, StandardCharsets.UTF_8)) {
                return loadFromReader(reader);
            } catch (IOException | InvalidConfigException e) {
                plugin.getLogger().error("Could not read configuration file", e);
            }
        }
        return new ConfigObject();
    }

    /**
     * Reads a JSON stream into a configuration object.
     *
     * @param reader
     *            The JSON stream.
     * @return The configuration object.
     * @throws InvalidConfigException
     *             If parsing fails.
     */
    @SuppressWarnings("unchecked") // Safe, it's JSON
    public static ConfigObject loadFromReader(Reader reader) throws InvalidConfigException {
        Object value = JSONValue.parse(reader);
        if (value instanceof JSONObject) {
            return new ConfigObject((Map<String, Object>) value);
        }
        throw new InvalidConfigException("Expected object in config file, found " + ConfigObject.getTypeOf(value));
    }

    public static void saveForPlugin(ServerFolders folders, ActivePlugin plugin, ConfigObject config) {
        Path file = plugin.getConfigFolder(folders).resolve(JSON_CONFIG_FILE);
        try {
            Files.createDirectories(file.getParent());
            try (BufferedWriter writer = Files.newBufferedWriter(file, StandardCharsets.UTF_8)) {
                saveToWriter(writer, config);
            }
        } catch (IOException e) {
            plugin.getLogger().error("Failed to save configuration file", e);
        }
    }

    /**
     * Writes the given configuration object to a writer.
     * 
     * @param writer
     *            The writer, see for example
     *            {@link Files#newBufferedWriter(Path, java.nio.charset.Charset, java.nio.file.OpenOption...)}.
     * @param config
     *            The configuration object.
     * @throws IOException
     *             If writing fails.
     */
    public static void saveToWriter(Writer writer, ConfigObject config) throws IOException {
        JSONObject.writeJSONString(config.accessMap(), writer);
    }

}
