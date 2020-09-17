package net.jcip.examples.ch04;

import net.jcip.annotations.*;

/**
 * Counter
 * 
 * @list 4.1
 * @smell Good
 * @author Brian Goetz and Tim Peierls
 * 
 * <p>Simple thread-safe counter using the Java monitor pattern.
 */
@ThreadSafe
public final class Counter {
    @GuardedBy("this") private long value = 0;          // This object has only one field, so the value field comprises its entire state.

    public synchronized long getValue() {
        return value;
    }

    public synchronized long increment() {
        if (value == Long.MAX_VALUE)
            throw new IllegalStateException("counter overflow");
        return ++value;
    }
}
