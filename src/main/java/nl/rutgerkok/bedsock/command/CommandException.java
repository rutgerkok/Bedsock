package nl.rutgerkok.bedsock.command;

/**
 * Thrown when executing a command fails.
 */
public final class CommandException extends Exception {

    private static final long serialVersionUID = 1L;

    public CommandException(String message) {
        super(message);
    }
}
