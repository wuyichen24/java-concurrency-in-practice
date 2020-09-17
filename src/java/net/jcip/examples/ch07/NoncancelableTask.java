package net.jcip.examples.ch07;

import java.util.concurrent.*;

/**
 * NoncancelableTask
 *
 * @list 7.7
 * @smell Good
 * @author Brian Goetz and Tim Peierls
 * 
 * <p>Example of the method, which doesn't support cancellation, can still call interruptible blocking methods.
 */
public class NoncancelableTask {
    public Task getNextTask(BlockingQueue<Task> queue) {
        boolean interrupted = false;
        try {
            while (true) {
                try {
                    return queue.take();                             // The interruptible blocking method (if the blocking queue is empty, take() will be blocked)
                } catch (InterruptedException e) {
                    interrupted = true;                              // Save the interruption status locally and retry the blocking method
                }
            }
        } finally {
            if (interrupted)
                Thread.currentThread().interrupt();
        }
    }

    interface Task {
    }
}
