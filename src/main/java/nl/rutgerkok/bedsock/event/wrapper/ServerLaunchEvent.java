package nl.rutgerkok.bedsock.event.wrapper;

import nl.rutgerkok.bedsock.ActiveServer;

/**
 * Called just after the server process is started.
 *
 */
public final class ServerLaunchEvent extends WrapperEvent {


    public ServerLaunchEvent(ActiveServer server) {
        super(server);
    }

}
