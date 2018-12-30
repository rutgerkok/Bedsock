package nl.rutgerkok.bedsock;

import nl.rutgerkok.bedsock.command.CommandArgs;
import nl.rutgerkok.bedsock.command.CommandRunner;

/**
 * The server wrapper, used when the Bedrock wrapper has started.
 */
public interface ActiveServer extends Server {

    /**
     * Gets a command runner that directly forwards the command to the server, without running wrapper commands.
     * @return The command runner.
     */
    CommandRunner getBedrockCommandRunner();

    /**
     * Gets the command runner of the server, which is used to perform commands.
     *
     * @return The command runner.
     */
    CommandRunner getCommandRunner();

    /**
     * Gets the server scheduler.
     *
     * @return The scheduler.
     */
    Scheduler getScheduler();

    /**
     * Parses the given string as a command string.
     *
     * @param string
     *            The command string, for example "/time set 0" or "weather clear".
     *            If the string starts with a slash, then that slash is
     *            automatically removed.
     * @return The parsed command string.
     */
    CommandArgs parseCommand(String string);
}
