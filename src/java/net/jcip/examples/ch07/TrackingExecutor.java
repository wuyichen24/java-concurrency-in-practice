package net.jcip.examples.ch07;

import java.util.*;
import java.util.concurrent.*;

/**
 * TrackingExecutor
 * 
 * @list 7.21
 * @smell Good
 * @author Brian Goetz and Tim Peierls
 * 
 * <p>A solution for determining which tasks were in progress at shutdown time.
 */
public class TrackingExecutor extends AbstractExecutorService {                // Extend the AbstractExecutorService
    private final ExecutorService exec;
    private final Set<Runnable> tasksCancelledAtShutdown =
            Collections.synchronizedSet(new HashSet<Runnable>());              

    public TrackingExecutor(ExecutorService exec) {
        this.exec = exec;
    }

    public void shutdown() {
        exec.shutdown();
    }

    public List<Runnable> shutdownNow() {
        return exec.shutdownNow();
    }

    public boolean isShutdown() {
        return exec.isShutdown();
    }

    public boolean isTerminated() {
        return exec.isTerminated();
    }

    public boolean awaitTermination(long timeout, TimeUnit unit)
            throws InterruptedException {
        return exec.awaitTermination(timeout, unit);
    }

    public List<Runnable> getCancelledTasks() {                                // After the executor terminates, getCancelledTasks returns the list of cancelled tasks.
        if (!exec.isTerminated())
            throw new IllegalStateException(/*...*/);
        return new ArrayList<Runnable>(tasksCancelledAtShutdown);
    }

    public void execute(final Runnable runnable) {
        exec.execute(new Runnable() {
            public void run() {
                try {
                    runnable.run();
                } finally {
                    if (isShutdown()
                            && Thread.currentThread().isInterrupted())
                        tasksCancelledAtShutdown.add(runnable);                // When shutdown, save the in-progress tasks in the set
                }
            }
        });
    }
}
