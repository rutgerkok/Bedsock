package nl.rutgerkok.bedsock.util;

import java.util.Objects;

import org.eclipse.jdt.annotation.Nullable;

public final class NullAnnotation {

    /**
     * If you're using a library that doesn't use {@link Nullable} annotations, then
     * you may run into a situation where your type checker guesses the type
     * incorrectly. If the object is inferred as nullable, but you know it is
     * completely impossible to be null, then you can call this method to make it
     * non-null. An {@link AssertionError} will be thrown if the variable is in fact
     * null.
     *
     * @param object
     *            The non-null object.
     * @return The same object.
     * @see Objects#requireNonNull(Object, String) For validation.
     * @throws AssertionError
     *             If the object is actually null. This should never happen, do
     *             validation using {@link Objects#requireNonNull(Object, String)}.
     */
    public static <T> T nonNull(@Nullable T object) {
        if (object == null) {
            throw new AssertionError("Object was null");
        }
        return object;
    }

    /**
     * If you're using a library that doesn't use {@link Nullable} annotations, then
     * you may run into a situation where your type checker guesses the type incorrectly. If the object is inferred non-null, but you know it is nullable, then you can call this method over that object to make the object nullable.
     *
     * @param object
     *            The object.
     * @return Exactly the same object, but now the compiler knows it's nullable.
     */
    public static <@Nullable T> T nullable(T object) {
        return object;
    }
}
