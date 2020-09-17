package net.jcip.examples.ch04.vehicletracker.delegation;

import net.jcip.annotations.*;

/**
 * Point
 *
 * @list 4.6
 * @smell Good
 * @author Brian Goetz and Tim Peierls
 * 
 * <p>Immutable Point class used by {@code DelegatingVehicleTracker}.
 */
@Immutable
public class Point {
    public final int x, y;

    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }
}
