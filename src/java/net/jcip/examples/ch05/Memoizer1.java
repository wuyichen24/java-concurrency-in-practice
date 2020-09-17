package net.jcip.examples.ch05;

import java.math.BigInteger;
import java.util.*;

import net.jcip.annotations.*;

/**
 * Memoizer1
 *
 * @list 5.16
 * @smell Good
 * @author Brian Goetz and Tim Peierls
 * 
 * <p>Initial cache attempt using HashMap and synchronization
 * 
 * <p>Drawback: Only one thread at a time can execute compute at all. If another thread is busy computing a result, other threads calling compute may be blocked for a long time.
 */
public class Memoizer1 <A, V> implements Computable<A, V> {
    @GuardedBy("this") private final Map<A, V> cache = new HashMap<A, V>();
    private final Computable<A, V> c;

    public Memoizer1(Computable<A, V> c) {
        this.c = c;
    }

    public synchronized V compute(A arg) throws InterruptedException {         // HashMap is not thread-safe, so use synchronization to make sure only one thread at a time can execute compute at all (obvious scalability problem).
        V result = cache.get(arg);                                             // First checks whether the desired result is already cached, and returns the precomputed value if it is
        if (result == null) {
            result = c.compute(arg);
            cache.put(arg, result);
        }
        return result;
    }
}

interface Computable <A, V> {
    V compute(A arg) throws InterruptedException;
}

class ExpensiveFunction
        implements Computable<String, BigInteger> {
    public BigInteger compute(String arg) {
        // after deep thought...
        return new BigInteger(arg);
    }
}
