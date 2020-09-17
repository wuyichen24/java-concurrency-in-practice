package net.jcip.examples.ch03;

import net.jcip.annotations.*;

/**
 * SynchronizedInteger
 * 
 * @list 3.3
 * @smell Bad
 * @author Brian Goetz and Tim Peierls
 * 
 * <p>Synchronize both the getter and setter for thread-safe.
 */
@ThreadSafe
public class SynchronizedInteger {
    @GuardedBy("this") private int value;

    public synchronized int get() {
        return value;
    }

    public synchronized void set(int value) {
        this.value = value;
    }
}
