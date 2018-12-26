package nl.rutgerkok.bedsock.impl;

import java.io.OutputStream;

import nl.rutgerkok.bedsock.ActiveServer;
import nl.rutgerkok.bedsock.InactiveServer;
import nl.rutgerkok.bedsock.Logger;
import nl.rutgerkok.bedsock.command.CommandRegistry;
import nl.rutgerkok.bedsock.command.CommandRunner;
import nl.rutgerkok.bedsock.impl.command.BedrockCommandRunner;
import nl.rutgerkok.bedsock.impl.command.MixedCommandRunner;

final class ActiveServerImpl implements ActiveServer {

    private final BedrockCommandRunner bedrockCommandRunner;
    private final Logger logger;
    private final CommandRunner mixedCommandRunner;
    private final CommandRegistry commandRegistry;

    ActiveServerImpl(OutputStream serverStdIn, InactiveServer inactiveServer) {
        this.bedrockCommandRunner = new BedrockCommandRunner(serverStdIn);
        this.logger = inactiveServer.getLogger();
        this.commandRegistry = inactiveServer.getCommandRegistry();
        this.mixedCommandRunner = new MixedCommandRunner(bedrockCommandRunner, commandRegistry);
    }

    @Override
    public CommandRunner getBedrockCommandRunner() {
        return bedrockCommandRunner;
    }

    @Override
    public CommandRegistry getCommandRegistry() {
        return commandRegistry;
    }

    @Override
    public CommandRunner getCommandRunner() {
        return mixedCommandRunner;
    }

    @Override
    public Logger getLogger() {
        return logger;
    }
}
