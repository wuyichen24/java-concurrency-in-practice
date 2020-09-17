package net.jcip.examples.ch07;

import java.util.concurrent.*;

/**
 * TimedRun1
 *
 * @list 7.8
 * @smell Bad
 * @author Brian Goetz and Tim Peierls
 * 
 * <p>The bad example of an attempt at running an arbitrary Runnable for a given amount of time.
 * 
 * <p>Problem: Since timedRun can be called from an arbitrary thread, it cannot know the calling thread's interruption policy. 
 * If the task completes before the timeout, the cancellation task that interrupts the thread in which timedRun was called could 
 * go off after timedRun has returned to its caller. We don't know what code will be running when that happens.
 */
public class TimedRun1 {
    private static final ScheduledExecutorService cancelExec = Executors.newScheduledThreadPool(1);

    public static void timedRun(Runnable r, long timeout, TimeUnit unit) {
        final Thread taskThread = Thread.currentThread();
        cancelExec.schedule(new Runnable() {
            public void run() {
                taskThread.interrupt();
            }
        }, timeout, unit);
        r.run();
    }
}
