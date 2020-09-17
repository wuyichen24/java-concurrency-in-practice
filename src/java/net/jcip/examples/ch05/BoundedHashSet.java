package net.jcip.examples.ch05;

import java.util.*;
import java.util.concurrent.*;

/**
 * BoundedHashSet
 * 
 * @list 5.14
 * @smell Good
 * @author Brian Goetz and Tim Peierls
 * 
 * <p>Example of using a Semaphore to turn any collection into a blocking bounded collection.
 */
public class BoundedHashSet <T> {
    private final Set<T> set;
    private final Semaphore sem;

    public BoundedHashSet(int bound) {
        this.set = Collections.synchronizedSet(new HashSet<T>());
        sem = new Semaphore(bound);
    }

    public boolean add(T o) throws InterruptedException {
        sem.acquire();                                     // The add operation acquires a permit before adding the item into the underlying collection.
        boolean wasAdded = false;
        try {
            wasAdded = set.add(o);
            return wasAdded;
        } finally {
            if (!wasAdded)
                sem.release();                             // If the underlying add operation does not actually add anything, it releases the permit immediately.
        }
    }

    public boolean remove(Object o) {
        boolean wasRemoved = set.remove(o);
        if (wasRemoved)
            sem.release();                                 // A successful remove operation releases a permit, enabling more elements to be added
        return wasRemoved;
    }
}
