package nl.rutgerkok.bedsock.impl;

import java.util.Objects;
import java.util.concurrent.Executor;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;

import org.eclipse.jdt.annotation.Nullable;

import nl.rutgerkok.bedsock.Scheduler;
import nl.rutgerkok.bedsock.plugin.ActivePlugin;
import nl.rutgerkok.bedsock.util.TimeSpan;

/**
 * Hosts the main loop and the worked threads.
 *
 */
final class SchedulerImpl implements Scheduler {

    private final MainLoop mainLoop;
    private final ScheduledThreadPoolExecutor worker;

    SchedulerImpl() {
        mainLoop = new MainLoop();
        worker = new ScheduledThreadPoolExecutor(0);
        worker.setExecuteExistingDelayedTasksAfterShutdownPolicy(false);
        worker.setContinueExistingPeriodicTasksAfterShutdownPolicy(false);
        worker.setRemoveOnCancelPolicy(true);
    }

    private Runnable catchingErrors(ActivePlugin plugin, @Nullable Runnable runnable) {
        if (runnable == null) {
            throw new NullPointerException("runnable");
        }
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
     * Starts the main server loop. This method won't return until the loop is
     * stopped.
     *
     * @param server
     *            The server.
     */
    void mainloop(ActiveServerImpl server) {
        this.mainLoop.loop(server);
    }

    @Override
    public Executor mainThreadExecutor() {
        return new Executor() {

            @Override
            public void execute(@Nullable Runnable runnable) {
                runOnMainThread0(Objects.requireNonNull(runnable, "runnable"));
            }

        };
    }

    void requestStop() {
        this.mainLoop.stopRequested = true;
        this.worker.shutdown();
    }

    @Override
    public void runOnMainThread(ActivePlugin plugin, @Nullable Runnable runnable) {
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

    @Override
    public Ticket runOnWorkerThreadDelayed(ActivePlugin plugin, Runnable runnable, TimeSpan delay) {
        ScheduledFuture<?> future = worker.schedule(catchingErrors(plugin, runnable), delay.getValue(),
                delay.getUnit());
        return () -> future.cancel(false);
    }

    @Override
    public Ticket runOnWorkerThreadRepeating(ActivePlugin plugin, Runnable runnable, TimeSpan time) {
        ScheduledFuture<?> future = worker.scheduleAtFixedRate(catchingErrors(plugin, runnable), time.getValue(),
                time.getValue(), time.getUnit());
        return () -> future.cancel(false);
    }

    @Override
    public Executor workerThreadExecutor() {
        return new Executor() {

            @Override
            public void execute(@Nullable Runnable command) {
                worker.execute(command);
            }

        };
    }
}
