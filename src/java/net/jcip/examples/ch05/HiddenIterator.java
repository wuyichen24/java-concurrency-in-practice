package net.jcip.examples.ch05;

import java.util.*;

import net.jcip.annotations.*;

/**
 * HiddenIterator
 * 
 * @list 5.3
 * @smell Bad
 * @author Brian Goetz and Tim Peierls
 * 
 * <p>Iteration hidden within string concatenation
 */
public class HiddenIterator {
    @GuardedBy("this") private final Set<Integer> set = new HashSet<Integer>();

    public synchronized void add(Integer i) {
        set.add(i);
    }

    public synchronized void remove(Integer i) {
        set.remove(i);
    }

    public void addTenThings() {
        Random r = new Random();
        for (int i = 0; i < 10; i++)
            add(r.nextInt());
        System.out.println("DEBUG: added ten elements to " + set);   // set will be called as set.toString() in here, the implementation of toString iterates the collection, which needs a lock.
    }
}
