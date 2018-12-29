package nl.rutgerkok.bedsock.impl;

import java.io.OutputStream;

import nl.rutgerkok.bedsock.ActiveServer;
import nl.rutgerkok.bedsock.InactiveServer;
import nl.rutgerkok.bedsock.Logger;
import nl.rutgerkok.bedsock.command.CommandRegistry;
import nl.rutgerkok.bedsock.command.CommandRunner;
import nl.rutgerkok.bedsock.event.EventRegistry;
import nl.rutgerkok.bedsock.impl.command.BedrockCommandRunner;
import nl.rutgerkok.bedsock.impl.command.MixedCommandRunner;

final class ActiveServerImpl implements ActiveServer {

    private final BedrockCommandRunner bedrockCommandRunner;
    private final Logger logger;
    private final CommandRunner mixedCommandRunner;
    private final CommandRegistry commandRegistry;
    private final EventRegistry eventRegistry;
    private final SchedulerImpl scheduler;

    ActiveServerImpl(OutputStream serverStdIn, InactiveServer inactiveServer) {
        this.bedrockCommandRunner = new BedrockCommandRunner(serverStdIn);
        this.logger = inactiveServer.getLogger();
        this.commandRegistry = inactiveServer.getCommandRegistry();
        this.eventRegistry = inactiveServer.getEventRegistry();
        this.mixedCommandRunner = new MixedCommandRunner(bedrockCommandRunner, commandRegistry);
        this.scheduler = new SchedulerImpl();
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
    public EventRegistry getEventRegistry() {
        return eventRegistry;
    }

    @Override
    public Logger getLogger() {
        return logger;
    }

    @Override
    public SchedulerImpl getScheduler() {
        return scheduler;
    }
}
