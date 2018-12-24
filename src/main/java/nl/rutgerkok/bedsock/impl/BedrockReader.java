package nl.rutgerkok.bedsock.impl;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.IOException;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import nl.rutgerkok.bedsock.SockLogger;
import nl.rutgerkok.bedsock.SockLogger.LogLevel;

final class BedrockReader implements Closeable {

    private static final String NO_LOG_FILE = "NO LOG FILE! - ";
    private static final Pattern DATE_TIME_LEVEL = Pattern.compile("^\\[[\\d- :]+(?<level>[A-Z]+)\\] (?<message>.*)");

    private final BufferedReader stream;
    private final SockLogger logger;
    private volatile OutputFilter filter;

    public BedrockReader(BufferedReader stream, SockLogger logger) {
        this.stream = Objects.requireNonNull(stream);
        this.logger = Objects.requireNonNull(logger);
    }

    @Override
    public void close() throws IOException {
        stream.close();
    }

    public void run() throws IOException {
        while (true) {
            String line = stream.readLine();
            if (line == null) {
                return; // End of stream
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
                logger.log(level, message);
            } else if (!filter.parse(message)) {
                this.filter = null;
            }
        }
    }

    public void setFilter(OutputFilter filter) throws IllegalStateException {
        if (this.filter != null) {
            throw new IllegalStateException("Setting a filter while another one is in use");
        }
        this.filter = Objects.requireNonNull(filter, "filter");
    }
}
