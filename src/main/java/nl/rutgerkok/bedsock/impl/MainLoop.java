package nl.rutgerkok.bedsock.impl;

import java.util.Objects;
import java.util.concurrent.ConcurrentLinkedQueue;

import nl.rutgerkok.bedsock.ActiveServer;
import nl.rutgerkok.bedsock.event.wrapper.TickEvent;

final class MainLoop {
    private final ConcurrentLinkedQueue<Runnable> tasks = new ConcurrentLinkedQueue<>();
    private final Thread mainLoopThread;

    /**
     * Only modified from the main loop thread.
     */
    private long nextTickTime;

    volatile boolean stopRequested = false;

    MainLoop() {
        this.nextTickTime = System.currentTimeMillis() + 1000L;
        this.mainLoopThread = Thread.currentThread();
    }

    /**
     * Interrupts the main loop to run the given task.
     * 
     * @param task
     *            The task.
     */
    public void interruptMe(Runnable task) {
        Objects.requireNonNull(task, "task");
        tasks.add(task);
        mainLoopThread.interrupt();
    }

    void loop(ActiveServer server) {
        TickEvent tickEvent = new TickEvent(server);
        while (!stopRequested) {
            runTasks();
            server.getEventRegistry().callEvent(tickEvent);
            nextTickTime += 1000L; // Run new tick in the next second
            sleepUntilNextTickTime();
        }
    }

    private void runTasks() {
        Runnable task;
        while ((task = tasks.poll()) != null) {
            task.run();
        }
    }

    private void sleepUntilNextTickTime() {
        long currentTime = System.currentTimeMillis();
        long timeRemaining = nextTickTime - currentTime;
        if (timeRemaining < 0) {
            // Lag, or interrupted just before end of sleeping second
            // Just run the next tick immediately
            nextTickTime = currentTime;
            return;
        } else if (timeRemaining > 1100L) {
            // System time changed?
            timeRemaining = 1000L;
            nextTickTime = currentTime + timeRemaining;
        }
        
        try {
            Thread.sleep(timeRemaining);
        } catch (InterruptedException e) {
            // Interrupted, apparently someone wanted our attention
            if (stopRequested) {
                return; // Die
            }
            runTasks();

            // Go back to sleep until nextTickTime
            sleepUntilNextTickTime();
        }
    }
}
