package nl.rutgerkok.bedsock.command;

import java.util.List;
import java.util.Optional;

/**
 * Used to register commands.
 *
 */
public interface CommandRegistry {

    /**
     * Gets the console command with the given name.
     * 
     * @param name
     *            The command name.
     * @return The command.
     */
    Optional<WrapperCommand> getConsoleCommand(String name);

    /**
     * Gets all registered console commands.
     * @return The commands.
     */
    List<WrapperCommand> getConsoleCommands();

    /**
     * Registers a new console command.
     * @param name
     *            Name of the command. May only contain lowercase letters and the
     *            dash (-).
     * @param command
     *            The command code.
     */
    void register(String name, WrapperCommand command);
}
