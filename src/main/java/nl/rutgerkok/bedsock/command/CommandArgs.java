package nl.rutgerkok.bedsock.command;

import nl.rutgerkok.bedsock.ActiveServer;

public interface CommandArgs {

    /**
     * Throws an exception if too many arguments are provided.
     *
     * @param size
     *            Maximal number of arguments.
     * @throws CommandException
     *             If too many arguments are provided.
     */
    void expectMaxSize(int size) throws CommandException;

    /**
     * Throws an exception if too few arguments are provided.
     *
     * @param size
     *            Minimal number of arguments.
     * @throws CommandException
     *             If too few arguments are provided.
     */
    void expectMinSize(int size) throws CommandException;

    /**
     * Throws an exception if an unexpected number of arguments is provided.
     *
     * @param size
     *            Exact number of arguments.
     * @throws CommandException
     *             If too many or too few arguments are provided.
     */
    void expectSize(int size) throws CommandException;

    /**
     * Gets the int at the given position.
     *
     * @param pos
     *            The position.
     * @return The string.
     * @throws CommandException
     *             If too few arguments were provided, or if the argument is not an
     *             int.
     * @throws ArrayIndexOutOfBoundsException
     *             If pos < 0.
     */
    int getInt(int pos) throws CommandException;

    /**
     * Gets a joined string. So for "foo bar baz", getJoinedStrings(1) returns "bar
     * baz".
     *
     * @param pos
     *            The position, may not be larger than size().
     * @return The joined string.
     */
    String getJoinedStrings(int pos);

    /**
     * Gets the server the command is running on.
     *
     * @return The server.
     */
    ActiveServer getServer();

    /**
     * Gets the string at the given position.
     *
     * @param pos
     *            The position.
     * @return The string.
     * @throws CommandException
     *             If too few arguments were provided.
     * @throws ArrayIndexOutOfBoundsException
     *             If pos < 0.
     */
    String getString(int pos) throws CommandException;

    /**
     * Shifts the first argument off the stack.
     *
     * @return A copy without the first argument.
     */
    CommandArgs shift();

    /**
     * Gets the number of arguments provided.
     *
     * @return The number.
     */
    int size();

    /**
     * Returns all arguments connected by spaces.
     * @return All arguments connected by spaces.
     */
    @Override
    String toString();
}
