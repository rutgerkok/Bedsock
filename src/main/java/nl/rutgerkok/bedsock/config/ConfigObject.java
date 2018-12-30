package nl.rutgerkok.bedsock.config;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.eclipse.jdt.annotation.Nullable;

/**
 * A map that can store ints, floats, bools, strings, arrays and other JSON
 * objects.
 *
 */
public final class ConfigObject {

    static String getTypeOf(@Nullable Object value) {
        if (value == null) {
            return "null";
        }
        if (value instanceof List) {
            return "list";
        }
        if (value instanceof Map) {
            return "object";
        }
        if (value instanceof Number) {
            return "number";
        }
        if (value instanceof String) {
            return "string";
        }
        return value.getClass().getSimpleName().toLowerCase(Locale.ROOT);
    }

    private final Map<String, Object> internal;

    public ConfigObject() {
        this(new HashMap<>());
    }

    ConfigObject(Map<String, Object> internal) {
        this.internal = internal;
    }

    /**
     * Gets an integer.
     *
     * @param key
     *            Storage location.
     * @return The integer.
     * @throws InvalidConfigException
     *             If no integer (or float) is stored at that location.
     */
    public int getInt(String key) throws InvalidConfigException {
        Object value = internal.get(key);
        if (value == null || !(value instanceof Number)) {
            throw new InvalidConfigException("Expected number at " + key + ", found " + getTypeOf(value));
        }
        return ((Number) value).intValue();
    }

    /**
     * Gets a child configuration object.
     *
     * @param key
     *            Storage location.
     * @return The object.
     * @throws InvalidConfigException
     *             If no integer is stored at that location.
     */
    @SuppressWarnings("unchecked")
    public ConfigObject getObject(String key) throws InvalidConfigException {
        Object value = internal.get(key);
        if (value == null || !(value instanceof Map)) {
            throw new InvalidConfigException("Expected object at " + key + ", found " + getTypeOf(value));
        }
        return new ConfigObject((Map<String, Object>) value);
    }

    /**
     * Gets a string stored at the given location.
     *
     * @param key
     *            Storage location.
     * @return The string.
     * @throws InvalidConfigException
     *             If no string is stored at that location.
     */
    public String getString(String key) throws InvalidConfigException {
        Object value = internal.get(key);
        if (value == null || !(value instanceof String)) {
            throw new InvalidConfigException("Expected string at " + key + ", found " + getTypeOf(value));
        }
        return (String) value;
    }

}
