package nl.rutgerkok.bedsock.event.wrapper;

import nl.rutgerkok.bedsock.ActiveServer;

/**
 * Fired whenever a server wrapper is ticked. This usually happens every second.
 * Note that these ticks have no relation to the Bedrock server ticks, which
 * happen 20 times a second.
 *
 * <p>
 * Even if a tick of the wrapper server takes a long time (for example half a
 * second) the Bedrock server will happily continue ticking, and the players
 * won't notice any lag. However, any action on the Bedrock server that needs to
 * interact with the wrapper will of course be delayed until the wrapper is
 * responsive again. As a result, it is still a good idea to not block the main
 * wrapper thread for too long.
 */
public final class TickEvent extends WrapperEvent {

    public TickEvent(ActiveServer server) {
        super(server);
    }

}
