package nl.rutgerkok.bedsock.impl;

import nl.rutgerkok.bedsock.InactiveServer;
import nl.rutgerkok.bedsock.Logger;
import nl.rutgerkok.bedsock.command.CommandRegistry;
import nl.rutgerkok.bedsock.event.EventRegistry;
import nl.rutgerkok.bedsock.impl.command.CommandRegistryImpl;
import nl.rutgerkok.bedsock.impl.event.EventRegistryImpl;
import nl.rutgerkok.bedsock.impl.plugin.PluginLoader;

final class InactiveServerImpl implements InactiveServer {

    private final CommandRegistry commandRegistry;
    private final EventRegistry eventRegistry;
    private final Logger logger;
    final PluginLoader pluginLoader;

    InactiveServerImpl() {
        this.commandRegistry = new CommandRegistryImpl();
        this.eventRegistry = new EventRegistryImpl();
        this.logger = new PrintlnLogger();
        this.pluginLoader = new PluginLoader();
    }

    @Override
    public CommandRegistry getCommandRegistry() {
        return commandRegistry;
    }

    @Override
    public EventRegistry getEventRegistry() {
        return eventRegistry;
    }

    @Override
    public Logger getLogger() {
        return logger;
    }

}
