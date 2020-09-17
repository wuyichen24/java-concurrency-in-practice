package net.jcip.examples.ch10;

import java.util.*;

import net.jcip.annotations.*;
import net.jcip.examples.ch04.vehicletracker.delegation.Point;

/**
 * CooperatingDeadlock
 * 
 * @list 10.5
 * @smell Bad
 * @author Brian Goetz and Tim Peierls
 * 
 * <p>Example of lock-ordering deadlock between cooperating objects.
 * 
 * <p>If a thread calls Taxi.setLocation(), it will acquire locks in this order: Taxi and then Dispatcher.
 *    If another thread calls Dispatcher.getImage(), it will acquire locks in this order: Dispatcher and then Taxi.
 *    Inconsistent lock-ordering will cause deadlock.
 */
public class CooperatingDeadlock {
	// Taxi represents an individual taxi with a location and a destination
    class Taxi {
        @GuardedBy("this") private Point location, destination;
        private final Dispatcher dispatcher;

        public Taxi(Dispatcher dispatcher) {
            this.dispatcher = dispatcher;
        }

        public synchronized Point getLocation() {
            return location;
        }

        public synchronized void setLocation(Point location) {                 // If one thread calls this setLocation(), because of setLocation() is calling notifyAvailable() and both of them are synchronized,
            this.location = location;                                          // so that thread will acquire Taxi lock and then Dispatcher lock.
            if (location.equals(destination))
                dispatcher.notifyAvailable(this);
        }

        public synchronized Point getDestination() {
            return destination;
        }

        public synchronized void setDestination(Point destination) {
            this.destination = destination;
        }
    }

    // Dispatcher represents a fleet of taxis.
    class Dispatcher {
        @GuardedBy("this") private final Set<Taxi> taxis;
        @GuardedBy("this") private final Set<Taxi> availableTaxis;

        public Dispatcher() {
            taxis = new HashSet<Taxi>();
            availableTaxis = new HashSet<Taxi>();
        }

        public synchronized void notifyAvailable(Taxi taxi) {
            availableTaxis.add(taxi);
        }

        public synchronized Image getImage() {                                 // If one thread calls this getImage(), because of getImage() is calling getLocation() and both of them are synchronized,
            Image image = new Image();                                         // so that thread will acquire Dispatcher lock and then Taxi lock.
            for (Taxi t : taxis)
                image.drawMarker(t.getLocation());
            return image;
        }
    }

    class Image {
        public void drawMarker(Point p) {
        }
    }
}
