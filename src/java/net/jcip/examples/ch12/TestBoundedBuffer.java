package net.jcip.examples.ch12;

import junit.framework.TestCase;

/**
 * TestBoundedBuffer
 * 
 * @list 12.2
 * @list 12.3
 * @list 12.7
 * @smell Good
 * @author Brian Goetz and Tim Peierls
 * 
 * <p>The basic unit tests for {@code SemaphoreBoundedBuffer}.
 */
public class TestBoundedBuffer extends TestCase {
    private static final long LOCKUP_DETECT_TIMEOUT = 1000;
    private static final int CAPACITY = 10000;
    private static final int THRESHOLD = 10000;

    public void testIsEmptyWhenConstructed() {                                                     // List 12.2 basic unit test
        SemaphoreBoundedBuffer<Integer> bb = new SemaphoreBoundedBuffer<Integer>(10);              // A freshly created buffer should identify itself as empty, and also as not full.
        assertTrue(bb.isEmpty());
        assertFalse(bb.isFull());
    }

    public void testIsFullAfterPuts() throws InterruptedException {                                // List 12.2 basic unit test 
        SemaphoreBoundedBuffer<Integer> bb = new SemaphoreBoundedBuffer<Integer>(10);              // Insert N elements into a buffer with capacity N (which should succeed without blocking), 
        for (int i = 0; i < 10; i++)                                                               // and test that the buffer recognizes that it is full (and not empty).
            bb.put(i);
        assertTrue(bb.isFull());
        assertFalse(bb.isEmpty());
    }


    public void testTakeBlocksWhenEmpty() {                                                        // List 12.3 test blocking method
        final SemaphoreBoundedBuffer<Integer> bb = new SemaphoreBoundedBuffer<Integer>(10);
        Thread taker = new Thread() {                                                              // Create a “taker” thread that attempts to take an element from an empty buffer.
            public void run() {
                try {
                    int unused = bb.take();
                    fail();                                                                        // If take() succeeds, it registers failure (take method should block taker thread goes here).
                } catch (InterruptedException success) {                                           // If the taker thread has correctly blocked in the take operation, it will throw InterruptedException (interrupt a blocking method will throw InterruptedException), 
                }                                                                                  // and the catch block for this exception treats this as success and lets the thread exit.
            }
        };
        try {
            taker.start();                                                                         // The test runner thread starts the taker thread
            Thread.sleep(LOCKUP_DETECT_TIMEOUT);
            taker.interrupt();                                                                     // Waits a long time, and then interrupts it (and then the take() in the taker thread should throw InterruptedException - see line 42)
            taker.join(LOCKUP_DETECT_TIMEOUT);                                                     // The main test runner thread then attempts to join with the taker thread and verifies that the join returned successfully by calling Thread.isAlive()
            assertFalse(taker.isAlive());                                                          // If the taker thread responded to the interrupt(), the join() should complete quickly.
                                                                                                   // The timed join ensures that the test completes even if take() gets stuck in some unexpected way.
        } catch (Exception unexpected) {
            fail();
        }
    }

    class Big {
        double[] data = new double[100000];
    }

    public void testLeak() throws InterruptedException {                                           // List 12.7 test resource management
        SemaphoreBoundedBuffer<Big> bb = new SemaphoreBoundedBuffer<Big>(CAPACITY);
        int heapSize1 = snapshotHeap();                                                            // Get the heap size before testing.
        for (int i = 0; i < CAPACITY; i++)                                                         // Insert several large objects into a bounded buffer and then removes them.
            bb.put(new Big());
        for (int i = 0; i < CAPACITY; i++)
            bb.take();
        int heapSize2 = snapshotHeap();                                                            // Get the heap size after testing.
        assertTrue(Math.abs(heapSize1 - heapSize2) < THRESHOLD);                                   // After testing, make sure the heap size didn't increase too much.
    }

    // The placeholder for a heap-inspection tool to snapshot the heap and return heap size.
    private int snapshotHeap() {
        return 0;
    }

}
