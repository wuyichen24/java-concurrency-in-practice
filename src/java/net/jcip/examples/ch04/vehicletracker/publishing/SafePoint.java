package net.jcip.examples.ch04.vehicletracker.publishing;

import net.jcip.annotations.*;

/**
 * SafePoint
 *
 * @list 4.11
 * @smell Good
 * @author Brian Goetz and Tim Peierls
 * 
 * <p>Mutable Point class but thread-safe used by {@code DelegatingVehicleTracker}.
 */
@ThreadSafe
public class SafePoint {
    @GuardedBy("this") private int x, y;

    private SafePoint(int[] a) {
        this(a[0], a[1]);
    }

    public SafePoint(SafePoint p) {
        this(p.get());
    }

    public SafePoint(int x, int y) {
        this.set(x, y);
    }

    public synchronized int[] get() {                      // Use synchronization to avoid a caller to see an inconsistent value
        return new int[]{x, y};
    }

    public synchronized void set(int x, int y) {           // Use synchronization to avoid a caller to see an inconsistent value
        this.x = x;
        this.y = y;
    }
}
