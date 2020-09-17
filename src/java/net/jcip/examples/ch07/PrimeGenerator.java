package net.jcip.examples.ch07;

import static java.util.concurrent.TimeUnit.SECONDS;
import java.math.BigInteger;
import java.util.*;
import java.util.concurrent.*;

import net.jcip.annotations.*;

/**
 * PrimeGenerator
 * 
 * @list 7.1
 * @list 7.2
 * @smell Good
 * @author Brian Goetz and Tim Peierls
 * 
 * <p>Example of a cooperative mechanism for stopping a task: Set a “cancellation requested” flag that the task checks periodically.
 */
@ThreadSafe
public class PrimeGenerator implements Runnable {
    private static ExecutorService exec = Executors.newCachedThreadPool();

    @GuardedBy("this") private final List<BigInteger> primes = new ArrayList<BigInteger>();
    private volatile boolean cancelled;                                        // The cancelled flag to mark the task needs to be stopped.

    public void run() {
        BigInteger p = BigInteger.ONE;
        while (!cancelled) {                                                   // The task checks the cancelled flag before processing the new iteration.
            p = p.nextProbablePrime();
            synchronized (this) {
                primes.add(p);
            }
        }
    }

    public void cancel() {                                                     // The cancel() will set the cancelled flag as true.
        cancelled = true;
    }

    public synchronized List<BigInteger> get() {
        return new ArrayList<BigInteger>(primes);
    }

    static List<BigInteger> aSecondOfPrimes() throws InterruptedException {
        PrimeGenerator generator = new PrimeGenerator();
        exec.execute(generator);
        try {
            SECONDS.sleep(1);
        } finally {
            generator.cancel();                                                // Let the prime generator run for one second before canceling it.
        }
        return generator.get();
    }
}
