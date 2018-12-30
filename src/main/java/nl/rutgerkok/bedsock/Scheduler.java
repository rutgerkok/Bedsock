package nl.rutgerkok.bedsock;

import java.util.concurrent.Executor;

import nl.rutgerkok.bedsock.plugin.ActivePlugin;

/**
 * The server scheduler. For now, it is only used to run code on the main
 * thread.
 *
 */
public interface Scheduler {

    /**
     * An executor for running things on the main thread.
     *
     * @param plugin
     *            The plugin, used for blaming purposes if you throw an exception.
     * @return The executor.
     */
    Executor mainThreadExecutor(ActivePlugin plugin);

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

    /**
     * An executor for running things on the main thread. Example usage:
     *
     * <pre>
     * {@code
     *      CompletableFuture.runAsync(() -> expensiveTask(numbers), scheduler.workerThreadExecutor(getPlugin()))
     *          .thenAccept(v -> getLogger().info("Task complete!"));
     * }
     * </pre>
     *
     * @param plugin
     *            The plugin, used for blaming purposes if you throw an exception.
     * @return The executor.
     */
    Executor workerThreadExecutor(ActivePlugin plugin);
}
