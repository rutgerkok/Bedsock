package nl.rutgerkok.bedsock;

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


}
