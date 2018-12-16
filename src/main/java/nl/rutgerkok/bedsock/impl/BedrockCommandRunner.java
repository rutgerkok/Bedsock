package nl.rutgerkok.bedsock.impl;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

import nl.rutgerkok.bedsock.command.CommandException;
import nl.rutgerkok.bedsock.command.CommandRunner;

/**
 * Thread-safe class that passes commands directly to Minecraft.
 *
 */
final class BedrockCommandRunner implements CommandRunner {

    private final Object lock = new Object();
    private final OutputStreamWriter serverStdIn;

    public BedrockCommandRunner(OutputStream serverStdIn) {
        this.serverStdIn = new OutputStreamWriter(serverStdIn);
    }

    @Override
    public void runCommand(String command) throws CommandException {
        if (command.startsWith("/")) {
            command = command.substring(1); // Strip the initial slash, console does not accept it
        }
        synchronized (lock) {
            try {
                serverStdIn.write(command + System.lineSeparator());
                serverStdIn.flush();
            } catch (IOException e) {
                throw new CommandException("IO Error: " + e.getMessage());
            }
        }
    }

}
