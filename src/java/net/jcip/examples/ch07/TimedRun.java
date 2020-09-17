package net.jcip.examples.ch07;

import static net.jcip.examples.ch05.LaunderThrowable.launderThrowable;

import java.util.concurrent.*;

/**
 * TimedRun
 * 
 * @list 7.10
 * @smell Good
 * @author Brian Goetz and Tim Peierls
 * 
 * <p>The example of an attempt at running an arbitrary Runnable for a given amount of time by Future.
 */
public class TimedRun {
    private static final ExecutorService taskExec = Executors.newCachedThreadPool();

    public static void timedRun(Runnable r, long timeout, TimeUnit unit) throws InterruptedException {
        Future<?> task = taskExec.submit(r);
        try {
            task.get(timeout, unit);                                 // Future.get() can set the timeout.
        } catch (TimeoutException e) {                               // If timeout is reached, Future.get() will throw a TimeoutException.
            // task will be cancelled below
        } catch (ExecutionException e) {                             // If the underlying computation throws an exception prior to cancellation, 
            throw launderThrowable(e.getCause());                    // it is rethrown from timedRun, which is the most convenient way for the caller to deal with the exception.
        } finally {
            task.cancel(true);                                       // Cancel tasks whose result is no longer needed.
        }
    }
}
