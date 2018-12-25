package nl.rutgerkok.bedsock.command;

/**
 * Used to send commands to the Bedrock server.
 *
 */
public interface CommandRunner {

    /**
     * Runs a command on the Bedrock server. If the command starts with a slash, the
     * slash will be removed. The output of the command is captured, and returned as
     * a string.
     * <p>
     * Note: this method cannot be called from the thread that reads the console, as
     * that would lead to a deadlock.
     * 
     * @param command
     *            The command.
     * @return The output of the command.
     * @throws CommandException
     *             If the command cannot be passed to the bedrock server, for
     *             example because it is empty.
     */
    String runAndRecordCommand(String command) throws CommandException;

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
