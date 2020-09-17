package net.jcip.examples.ch04;

import java.util.concurrent.atomic.*;

import net.jcip.annotations.NotThreadSafe;

/**
 * NumberRange
 *
 * @list 4.10
 * @smell Bad
 * @author Brian Goetz and Tim Peierls
 * 
 * <p>The example of an object follows delegation but still not thread-safe.
 */
@NotThreadSafe
public class NumberRange {
    // Additional constraint: lower <= upper (those state variables are not independent)
    private final AtomicInteger lower = new AtomicInteger(0);
    private final AtomicInteger upper = new AtomicInteger(0);

    public void setLower(int i) {                                              // Warning - unsafe check-then-act: if 2 threads call setLower() and setUpper() concurrently, it may cause an issue which doesn't obey the additional constraint
        if (i > upper.get())
            throw new IllegalArgumentException("can't set lower to " + i + " > upper");
        lower.set(i);
    }

    public void setUpper(int i) {                                              // Warning - unsafe check-then-act: if 2 threads call setLower() and setUpper() concurrently, it may cause an issue which doesn't obey the additional constraint
        if (i < lower.get())
            throw new IllegalArgumentException("can't set upper to " + i + " < lower");
        upper.set(i);
    }

    public boolean isInRange(int i) {
        return (i >= lower.get() && i <= upper.get());
    }
}

