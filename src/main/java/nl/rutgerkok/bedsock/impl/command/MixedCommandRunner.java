package nl.rutgerkok.bedsock.impl.command;

import java.util.Objects;
import java.util.Optional;

import nl.rutgerkok.bedsock.command.CommandArgs;
import nl.rutgerkok.bedsock.command.CommandException;
import nl.rutgerkok.bedsock.command.CommandRegistry;
import nl.rutgerkok.bedsock.command.CommandRunner;
import nl.rutgerkok.bedsock.command.CommandSender;
import nl.rutgerkok.bedsock.command.WrapperCommand;

/**
 * Tries to run a command using the registered commands first, and forwards
 *
 */
public final class MixedCommandRunner implements CommandRunner {

    private final CommandRunner bedrockCommandRunner;
    private final CommandRegistry registry;

    public MixedCommandRunner(CommandRunner bedrockCommandRunner, CommandRegistry registry) {
        this.bedrockCommandRunner = Objects.requireNonNull(bedrockCommandRunner, "bedrockCommandRunner");
        this.registry = Objects.requireNonNull(registry, "registry");
    }

    @Override
    public String runAndRecordCommand(CommandArgs args) throws CommandException {
        args.expectMinSize(1);
        Optional<WrapperCommand> oCommand = registry.getConsoleCommand(args.getString(0));
        if (oCommand.isPresent()) {
            WrapperCommand command = oCommand.get();
            StringBuffer builder = new StringBuffer();
            CommandSender recorder = new CommandSender() {
                @Override
                public void sendMessage(String string) {
                    builder.append(string).append('\n');
                }
            };
            command.run(recorder, args.shift());
            return builder.toString();
        }

        // Fall back to Bedrock
        return bedrockCommandRunner.runAndRecordCommand(args);
    }

    @Override
    public void runCommand(CommandArgs args) throws CommandException {
        args.expectMinSize(1);
        Optional<WrapperCommand> oCommand = registry.getConsoleCommand(args.getString(0));
        if (oCommand.isPresent()) {
            WrapperCommand command = oCommand.get();
            command.run(command.getPlugin().getLogger()::info, args.shift());
            return;
        }

        // Fall back to Bedrock
        bedrockCommandRunner.runCommand(args);
    }

}
