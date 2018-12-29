package nl.rutgerkok.bedsock.logger;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * A logger that just forwards all messages to other loggers.
 *
 */
public final class ForwardingLogger implements Logger {

    private final CopyOnWriteArrayList<Logger> list = new CopyOnWriteArrayList<>();

    public ForwardingLogger(Logger... loggers) {
        for (Logger logger : loggers) {
            Objects.requireNonNull(logger, "a logger in loggers");
        }
        list.addAll(Arrays.asList(loggers));
    }

    @Override
    public void chat(String name, String message) {
        for (Logger logger : list) {
            logger.chat(name, message);
        }
    }

    @Override
    public void error(String message, Throwable e) {
        for (Logger logger : list) {
            logger.error(message, e);
        }
    }

    /**
     * Gets a mutable, thread-safe list of all loggers that the messages are
     * forwarded to. You can add your own loggers here. Your logger implementation
     * should implement {@link ConsoleLogger} and/or {@link PersistentLogger} if
     * appropriate.
     *
     * <p>
     * Adding null to this list is not allowed: this will cause errors the next time
     * a message is logged.
     *
     * @return The list.
     */
    public List<Logger> getListeners() {
        return list;
    }

    @Override
    public void log(LogLevel level, String message) {
        for (Logger logger : list) {
            logger.log(level, message);
        }
    }
}
