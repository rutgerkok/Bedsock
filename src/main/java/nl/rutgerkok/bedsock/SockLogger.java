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
     * Logs a message.
     * 
     * @param message
     *            The message.
     */
    void info(String message);

    /**
     * Logs an error.
     * 
     * @param message
     *            The error message.
     */
    void error(String message);

    /**
     * Logs a warning.
     * @param message The warning message.
     */
    void warning(String message);
}
