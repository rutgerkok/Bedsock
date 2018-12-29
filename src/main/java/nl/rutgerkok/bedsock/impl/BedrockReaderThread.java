package nl.rutgerkok.bedsock.impl;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.jdt.annotation.Nullable;

import nl.rutgerkok.bedsock.logger.Logger.LogLevel;

/**
 * This reads output from Minecraft.
 *
 */
final class BedrockReaderThread extends Thread {

    private static final String NO_LOG_FILE = "NO LOG FILE! - ";
    private static final Pattern DATE_TIME_LEVEL = Pattern.compile("^\\[[\\d- :]+(?<level>[A-Z]+)\\] (?<message>.*)");

    private final BufferedReader stream;
    private final ActiveServerImpl server;
    private @Nullable volatile OutputFilter filter;

    public BedrockReaderThread(BufferedReader stream, ActiveServerImpl server) {
        this.stream = Objects.requireNonNull(stream, "stream");
        this.server = Objects.requireNonNull(server, "server");
    }

    @Override
    public void run() {
        try {
            while (true) {
                String line = stream.readLine();
                if (line == null) {
                    break; // End of stream
                }
                if (line.startsWith(NO_LOG_FILE)) {
                    line = line.substring(NO_LOG_FILE.length());
                }

                LogLevel level = LogLevel.INFO;
                String message = line;

                Matcher matcher = DATE_TIME_LEVEL.matcher(line);
                if (matcher.matches()) {
                    try {
                        level = LogLevel.valueOf(matcher.group("level"));
                        message = matcher.group("message");
                    } catch (IllegalArgumentException e) {
                        // Invalid level - don't parse original message
                    }
                }

                OutputFilter filter = this.filter;
                if (filter == null) {
                    server.getServerLogger().log(level, message);
                } else if (!filter.parse(message)) {
                    this.filter = null;
                }
            }
            stream.close();
        } catch (IOException e) {
            server.getServerLogger().error("Error reading server output", e);
        }

        // Stop the wrapper program now that the Bedrock server has stopped
        server.getScheduler().requestStop();
    }

    void setFilter(OutputFilter filter) throws IllegalStateException {
        if (this.filter != null) {
            throw new IllegalStateException("Setting a filter while another one is in use");
        }
        this.filter = Objects.requireNonNull(filter, "filter");
    }
}
