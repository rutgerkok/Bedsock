package nl.rutgerkok.bedsock.impl;

import java.io.OutputStream;

import nl.rutgerkok.bedsock.ActiveServer;
import nl.rutgerkok.bedsock.ServerFolders;
import nl.rutgerkok.bedsock.command.CommandArgs;
import nl.rutgerkok.bedsock.command.CommandRegistry;
import nl.rutgerkok.bedsock.command.CommandRunner;
import nl.rutgerkok.bedsock.event.EventRegistry;
import nl.rutgerkok.bedsock.impl.command.BedrockCommandRunner;
import nl.rutgerkok.bedsock.impl.command.CommandArgsImpl;
import nl.rutgerkok.bedsock.impl.command.MixedCommandRunner;
import nl.rutgerkok.bedsock.impl.plugin.PluginRegistryImpl;
import nl.rutgerkok.bedsock.logger.ForwardingLogger;

final class ActiveServerImpl implements ActiveServer {

    private final BedrockCommandRunner bedrockCommandRunner;
    private final ForwardingLogger logger;
    private final CommandRunner mixedCommandRunner;
    private final CommandRegistry commandRegistry;
    private final EventRegistry eventRegistry;
    private final SchedulerImpl scheduler;
    private final PluginRegistryImpl pluginRegistry;
    private final ServerFolders folders;

    ActiveServerImpl(OutputStream serverStdIn, InactiveServerImpl inactiveServer) {
        this.bedrockCommandRunner = new BedrockCommandRunner(serverStdIn);
        this.logger = inactiveServer.getServerLogger();
        this.commandRegistry = inactiveServer.getCommandRegistry();
        this.eventRegistry = inactiveServer.getEventRegistry();
        this.mixedCommandRunner = new MixedCommandRunner(bedrockCommandRunner, commandRegistry);
        this.scheduler = new SchedulerImpl();
        this.pluginRegistry = inactiveServer.getPluginRegistry();
        this.folders = inactiveServer.getFolders();
    }

    @Override
    public BedrockCommandRunner getBedrockCommandRunner() {
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
    public ServerFolders getFolders() {
        return folders;
    }

    @Override
    public PluginRegistryImpl getPluginRegistry() {
        return pluginRegistry;
    }

    @Override
    public SchedulerImpl getScheduler() {
        return scheduler;
    }

    @Override
    public ForwardingLogger getServerLogger() {
        return logger;
    }

    @Override
    public CommandArgs parseCommand(String string) {
        return CommandArgsImpl.parse(this, string);
    }
}
