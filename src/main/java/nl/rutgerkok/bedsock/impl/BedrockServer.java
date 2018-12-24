package nl.rutgerkok.bedsock.impl;

import java.io.OutputStream;
import java.util.Objects;

import nl.rutgerkok.bedsock.Logger;
import nl.rutgerkok.bedsock.ServerWrapper;
import nl.rutgerkok.bedsock.command.CommandRunner;

final class BedrockServer implements ServerWrapper {

    final BedrockCommandRunner commandRunner;
    final Logger logger;

    BedrockServer(OutputStream serverStdIn, Logger logger) {
        this.commandRunner = new BedrockCommandRunner(serverStdIn);
        this.logger = Objects.requireNonNull(logger, "logger");
    }

    @Override
    public CommandRunner getCommandRunner() {
        return commandRunner;
    }

    @Override
    public Logger getLogger() {
        return logger;
    }
}
