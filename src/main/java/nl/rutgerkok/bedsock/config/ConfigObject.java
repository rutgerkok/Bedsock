package nl.rutgerkok.bedsock.config;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

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

    Map<String, Object> accessMap() {
        return internal;
    }

    /**
     * Gets an integer. If a float is stored at the given location, it is
     * automatically cast to an integer.
     *
     * @param key
     *            Storage location.
     * @return The integer.
     * @throws InvalidConfigException
     *             If no integer (or float) is stored at that location.
     */
    public int getInt(String key) throws InvalidConfigException {
        Objects.requireNonNull(key, "key");
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
     * Gets the given int. If no int is stored at the given location, the given
     * default value is stored at that location.
     * 
     * @param key
     *            The storage location.
     * @param defaultValue
     *            Value used if no integer is stored at the given location.
     * @return The int stored at the given location, or the default value.
     */
    public int getOrPlaceInt(String key, int defaultValue) {
        try {
            return getInt(key);
        } catch (InvalidConfigException e) {
            internal.put(key, defaultValue);
            return defaultValue;
        }
    }

    /**
     * Gets the given string. If no string is stored at the given location, the
     * default value is stored at that location.
     * 
     * @param key
     *            The storage location.
     * @param defaultValue
     *            Default value, used if there is no string stored.
     * @return The string at the given location, or the default value.
     */
    public String getOrPlaceString(String key, String defaultValue) {
        Objects.requireNonNull(defaultValue, "defaultValue");
        try {
            return getString(key);
        } catch (InvalidConfigException e) {
            internal.put(key, defaultValue);
            return defaultValue;
        }
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
        Objects.requireNonNull(key, "key");
        Object value = internal.get(key);
        if (value == null || !(value instanceof String)) {
            throw new InvalidConfigException("Expected string at " + key + ", found " + getTypeOf(value));
        }
        return (String) value;
    }

}
