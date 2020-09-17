package net.jcip.examples.ch01;

import net.jcip.annotations.*;

/**
 * Sequence
 *
 * @list 1.2
 * @smell Good
 * @author Brian Goetz and Tim Peierls
 */
@ThreadSafe
public class Sequence {
    @GuardedBy("this") private int nextValue;

    public synchronized int getNext() {         // synchronized makes sure only one thread accesses this function at a time.
        return nextValue++;
    }
}
