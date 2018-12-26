package nl.rutgerkok.bedsock.impl.command;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import nl.rutgerkok.bedsock.command.CommandRegistry;
import nl.rutgerkok.bedsock.command.WrapperCommand;

public final class CommandRegistryImpl implements CommandRegistry {

    private Map<String, WrapperCommand> commands = new HashMap<>();

    @Override
    public Optional<WrapperCommand> getConsoleCommand(String name) {
        return Optional.ofNullable(commands.get(name));
    }

    @Override
    public List<WrapperCommand> getConsoleCommands() {
        return commands.values().stream().collect(Collectors.toList());
    }

    @Override
    public void register(String name, WrapperCommand command) {
        commands.put(name, command);
    }

}
