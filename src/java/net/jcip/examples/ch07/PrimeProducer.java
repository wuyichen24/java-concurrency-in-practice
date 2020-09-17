package net.jcip.examples.ch07;

import java.math.BigInteger;
import java.util.concurrent.*;

/**
 * PrimeProducer
 * 
 * @list 7.5
 * @smell Good
 * @author Brian Goetz and Tim Peierls
 * 
 * <p>Fix the problem in the {code BrokenPrimeProducer} by using thread interruption 
 * instead of a boolean flag to request cancellation.
 */
public class PrimeProducer extends Thread {
    private final BlockingQueue<BigInteger> queue;

    PrimeProducer(BlockingQueue<BigInteger> queue) {
        this.queue = queue;
    }

    public void run() {
        try {
            BigInteger p = BigInteger.ONE;
            while (!Thread.currentThread().isInterrupted())          // Check the current thread's interrupted status.
                queue.put(p = p.nextProbablePrime());                // The blocking method can be notified if an interruption has been requested (more responsive to interruption).
        } catch (InterruptedException consumed) {
            /* Allow thread to exit */
        }
    }

    public void cancel() {
        interrupt();                                                 // Call Thread.interrupt() to interrupt the current thread so that the blocking method put() can be notified.
    }
}
