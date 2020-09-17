package net.jcip.examples.ch05;

import java.util.concurrent.*;

/**
 * TestHarness
 * 
 * @list 5.11
 * @smell Good
 * @author Brian Goetz and Tim Peierls
 * 
 * <p>Using CountDownLatch for starting and stopping threads in timing tests: How long it takes to run a task n times concurrently
 */
public class TestHarness {
    public long timeTasks(int nThreads, final Runnable task) throws InterruptedException {
        final CountDownLatch startGate = new CountDownLatch(1);                // The starting gate is initialized with a count of one
        final CountDownLatch endGate = new CountDownLatch(nThreads);           // The ending gate is initialized with a count equal to the number of worker threads

        for (int i = 0; i < nThreads; i++) {
            Thread t = new Thread() {
                public void run() {
                    try {
                        startGate.await();                                     // Each worker thread does is wait on the starting gate; this ensures that none of them starts working until they all are ready to start. 
                        try {
                            task.run();
                        } finally {
                            endGate.countDown();                               // Count down on the ending gate; this allows the master thread to wait efficiently until the last of the worker threads has finished
                        }
                    } catch (InterruptedException ignored) {
                    }
                }
            };
            t.start();
        }

        long start = System.nanoTime();
        startGate.countDown();
        endGate.await();
        long end = System.nanoTime();
        return end - start;                                                    // Calculate the elapsed time
    }
}
