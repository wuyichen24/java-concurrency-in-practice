package net.jcip.examples.ch03;

import net.jcip.annotations.*;

/**
 * MutableInteger
 * 
 * @list 3.2
 * @smell Bad
 * @author Brian Goetz and Tim Peierls
 */

@NotThreadSafe
public class MutableInteger {
    private int value;                 // It is not thread-safe because the value field is accessed from both get() and set().
                                       // If one thread calls set(), other threads calling get() may or may not see that update.
    public int get() {
        return value;
    }

    public void set(int value) {
        this.value = value;
    }
}








