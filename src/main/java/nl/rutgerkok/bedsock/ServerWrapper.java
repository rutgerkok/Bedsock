package nl.rutgerkok.bedsock;

import nl.rutgerkok.bedsock.command.CommandRunner;

/**
 * The server wrapper.
 */
public interface ServerWrapper {

    /**
     * Gets the command runner of the server, which is used to perform commands.
     * 
     * @return The command runner.
     */
    CommandRunner getCommandRunner();

    /**
     * Gets the logger of the server.
     * 
     * @return The logger.
     */
    Logger getLogger();
}
