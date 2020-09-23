package net.jcip.examples.ch11;

import net.jcip.annotations.*;

/**
 * StripedMap
 * 
 * @list 11.8
 * @smell Good
 * @author Brian Goetz and Tim Peierls
 * 
 * <p>Illustrates implementing a hash-based map using lock striping.
 */
@ThreadSafe
public class StripedMap {
    private static final int N_LOCKS = 16;                           // Synchronization policy: buckets[n] guarded by locks[n%N_LOCKS]
    private final Node[] buckets;
    private final Object[] locks;

    private static class Node {
        Node next;
        Object key;
        Object value;
    }

    public StripedMap(int numBuckets) {
        buckets = new Node[numBuckets];
        locks = new Object[N_LOCKS];
        for (int i = 0; i < N_LOCKS; i++)
            locks[i] = new Object();
    }

    private final int hash(Object key) {
        return Math.abs(key.hashCode() % buckets.length);
    }

    public Object get(Object key) {
        int hash = hash(key);
        synchronized (locks[hash % N_LOCKS]) {                       // Most methods, like get, need acquire only a single bucket lock.
            for (Node m = buckets[hash]; m != null; m = m.next)
                if (m.key.equals(key))
                    return m.value;
        }
        return null;
    }

    public void clear() {
        for (int i = 0; i < buckets.length; i++) {
            synchronized (locks[i % N_LOCKS]) {                      // Some methods may need to acquire all the locks (not simultaneously).
                buckets[i] = null;
            }
        }
    }
}
