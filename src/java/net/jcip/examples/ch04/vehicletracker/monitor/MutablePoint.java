package net.jcip.examples.ch04.vehicletracker.monitor;

import net.jcip.annotations.*;

/**
 * MutablePoint
 * 
 * @list 4.5
 * @smell Good
 * @author Brian Goetz and Tim Peierls
 * 
 * <p>Mutable Point class similar to java.awt.Point for representing the vehicle locations.
 * 
 * <p>Mutable Point class used by {@code MonitorVehicleTracker}.
 */
@NotThreadSafe
public class MutablePoint {
    public int x, y;

    public MutablePoint() {
        x = 0;
        y = 0;
    }

    public MutablePoint(MutablePoint p) {
        this.x = p.x;
        this.y = p.y;
    }
}
