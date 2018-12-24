package nl.rutgerkok.bedsock;

public interface SockLogger {

    /**
     * The level at which a message is logged.
     */
    enum LogLevel {
        INFO, DEBUG, ERROR, WARNING
    }

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
    default void debug(String message) {
        log(LogLevel.DEBUG, message);
    }

    /**
     * Logs an error.
     *
     * @param message
     *            The error message.
     */
    default void error(String message) {
        log(LogLevel.ERROR, message);
    }

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
    default void info(String message) {
        log(LogLevel.INFO, message);
    }

    /**
     * Logs a message.
     * @param level Level to log at.
     * @param message The message.
     */
    void log(LogLevel level, String message);

    /**
     * Logs a warning.
     * @param message The warning message.
     */
    default void warning(String message) {
        log(LogLevel.WARNING, message);
    }
}
