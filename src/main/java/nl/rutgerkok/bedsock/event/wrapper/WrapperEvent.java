package nl.rutgerkok.bedsock.event.wrapper;

import java.util.Objects;

import nl.rutgerkok.bedsock.ActiveServer;
import nl.rutgerkok.bedsock.event.Event;

/**
 * Some abstract event that holds a reference to the server wrapper.
 *
 */
public abstract class WrapperEvent extends Event {
    private final ActiveServer server;

    public WrapperEvent(ActiveServer server) {
        this.server = Objects.requireNonNull(server, "server");
    }

    /**
     * Gets the server that is being ticked.
     *
     * @return The server.
     */
    public ActiveServer getServer() {
        return server;
    }
}
