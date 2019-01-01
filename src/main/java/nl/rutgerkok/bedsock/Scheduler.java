package nl.rutgerkok.bedsock;

import java.util.concurrent.Executor;

import nl.rutgerkok.bedsock.plugin.ActivePlugin;
import nl.rutgerkok.bedsock.util.TimeSpan;

/**
 * The server scheduler. For now, it is only used to run code on the main
 * thread.
 *
 */
public interface Scheduler {

    /**
     * When you schedule a task, you get at ticket. If you later on decide to cancel
     * the task, you can use this ticket to do so.
     *
     */
    public interface Ticket {

        /**
         * Attempts to cancel the task. Does nothing if the task is already cancelled.
         */
        void cancel();
    }

    /**
     * An executor for running things on the main thread.
     *
     * @return The executor.
     */
    Executor mainThreadExecutor();

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
     * Runs the given code once on the main thread after a delay.
     *
     * @param plugin
     *            The plugin, used for blaming purposes if the code throws an
     *            exception.
     * @param runnable
     *            The code to run.
     * @param delay
     *            The delay.
     * @return A ticket, used to cancel the task.
     */
    default Ticket runOnMainThreadDelayed(ActivePlugin plugin, Runnable runnable, TimeSpan delay) {
        return runOnWorkerThreadDelayed(plugin, () -> {
            runOnMainThread(plugin, runnable);
        }, delay);
    }

    /**
     * Run the given code repeatingly on the main thread. The first run happens after the given time.
     *
     * @param plugin
     *            The plugin, used for blaming purposes if the code throws an
     *            exception.
     * @param runnable
     *            The code to run.
     * @param time
     *            The interval and the initial delay.
     * @return A ticket, used to cancel the task.
     */
    default Ticket runOnMainThreadRepeating(ActivePlugin plugin, Runnable runnable, TimeSpan time) {
        return runOnWorkerThreadRepeating(plugin, () -> {
            runOnMainThread(plugin, runnable);
        }, time);
    }

    /**
     * Runs the given code once on a worker thread after a delay.
     *
     * @param plugin
     *            The plugin, used for blaming purposes if the code throws an
     *            exception.
     * @param runnable
     *            The code to run.
     * @param delay
     *            The delay.
     * @return A ticket, used to cancel the task.
     */
    Ticket runOnWorkerThreadDelayed(ActivePlugin plugin, Runnable runnable, TimeSpan delay);

    /**
     * Run the given code repeatingly on a worker thread. The first run happens after the given time.
     *
     * @param plugin
     *            The plugin, used for blaming purposes if the code throws an
     *            exception.
     * @param runnable
     *            The code to run.
     * @param time
     *            The interval and the initial delay.
     * @return A ticket, used to cancel the task.
     */
    Ticket runOnWorkerThreadRepeating(ActivePlugin plugin, Runnable runnable, TimeSpan time);

    /**
     * An executor for running things on the main thread. Example usage for
     * calculating something, then using the result on the server thread:
     *
     * <pre>
     * {@code
     *      CompletableFuture.supplyAsync(() -> expensiveTask(numbers), scheduler.workerThreadExecutor())
     *          .handleAsync((result, error) -> {
     *              if (error != null) {
     *                  // There was an error
     *              } else {
     *                  // There was a result
     *              }
     *              return result;
     *          }, scheduler.mainThreadExecutor());
     * }
     * </pre>
     *
     * @return The executor.
     */
    Executor workerThreadExecutor();
}
