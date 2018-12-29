package nl.rutgerkok.bedsock.impl;

import nl.rutgerkok.bedsock.ActiveServer;
import nl.rutgerkok.bedsock.Scheduler;
import nl.rutgerkok.bedsock.plugin.ActivePlugin;

/**
 * Hosts the main loop and the worked threads.
 *
 */
final class SchedulerImpl implements Scheduler {

    private final MainLoop mainLoop;

    SchedulerImpl() {
        this.mainLoop = new MainLoop();
    }

    private Runnable catchingErrors(ActivePlugin plugin, Runnable runnable) {
        return new Runnable() {

            @Override
            public void run() {
                try {
                    runnable.run();
                } catch (Throwable t) {
                    plugin.getLogger().error("Error in scheduled code", t);
                }
            }
        };

    }

    /**
     * Starts the main server loop. This method won't return until the loop is stopped.
     * @param server The server.
     */
    void mainloop(ActiveServer server) {
        this.mainLoop.loop(server);
    }

    void requestStop() {
        this.mainLoop.stopRequested = true;
    }

    @Override
    public void runOnMainThread(ActivePlugin plugin, Runnable runnable) {
        runOnMainThread0(catchingErrors(plugin, runnable));
    }

    /**
     * Does not do error catching - use
     * {@link #runOnMainThread(ActivePlugin, Runnable)} for plugin code.
     * 
     * @param runnable
     *            The code to run.
     */
    void runOnMainThread0(Runnable runnable) {
        this.mainLoop.interruptMe(runnable);
    }
}
