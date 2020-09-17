package net.jcip.examples.ch04.vehicletracker.delegation;

import java.util.*;
import java.util.concurrent.*;
import java.awt.Point;

import net.jcip.annotations.*;

/**
 * DelegatingVehicleTracker
 *
 * @list 4.7
 * @smell Good
 * @author Brian Goetz and Tim Peierls
 * 
 * <p>Vehicle tracker implementation using the delegation.
 */
@ThreadSafe
public class DelegatingVehicleTracker {
    private final ConcurrentMap<String, Point> locations;                      // store the locations in a thread-safe Map implementation: ConcurrentHashMap
    private final Map<String, Point> unmodifiableMap;

    public DelegatingVehicleTracker(Map<String, Point> points) {
        locations = new ConcurrentHashMap<String, Point>(points);
        unmodifiableMap = Collections.unmodifiableMap(locations);
    }

    public Map<String, Point> getLocations() {                                 // Unlike MonitorVehicleTracker, no need to use synchronization, all access to state is managed by ConcurrentHashMap,
        return unmodifiableMap;                                                // and all the keys and values of the Map are immutable.
    }

    public Point getLocation(String id) {                                      // Unlike MonitorVehicleTracker, no need to use synchronization, all access to state is managed by ConcurrentHashMap,
        return locations.get(id);                                              // and all the keys and values of the Map are immutable.
    }

    public void setLocation(String id, int x, int y) {                         // Unlike MonitorVehicleTracker, no need to use synchronization, all access to state is managed by ConcurrentHashMap,
        if (locations.replace(id, new Point(x, y)) == null)                    // and all the keys and values of the Map are immutable.
            throw new IllegalArgumentException("invalid vehicle name: " + id);
    }

    // Alternate version of getLocations (Listing 4.8)
    public Map<String, Point> getLocationsAsStatic() {
        return Collections.unmodifiableMap(
                new HashMap<String, Point>(locations));
    }
}

