package net.jcip.examples.ch12;

import java.util.concurrent.*;
import java.util.concurrent.atomic.*;

import junit.framework.TestCase;

/**
 * PutTakeTest
 * 
 * @list 12.5
 * @list 12.6
 * @smell Good
 * @author Brian Goetz and Tim Peierls
 * 
 * <p>Multiple-threads safety test for {@code SemaphoreBoundedBuffer}.
 * 
 * <p>Use N producer threads that generate elements and enqueue them, and N consumer threads that dequeue them.
 */
public class PutTakeTest extends TestCase {
    protected static final ExecutorService pool = Executors.newCachedThreadPool();
    protected CyclicBarrier barrier;
    protected final SemaphoreBoundedBuffer<Integer> bb;
    protected final int nTrials;                                                         // The number of test elements for each thread.
    protected final int nPairs;                                                          // The number of producer threads and consumer threads.
    protected final AtomicInteger putSum = new AtomicInteger(0);                         // The put checksum
    protected final AtomicInteger takeSum = new AtomicInteger(0);                        // The take checksum

    public static void main(String[] args) throws Exception {
        new PutTakeTest(10, 10, 100000).test();
        pool.shutdown();
    }

    // Initialize the test class
    public PutTakeTest(int capacity, int npairs, int ntrials) {
        this.bb = new SemaphoreBoundedBuffer<Integer>(capacity);
        this.nTrials = ntrials;
        this.nPairs = npairs;
        this.barrier = new CyclicBarrier(npairs * 2 + 1);
    }

    // The test function
    void test() {
        try {
            for (int i = 0; i < nPairs; i++) {
                pool.execute(new Producer());                                            // Start N producer threads.
                pool.execute(new Consumer());                                            // Start N consumer threads.
            }
            barrier.await();                                                             // Wait for all threads to be ready (ensures that all threads are up and running before any start working).
            barrier.await();                                                             // Wait for all threads to finish.
            assertEquals(putSum.get(), takeSum.get());                                   // After running the producer threads and consumer thread, the put checksum should be same with the take checksum.
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    
    // Medium-quality random number generator.
    static int xorShift(int y) {
        y ^= (y << 6);
        y ^= (y >>> 21);
        y ^= (y << 7);
        return y;
    }

    // Producer thread
    class Producer implements Runnable {
        public void run() {
            try {
                int seed = (this.hashCode() ^ (int) System.nanoTime());
                int sum = 0;
                barrier.await();
                for (int i = nTrials; i > 0; --i) {
                    bb.put(seed);                                                        // Enqueue a element into the queue
                    sum += seed;                                                         // Update the put checksum
                    seed = xorShift(seed);
                }
                putSum.getAndAdd(sum);
                barrier.await();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    // Consumer thread
    class Consumer implements Runnable {
        public void run() {
            try {
                barrier.await();
                int sum = 0;
                for (int i = nTrials; i > 0; --i) {
                    sum += bb.take();                                                    // Dequeue a element from the queue
                }                                                                        // Update the take checksum
                takeSum.getAndAdd(sum);                                                  
                barrier.await();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }
}
