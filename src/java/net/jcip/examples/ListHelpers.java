package net.jcip.examples;

import java.util.*;

import net.jcip.annotations.*;

/**
 * ListHelder
 *
 * @list 4.14
 * @list 4.15
 * @author Brian Goetz and Tim Peierls
 * 
 * <p>Examples of thread-safe and non-thread-safe implementations of put-if-absent helper methods for List
 */

@NotThreadSafe
class BadListHelper <E> {
    public List<E> list = Collections.synchronizedList(new ArrayList<E>());

    public synchronized boolean putIfAbsent(E x) {                   // Synchronizes on the wrong lock, which means that putIfAbsent is not atomic relative to other operations on the List
        boolean absent = !list.contains(x);
        if (absent)
            list.add(x);
        return absent;
    }
}

@ThreadSafe
class GoodListHelper <E> {
    public List<E> list = Collections.synchronizedList(new ArrayList<E>());

    public boolean putIfAbsent(E x) {                                // Synchronizes on the correct lock, 
        synchronized (list) {                                        // there is a guarantee that another thread won't modify the list while putIfAbsent is executing.
            boolean absent = !list.contains(x);
            if (absent)
                list.add(x);
            return absent;
        }
    }
}
