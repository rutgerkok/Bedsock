package nl.rutgerkok.bedsock;

import nl.rutgerkok.bedsock.plugin.ActivePlugin;

/**
 * The server scheduler. For now, it is only used to run code on the main
 * thread.
 *
 */
public interface Scheduler {

    /**
     * Runs the given code on the main server thread. The code will be run in
     * between two server ticks, at the latest just before the next server tick
     * event is called. This also holds if you call this method from the main
     * thread.
     *
     * @param plugin
     *            The plugin, used for blaming purposes if the code throws an
     *            exception.
     * @param runnable
     *            The code to run.
     */
    void runOnMainThread(ActivePlugin plugin, Runnable runnable);
}
