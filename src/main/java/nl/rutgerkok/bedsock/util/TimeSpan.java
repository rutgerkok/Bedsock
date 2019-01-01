package nl.rutgerkok.bedsock.util;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

import org.eclipse.jdt.annotation.Nullable;

/**
 * Represents a certain amount of time.
 *
 *
 */
public final class TimeSpan {

    /**
     * A time span of a number of hours.
     *
     * @param hours
     *            The number of hours.
     * @return The time span.
     * @throws IllegalArgumentException
     *             If {@code hours < 0}.
     */
    public static TimeSpan hours(int hours) {
        return new TimeSpan(hours, TimeUnit.HOURS);
    }

    /**
     * A time span of a number of minutes.
     *
     * @param minutes
     *            The number of minutes.
     * @return The time span.
     * @throws IllegalArgumentException
     *             If {@code minutes < 0}.
     */
    public static TimeSpan minutes(int minutes) {
        return new TimeSpan(minutes, TimeUnit.MINUTES);
    }

    /**
     * Constructs some other time span.
     * @param value The value.
     * @param unit The unit of the value.
     * @return The time span.
     * @throws IllegalArgumentException If {@code value < 0}.
     */
    public static TimeSpan of(int value, TimeUnit unit) {
        return new TimeSpan(value, unit);
    }

    /**
     * A time span of a number of seconds.
     * @param seconds The number of seconds.
     * @return The time span.
     * @throws IllegalArgumentException If {@code seconds < 0}.
     */
    public static TimeSpan seconds(int seconds) {
        return new TimeSpan(seconds, TimeUnit.SECONDS);
    }

    private final int value;
    private final TimeUnit unit;

    private TimeSpan(int value, TimeUnit unit) {
        if (value < 0) {
            throw new IllegalArgumentException("Value must be larger than 0, was " + value);
        }
        this.value = value;
        this.unit = Objects.requireNonNull(unit, "unit");
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        TimeSpan other = (TimeSpan) obj;
        if (unit != other.unit) {
            return false;
        }
        if (value != other.value) {
            return false;
        }
        return true;
    }

    /**
     * Gets the unit.
     * @return The unit.
     */
    public TimeUnit getUnit() {
        return unit;
    }

    /**
     * Gets the value.
     * @return The value.
     */
    public int getValue() {
        return value;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + unit.hashCode();
        result = prime * result + value;
        return result;
    }

    @Override
    public String toString() {
        return "TimeSpan [value=" + value + ", unit=" + unit + "]";
    }
}
