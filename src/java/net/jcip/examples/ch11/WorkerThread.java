package net.jcip.examples.ch11;

import java.util.concurrent.*;

/**
 * WorkerThread
 * 
 * @list 11.1
 * @smell Good
 * @author Brian Goetz and Tim Peierls
 * 
 * <p>Example of serialized access to a task queue.
 */

public class WorkerThread extends Thread {
    private final BlockingQueue<Runnable> queue;

    public WorkerThread(BlockingQueue<Runnable> queue) {
        this.queue = queue;
    }

    public void run() {
        while (true) {
            try {
                Runnable task = queue.take();              // while one thread is dequeing a task, other threads that need to dequeue their next task must wait—and this is where task processing is serialized.
                task.run();
            } catch (InterruptedException e) {
                break;
            }
        }
    }
}
