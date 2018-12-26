package nl.rutgerkok.bedsock.command;

/**
 * Used to capture the output of a command as a string.
 *
 */
public interface CommandSender {

    /**
     * Outputs a line.
     *
     * @param string
     *            The string.
     */
    void sendMessage(String string);
}
