package net.jcip.examples.ch12;

import java.util.concurrent.*;
import java.util.concurrent.atomic.*;

import junit.framework.TestCase;

/**
 * TestingThreadFactory
 * 
 * @list 12.8
 * @list 12.9
 * @smell Good
 * @author Brian Goetz and Tim Peierls
 * 
 * <p>Example of testing a thread pool by a custom thread factory.
 */
public class TestThreadPool extends TestCase {
    private final TestingThreadFactory threadFactory = new TestingThreadFactory();

    // List 12.9 Test method to verify thread pool expansion.
    public void testPoolExpansion() throws InterruptedException {
        int MAX_SIZE = 10;
        ExecutorService exec = Executors.newFixedThreadPool(MAX_SIZE);                   // Create a thread pool with the capacity of 10.

        for (int i = 0; i < 10 * MAX_SIZE; i++)                                          
            exec.execute(new Runnable() {                                                // Submit 10 long-running tasks to the pool makes the number of executing tasks stay constant for long enough.
                public void run() {
                    try {
                        Thread.sleep(Long.MAX_VALUE);                  
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                }
            });
        for (int i = 0; i < 20 && threadFactory.numCreated.get() < MAX_SIZE; i++)
            Thread.sleep(100);
        assertEquals(threadFactory.numCreated.get(), MAX_SIZE);                          // Verify the pool is expanded as expected.
        exec.shutdownNow();                                                              // (The number of threads created by custom thread factory is equal to the size of the thread pool)
    }
}

// List 12.8 Instrument thread creation by using a custom thread factory.
class TestingThreadFactory implements ThreadFactory {
    public final AtomicInteger numCreated = new AtomicInteger();                         // The count of created threads.
    private final ThreadFactory factory = Executors.defaultThreadFactory();

    public Thread newThread(Runnable r) {
        numCreated.incrementAndGet();
        return factory.newThread(r);
    }
}
