package net.jcip.examples.ch01;

import net.jcip.annotations.*;

/**
 * UnsafeSequence
 *
 * @list 1.1
 * @smell Bad
 * @author Brian Goetz and Tim Peierls
 */
@NotThreadSafe
public class UnsafeSequence {
    private int value;

    /**
     * Returns a unique value.
     */
    public int getNext() {        // If multiple threads call getNext() at the same time, they may receive a same value (They should have received different values).
        return value++;
    }
}
