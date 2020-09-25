package net.jcip.examples.ch12;

import java.util.concurrent.atomic.*;

/**
 * XorShift
 * 
 * @list 12.4
 * @smell Good
 * @author Brian Goetz and Tim Peierls
 * 
 * <p>Medium-quality random number generator suitable for generating test data to test producer-consumer designs.
 */
public class XorShift {
    static final AtomicInteger seq = new AtomicInteger(8862213);
    int x = -1831433054;

    public XorShift(int seed) {
        x = seed;
    }

    public XorShift() {
        this((int) System.nanoTime() + seq.getAndAdd(129));
    }

    public int next() {
        x ^= x << 6;
        x ^= x >>> 21;
        x ^= (x << 7);
        return x;
    }
}
