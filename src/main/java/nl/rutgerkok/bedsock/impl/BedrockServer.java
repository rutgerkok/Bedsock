package nl.rutgerkok.bedsock.impl;

import java.io.OutputStream;
import java.util.Objects;

import nl.rutgerkok.bedsock.Logger;
import nl.rutgerkok.bedsock.ServerWrapper;
import nl.rutgerkok.bedsock.command.CommandRunner;
import nl.rutgerkok.bedsock.impl.plugin.PluginLoader;

final class BedrockServer implements ServerWrapper {

    final BedrockCommandRunner commandRunner;
    final Logger logger;
    final PluginLoader pluginLoader;

    BedrockServer(OutputStream serverStdIn, Logger logger) {
        this.commandRunner = new BedrockCommandRunner(serverStdIn);
        this.logger = Objects.requireNonNull(logger, "logger");

        this.pluginLoader = new PluginLoader();
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
