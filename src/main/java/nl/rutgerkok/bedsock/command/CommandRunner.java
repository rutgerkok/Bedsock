package nl.rutgerkok.bedsock.command;

/**
 * Used to send commands to the Bedrock server.
 *
 */
public interface CommandRunner {

    /**
     * Runs a command on the Bedrock server. If the command starts with a slash,
     * this slash will be removed.
     *
     * @param command
     *            The command.
     * @throws CommandException
     *             If the command cannot be passed to the bedrock server, for
     *             example because it is empty.
     */
    void runCommand(String command) throws CommandException;
}
