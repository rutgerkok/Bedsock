package nl.rutgerkok.bedsock.json;

import java.io.Reader;
import java.util.Map;

import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

/**
 * Used to save/load JSON configs.
 *
 */
public final class JsonConfigs {

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
}
