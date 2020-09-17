package net.jcip.examples.ch07;

import java.util.concurrent.*;
import static java.util.concurrent.Executors.newScheduledThreadPool;
import static net.jcip.examples.ch05.LaunderThrowable.launderThrowable;

/**
 * TimedRun2
 *
 * @list 7.9
 * @smell Good
 * @author Brian Goetz and Tim Peierls
 * 
 * <p>The example of an attempt at running an arbitrary Runnable for a given amount of time. 
 * This example addresses the exception-handling problem of {@code PrimeGenerator}'s aSecondOfPrimes() and the problems in the {@code TimedRun1}.
 * 
 */
public class TimedRun2 {
    private static final ScheduledExecutorService cancelExec = newScheduledThreadPool(1);

    public static void timedRun(final Runnable r, long timeout, TimeUnit unit) throws InterruptedException {
        class RethrowableTask implements Runnable {
            private volatile Throwable t;                            // The saved Throwable is shared between the two threads, and so is declared volatile to safely publish it from the task thread to the timedRun thread.

            public void run() {
                try {
                    r.run();
                } catch (Throwable t) {
                    this.t = t;
                }
            }

            void rethrow() {
                if (t != null)
                    throw launderThrowable(t);
            }
        }

        RethrowableTask task = new RethrowableTask();
        final Thread taskThread = new Thread(task);
        taskThread.start();
        cancelExec.schedule(new Runnable() {
            public void run() {
                taskThread.interrupt();
            }
        }, timeout, unit);
        taskThread.join(unit.toMillis(timeout));
        task.rethrow();                                              // It checks if an exception was thrown from the task and if so, rethrows it in the thread calling timedRun.
    }
}
