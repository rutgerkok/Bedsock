package nl.rutgerkok.bedsock.impl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Objects;

import nl.rutgerkok.bedsock.ServerWrapper;
import nl.rutgerkok.bedsock.command.CommandException;

/**
 * This reads the commands the admin is typing.
 *
 */
public class ConsoleReadThread extends Thread {

    private final BufferedReader console;
    private final ServerWrapper server;

    public ConsoleReadThread(InputStream console, ServerWrapper server) {
        super.setName("Console reader");
        this.console = new BufferedReader(new InputStreamReader(console));
        this.server = Objects.requireNonNull(server, "server");
    }

    @Override
    public void run() {
        while (true) {
            try {
                String command = console.readLine();
                if (command != null && !command.isEmpty()) {
                    server.getCommandRunner().runCommand(command);
                }
            } catch (CommandException e) {
                server.getLogger().error("Error running command: " + e.getMessage());
            } catch (IOException e) {
                server.getLogger().error("Error reading command", e);
            }
        }
    }
}
