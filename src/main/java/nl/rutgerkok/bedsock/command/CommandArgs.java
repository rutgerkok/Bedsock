package nl.rutgerkok.bedsock.command;

import java.util.Arrays;
import java.util.Objects;

import org.eclipse.jdt.annotation.Nullable;

public class CommandArgs {

    private final String[] args;

    public CommandArgs(String toParse) {
        toParse = toParse.trim();
        if (toParse.isEmpty()) {
            args = new String[0];
        } else {
            args = toParse.split(" +"); // Would be nice if you could quote arguments
        }
    }

    private CommandArgs(String[] args) {
        this.args = Objects.requireNonNull(args);
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        CommandArgs other = (CommandArgs) obj;
        if (!Arrays.equals(args, other.args)) {
            return false;
        }
        return true;
    }

    /**
     * Throws an exception if too many arguments are provided.
     *
     * @param size
     *            Maximal number of arguments.
     * @throws CommandException
     *             If too many arguments are provided.
     */
    public void expectMaxSize(int size) throws CommandException {
        if (this.args.length > size) {
            throw new CommandException(
                    "Expected at most " + size + " arguments, but only " + args.length + " were provided");
        }
    }

    /**
     * Throws an exception if too few arguments are provided.
     *
     * @param size
     *            Minimal number of arguments.
     * @throws CommandException
     *             If too few arguments are provided.
     */
    public void expectMinSize(int size) throws CommandException {
        if (this.args.length < size) {
            throw new CommandException(
                    "Expected at least " + size + " arguments, but only " + args.length + " were provided");
        }
    }

    /**
     * Throws an exception if an unexpected number of arguments is provided.
     *
     * @param size
     *            Exact number of arguments.
     * @throws CommandException
     *             If too many or too few arguments are provided.
     */
    public void expectSize(int size) throws CommandException {
        if (this.args.length != size) {
            throw new CommandException(
                    "Expected exactly " + size + " arguments, but " + args.length + " were provided");
        }
    }

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
    public int getInt(int pos) throws CommandException {
        try {
            return Integer.parseInt(getString(pos));
        } catch (NumberFormatException e) {
            throw new CommandException("Invalid number for argument " + (pos + 1) + ": " + args[pos]);
        }
    }

    /**
     * Gets a joined string. So for "foo bar baz", getJoinedStrings(1) returns "bar
     * baz".
     *
     * @param pos
     *            The position, may not be larger than size().
     * @return The joined string.
     */
    public String getJoinedStrings(int pos) {
        if (pos == args.length) {
            return "";
        }
        if (pos == 0) {
            return String.join(" ", args);
        }

        StringBuffer buffer = new StringBuffer();
        for (int i = pos; i < args.length; i++) {
            buffer.append(args[i]);
            if (i != args.length - 1) {
                buffer.append(' ');
            }
        }
        return buffer.toString();
    }

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
    public String getString(int pos) throws CommandException {
        if (pos >= args.length) {
            throw new CommandException(
                    "Expected at least " + (pos + 1) + " arguments, but only " + args.length + " were provided.");
        }
        return args[pos];
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + Arrays.hashCode(args);
        return result;
    }

    /**
     * Shifts the first argument off the stack.
     * @return A copy without the first argument.
     */
    public CommandArgs shift() {
        if (args.length <= 1) {
            return new CommandArgs(new String[0]);
        }
        String[] remainingArgs = new String[args.length - 1];
        System.arraycopy(args, 1, remainingArgs, 0, remainingArgs.length);
        return new CommandArgs(args);
    }

    /**
     * Gets the number of arguments provided.
     *
     * @return The number.
     */
    public int size() {
        return args.length;
    }
}
