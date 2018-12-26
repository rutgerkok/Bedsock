package nl.rutgerkok.bedsock.event;

public interface Cancellable {

    /**
     * Gets the event result, which indicates whether the event was cancelled and whether the default action should continue.
     * @return The result of the event.
     */
    EventResult getResult();

    /**
     * Returns true if the event was cancelled, for example because the player has
     * no permission. If the default action of the server is prevented, but a plugin
     * is doing something else (see {@link EventResult#CUSTOM_HANDLING}) then this will still
     * return {@code false}.
     *
     * @return True if the event was cancelled, false otherwise.
     */
    default boolean isCancelled() {
        return getResult().isCancelled();
    }

    /**
     * Returns true if the default action is prevented.
     *
     * @return True if the default action is prevented, false otherwise.
     */
    default boolean preventsDefault() {
        return getResult().preventsDefault();
    }

    /**
     * Sets the result of the event.
     * 
     * @param result
     *            The new result.
     */
    void setResult(EventResult result);
}
