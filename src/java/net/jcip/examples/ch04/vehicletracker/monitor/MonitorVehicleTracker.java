package net.jcip.examples.ch04.vehicletracker.monitor;

import java.util.*;

import net.jcip.annotations.*;

/**
 * MonitorVehicleTracker
 * 
 * @list 4.4
 * @smell Good
 * @author Brian Goetz and Tim Peierls
 * 
 * <p>Vehicle tracker implementation using the Java monitor pattern.
 */
@ThreadSafe
 public class MonitorVehicleTracker {
    @GuardedBy("this") private final Map<String, MutablePoint> locations;

    public MonitorVehicleTracker(Map<String, MutablePoint> locations) {
        this.locations = deepCopy(locations);
    }

    public synchronized Map<String, MutablePoint> getLocations() {             // Because of locations Map is not thread-safe, use explicit synchronization to make this class thread-safe
        return deepCopy(locations);
    }

    public synchronized MutablePoint getLocation(String id) {                  // Because of locations Map is not thread-safe, use explicit synchronization to make this class thread-safe
        MutablePoint loc = locations.get(id);
        return loc == null ? null : new MutablePoint(loc);
    }

    public synchronized void setLocation(String id, int x, int y) {            // Because of locations Map is not thread-safe, use explicit synchronization to make this class thread-safe
        MutablePoint loc = locations.get(id);
        if (loc == null)
            throw new IllegalArgumentException("No such ID: " + id);
        loc.x = x;
        loc.y = y;
    }

    private static Map<String, MutablePoint> deepCopy(Map<String, MutablePoint> m) {
        Map<String, MutablePoint> result = new HashMap<String, MutablePoint>();

        for (String id : m.keySet())
            result.put(id, new MutablePoint(m.get(id)));

        return Collections.unmodifiableMap(result);
    }
}

