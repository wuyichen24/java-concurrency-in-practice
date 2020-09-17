package net.jcip.examples.ch05;

import java.util.*;
import java.util.concurrent.*;

/**
 * Memoizer3
 *
 * @list 5.18
 * @smell Good
 * @author Brian Goetz and Tim Peierls
 * 
 * <p>The improved version of {@code Memoizer2} by using FutureTask.
 * 
 * <p>Drawback: There is still a small window of vulnerability in which two threads might compute the same value. This window is far smaller than in {@code Memoizer2}.
 */
public class Memoizer3 <A, V> implements Computable<A, V> {
    private final Map<A, Future<V>> cache = new ConcurrentHashMap<A, Future<V>>();
    private final Computable<A, V> c;

    public Memoizer3(Computable<A, V> c) {
        this.c = c;
    }

    public V compute(final A arg) throws InterruptedException {
        Future<V> f = cache.get(arg);                                          // First checks to see if the calculation has been started or not by other thread or not.
        if (f == null) {                                                       // If the computation is not in progress, it creates a FutureTask, registers it in the Map, and starts the computation
            Callable<V> eval = new Callable<V>() {
                public V call() throws InterruptedException {
                    return c.compute(arg);
                }
            };
            FutureTask<V> ft = new FutureTask<V>(eval);
            f = ft;
            cache.put(arg, ft);
            ft.run(); // call to c.compute happens here
        }
        try {
            return f.get();                                                    // If the computation is in progress, blocks/waits the computation is done and then get the result directly. 
                                                                               // If the result is already cached, get the result immediately.
        } catch (ExecutionException e) {
            throw LaunderThrowable.launderThrowable(e.getCause());
        }
    }
}