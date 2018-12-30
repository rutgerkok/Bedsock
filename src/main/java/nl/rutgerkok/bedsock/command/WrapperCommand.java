package nl.rutgerkok.bedsock.command;

import java.util.Objects;

import nl.rutgerkok.bedsock.logger.Logger;
import nl.rutgerkok.bedsock.plugin.ActivePlugin;

/**
 * Represents a command that is run on the wrapper.
 *
 */
public abstract class WrapperCommand {

    private final ActivePlugin plugin;

    protected WrapperCommand(ActivePlugin plugin) {
        this.plugin = Objects.requireNonNull(plugin, "plugin");
    }

    /**
     * Gets a description of the command, like "Teleports you to another player".
     *
     * @return A description.
     */
    public abstract String getDescription();

    /**
     * Gets access to the plugin logger.
     * @return The logger.
     */
    public final Logger getLogger() {
        return plugin.getLogger();
    }

    /**
     * Gets the plugin that provided this command.
     * @return The plugin.
     */
    public final ActivePlugin getPlugin() {
        return plugin;
    }

    /**
     * Gets a usage message for the command. For the command "/tpa &lt;player&gt;"
     * that message would be "&lt;player&gt;".
     *
     * @return A usage message.
     */
    public abstract String getUsage();

    /**
     * Runs the command.
     *
     * @param sender
     *            The sender of the command.
     * @param args
     *            The provided arguments.
     * @throws CommandException
     *             Thrown if command execution fails because the user did something
     *             incorrectly. The error message is shown to the user without the
     *             stack trace.
     */
    public abstract void run(CommandSender sender, CommandArgs args) throws CommandException;
}
