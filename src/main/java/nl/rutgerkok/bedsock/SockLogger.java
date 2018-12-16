package nl.rutgerkok.bedsock;

public interface SockLogger {

    /**
     * Logs a chat message.
     *
     * @param name
     *            Name of the player.
     * @param message
     *            The message.
     */
    void chat(String name, String message);

    /**
     * Logs a debugging message.
     *
     * @param message
     *            The message.
     */
    void debug(String message);

    /**
     * Logs an error.
     *
     * @param message
     *            The error message.
     */
    void error(String message);

    /**
     * Logs an error with a strack trace.
     * 
     * @param message
     *            The error.
     * @param e
     *            The exception.
     */
    void error(String message, Throwable e);

    /**
     * Logs a message.
     *
     * @param message
     *            The message.
     */
    void info(String message);

    /**
     * Logs a warning.
     * @param message The warning message.
     */
    void warning(String message);
}
