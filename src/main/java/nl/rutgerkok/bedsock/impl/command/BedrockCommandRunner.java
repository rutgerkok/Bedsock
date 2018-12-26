package nl.rutgerkok.bedsock.impl.command;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.Objects;
import java.util.UUID;
import java.util.function.Consumer;

import nl.rutgerkok.bedsock.command.CommandException;
import nl.rutgerkok.bedsock.command.CommandRunner;
import nl.rutgerkok.bedsock.impl.OutputFilter;

/**
 * Thread-safe class that passes commands directly to Minecraft.
 *
 */
public final class BedrockCommandRunner implements CommandRunner {

    /**
     * Collects all output until a line contains the given string.
     *
     */
    private static class RecordingOutputFilter implements OutputFilter {

        private final StringBuffer buffer = new StringBuffer();
        private volatile boolean done = false;

        private String endMarker;

        public RecordingOutputFilter(String endMarker) {
            this.endMarker = endMarker;
        }

        @Override
        public boolean parse(String line) {
            if (line.contains(endMarker)) {
                done = true;
                return false;
            }
            buffer.append(line).append('\n');
            return true;
        }

    }

    private final static String NON_EXISTING_COMMAND = "non_existing/";
    private final Object lock = new Object();
    private final OutputStreamWriter serverStdIn;

    private Consumer<OutputFilter> outputFilterSetter;

    public BedrockCommandRunner(OutputStream serverStdIn) {
        this.serverStdIn = new OutputStreamWriter(serverStdIn);
    }

    @Override
    public String runAndRecordCommand(String command) throws CommandException {
        // Run a two commands - the requested command, an a non-existing command.
        // When we detect that the non-existing command is running, we know that the
        // output of the first command has ended

        try {
            synchronized (lock) {
                Consumer<OutputFilter> outputFilterSetter = this.outputFilterSetter;
                if (outputFilterSetter == null) {
                    throw new IllegalStateException("Output filter not yet initialized");
                }

                String fakeCommand = NON_EXISTING_COMMAND + UUID.randomUUID();
                RecordingOutputFilter filter = new RecordingOutputFilter(fakeCommand);
                this.outputFilterSetter.accept(filter);
                serverStdIn.write(command + System.lineSeparator() + fakeCommand + System.lineSeparator());
                serverStdIn.flush();
                while (!filter.done) {
                    try {
                        Thread.sleep(10);
                    } catch (InterruptedException e) {
                        // Ignore
                    }
                }
                return filter.buffer.toString();
            }
        } catch (IOException e) {
            throw new CommandException("IO Error: " + e.getMessage());
        }

    }

    @Override
    public void runCommand(String command) throws CommandException {
        if (command.startsWith("/")) {
            command = command.substring(1); // Strip the initial slash, console does not accept it
        }
        synchronized (lock) {
            try {
                serverStdIn.write(command + System.lineSeparator());
                serverStdIn.flush();
            } catch (IOException e) {
                throw new CommandException("IO Error: " + e.getMessage());
            }
        }
    }

    /**
     * Once we have connected to the server, the given object is used to
     * (temporarily) record any output from that server.
     *
     * @param consumer
     *            Connection to the Bedrock server output.
     */
    void setOutputFilterSeter(Consumer<OutputFilter> consumer) {
        this.outputFilterSetter = Objects.requireNonNull(consumer);
    }

}
