package net.jcip.examples.ch08;

import java.util.concurrent.*;

import net.jcip.annotations.*;

/**
 * BoundedExecutor
 * 
 * @list 8.4
 * @smell Good
 * @author Brian Goetz and Tim Peierls
 * 
 * <p>Example of using a Semaphore to bound the task injection rate for an unbounded queue.
 */
@ThreadSafe
public class BoundedExecutor {
    private final Executor exec;
    private final Semaphore semaphore;

    public BoundedExecutor(Executor exec, int bound) {
        this.exec = exec;
        this.semaphore = new Semaphore(bound);                       // Set the bound on the semaphore to be equal to the pool size (currently executing) plus the number of queued tasks you want to allow (awaiting execution).
    }

    public void submitTask(final Runnable command)
            throws InterruptedException {
        semaphore.acquire();                                         // Acquires a permit before executing a task.
        try {
            exec.execute(new Runnable() {
                public void run() {
                    try {
                        command.run();
                    } finally {
                        semaphore.release();                         // Releases a permit after executing a task.
                    }
                }
            });
        } catch (RejectedExecutionException e) {
            semaphore.release();
        }
    }
}
