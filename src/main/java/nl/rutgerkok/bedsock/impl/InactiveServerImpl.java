package nl.rutgerkok.bedsock.impl;

import nl.rutgerkok.bedsock.InactiveServer;
import nl.rutgerkok.bedsock.command.CommandRegistry;
import nl.rutgerkok.bedsock.event.EventRegistry;
import nl.rutgerkok.bedsock.impl.command.CommandRegistryImpl;
import nl.rutgerkok.bedsock.impl.event.EventRegistryImpl;
import nl.rutgerkok.bedsock.impl.plugin.PluginLoader;
import nl.rutgerkok.bedsock.logger.ForwardingLogger;

final class InactiveServerImpl implements InactiveServer {

    private final CommandRegistry commandRegistry;
    private final EventRegistry eventRegistry;
    private final ForwardingLogger logger;
    final PluginLoader pluginLoader;

    InactiveServerImpl() {
        this.commandRegistry = new CommandRegistryImpl();
        this.eventRegistry = new EventRegistryImpl();
        this.logger = new ForwardingLogger(new PrintlnLogger());
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
    public ForwardingLogger getServerLogger() {
        return logger;
    }

}
