package net.jcip.examples.ch05;

import java.util.*;
import java.util.concurrent.*;

/**
 * Memoizer2
 * 
 * @list 5.17
 * @smell Good
 * @author Brian Goetz and Tim Peierls
 * 
 * <p>The improved version of {@code Memoizer1} by replacing HashMap with ConcurrentHashMap.
 * 
 * <p>Drawback: There is a window of vulnerability in which two threads calling compute at the same time could end up computing the same value.
 */
public class Memoizer2 <A, V> implements Computable<A, V> {
    private final Map<A, V> cache = new ConcurrentHashMap<A, V>();
    private final Computable<A, V> c;

    public Memoizer2(Computable<A, V> c) {
        this.c = c;
    }

    public V compute(A arg) throws InterruptedException {            // Since ConcurrentHashMap is thread-safe, there is no need to synchronize when accessing the backing Map
        V result = cache.get(arg);                                   // There is a window of vulnerability in which two threads calling compute at the same time could end up computing the same value
        if (result == null) {                                        // (Both threads don't see the result has been cache and  both of them compute the same result in parallel)
            result = c.compute(arg);                              
            cache.put(arg, result);
        }
        return result;
    }
}
