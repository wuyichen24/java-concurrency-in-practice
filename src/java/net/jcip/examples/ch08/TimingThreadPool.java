package net.jcip.examples.ch08;

import java.util.concurrent.*;
import java.util.concurrent.atomic.*;
import java.util.logging.*;

/**
 * TimingThreadPool
 *
 * @list 8.9
 * @smell Good
 * @author Brian Goetz and Tim Peierls
 * 
 * <p>Example of extending thread pool executor.
 */
public class TimingThreadPool extends ThreadPoolExecutor {

    public TimingThreadPool() {
        super(1, 1, 0L, TimeUnit.SECONDS, null);
    }

    private final ThreadLocal<Long> startTime = new ThreadLocal<Long>();       // ThreadLocal guarantees each thread can have its own start time. 
    private final Logger log = Logger.getLogger("TimingThreadPool");
    private final AtomicLong numTasks = new AtomicLong();                      // Keep track of the total number of tasks processed.
    private final AtomicLong totalTime = new AtomicLong();                     // Keep track of the total processing time.

    protected void beforeExecute(Thread t, Runnable r) {                       // beforeExecute will be triggered before the task is executing.
        super.beforeExecute(t, r);
        log.fine(String.format("Thread %s: start %s", t, r));
        startTime.set(System.nanoTime());                                      // Record the start time of the execution.
    }

    protected void afterExecute(Runnable r, Throwable t) {                     // afterExecute will be triggered whether the task completes by returning normally from run or by throwing an Exception.
        try {
            long endTime = System.nanoTime();
            long taskTime = endTime - startTime.get();                         // Record the end time of the execution and calculate the time for the execution.
            numTasks.incrementAndGet();
            totalTime.addAndGet(taskTime);
            log.fine(String.format("Thread %s: end %s, time=%dns",
                    t, r, taskTime));
        } finally {
            super.afterExecute(r, t);
        }
    }

    protected void terminated() {
        try {
            log.info(String.format("Terminated: avg time=%dns",                // When the thread executor is shutdown, calculate the average time of executing a task.
                    totalTime.get() / numTasks.get()));
        } finally {
            super.terminated();
        }
    }
}
