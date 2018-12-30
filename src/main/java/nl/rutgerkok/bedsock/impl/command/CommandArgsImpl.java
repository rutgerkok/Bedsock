package nl.rutgerkok.bedsock.impl.command;

import java.util.Arrays;
import java.util.Objects;

import org.eclipse.jdt.annotation.Nullable;

import nl.rutgerkok.bedsock.ActiveServer;
import nl.rutgerkok.bedsock.command.CommandArgs;
import nl.rutgerkok.bedsock.command.CommandException;

public class CommandArgsImpl implements CommandArgs {

    public static CommandArgs parse(ActiveServer server, String string) {
        return new CommandArgsImpl(server, string);
    }

    private final String[] args;
    private final ActiveServer server;

    private CommandArgsImpl(ActiveServer server, String toParse) {
        this.server = Objects.requireNonNull(server, "server");

        toParse = toParse.trim();
        if (toParse.startsWith("/")) {
            toParse = toParse.substring(1); // Strip the initial slash, console does not accept it
        }
        if (toParse.isEmpty()) {
            args = new String[0];
        } else {
            args = toParse.split(" +"); // Would be nice if you could quote arguments
        }
    }

    private CommandArgsImpl(ActiveServer server, String[] args) {
        this.server = Objects.requireNonNull(server, "server");
        this.args = Objects.requireNonNull(args, "args");
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
        CommandArgsImpl other = (CommandArgsImpl) obj;
        if (!Arrays.equals(args, other.args)) {
            return false;
        }
        return true;
    }

    @Override
    public void expectMaxSize(int size) throws CommandException {
        if (this.args.length > size) {
            throw new CommandException(
                    "Expected at most " + size + " arguments, but only " + args.length + " were provided");
        }
    }

    @Override
    public void expectMinSize(int size) throws CommandException {
        if (this.args.length < size) {
            throw new CommandException(
                    "Expected at least " + size + " arguments, but only " + args.length + " were provided");
        }
    }

    @Override
    public void expectSize(int size) throws CommandException {
        if (this.args.length != size) {
            throw new CommandException(
                    "Expected exactly " + size + " arguments, but " + args.length + " were provided");
        }
    }

    @Override
    public int getInt(int pos) throws CommandException {
        try {
            return Integer.parseInt(getString(pos));
        } catch (NumberFormatException e) {
            throw new CommandException("Invalid number for argument " + (pos + 1) + ": " + args[pos]);
        }
    }

    @Override
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

    @Override
    public ActiveServer getServer() {
        return server;
    }

    @Override
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

    @Override
    public CommandArgs shift() {
        if (args.length <= 1) {
            return new CommandArgsImpl(server, new String[0]);
        }
        String[] remainingArgs = new String[args.length - 1];
        System.arraycopy(args, 1, remainingArgs, 0, remainingArgs.length);
        return new CommandArgsImpl(server, args);
    }

    @Override
    public int size() {
        return args.length;
    }

    @Override
    public String toString() {
        return this.getJoinedStrings(0);
    }
}
