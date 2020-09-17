package net.jcip.examples.ch07;

import java.math.BigInteger;
import java.util.concurrent.*;

/**
 * BrokenPrimeProducer
 *
 * @list 7.3
 * @smell Bad
 * @author Brian Goetz and Tim Peierls
 * 
 * <p>Example of the problem when using cancellation flag to terminate a task: The task might never check the cancellation flag and therefore might never terminate.
 */
class BrokenPrimeProducer extends Thread {
    private final BlockingQueue<BigInteger> queue;
    private volatile boolean cancelled = false;

    BrokenPrimeProducer(BlockingQueue<BigInteger> queue) {
        this.queue = queue;
    }

    public void run() {
        try {
            BigInteger p = BigInteger.ONE;
            while (!cancelled)                                     
                queue.put(p = p.nextProbablePrime());                // If the blocking queue is full, so this put() method will be blocked. The cancellation flag will not be checked even if the flag has been set to true.
            
        } catch (InterruptedException consumed) {
        }
    }

    public void cancel() {
        cancelled = true;
    }
}

