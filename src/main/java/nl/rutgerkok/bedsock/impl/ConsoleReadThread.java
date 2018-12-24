package nl.rutgerkok.bedsock.impl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Objects;

import nl.rutgerkok.bedsock.SockLogger;
import nl.rutgerkok.bedsock.command.CommandException;
import nl.rutgerkok.bedsock.command.CommandRunner;

/**
 * This reads the commands the admin is typing.
 *
 */
public class ConsoleReadThread extends Thread {

    private final BufferedReader console;
    private final CommandRunner commandRunner;
    private final SockLogger logger;

    public ConsoleReadThread(InputStream console, CommandRunner commandRunner, SockLogger logger) {
        this.console = new BufferedReader(new InputStreamReader(console));
        this.commandRunner = Objects.requireNonNull(commandRunner, "commandRunner");
        this.logger = Objects.requireNonNull(logger, "logger");
    }

    @Override
    public void run() {
        while (true) {
            try {
                String command = console.readLine();
                if (command != null && !command.isEmpty()) {
                    commandRunner.runCommand(command);
                }
            } catch (CommandException e) {
                logger.error("Error running command: " + e.getMessage());
            } catch (IOException e) {
                logger.error("Error reading command", e);
            }
        }
    }
}
