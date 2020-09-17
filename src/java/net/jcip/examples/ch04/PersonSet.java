package net.jcip.examples.ch04;

import java.util.*;

import net.jcip.annotations.*;

/**
 * PersonSet
 *
 * @list 4.2
 * @smell Good
 * @author Brian Goetz and Tim Peierls
 * 
 * <p>Example of how confinement and locking can work together to make a class thread-safe even when its component state variables are not.
 */
@ThreadSafe
public class PersonSet {
    @GuardedBy("this") private final Set<Person> mySet = new HashSet<Person>();   // Even if HashSet is not thread-safe, but because mySet is private and not allowed to escape, the HashSet is confined to the PersonSet.

    public synchronized void addPerson(Person p) {                                // This function acquires the lock on the PersonSet. All its state is guarded by its intrinsic lock
        mySet.add(p);
    }

    public synchronized boolean containsPerson(Person p) {                        // This function acquires the lock on the PersonSet. All its state is guarded by its intrinsic lock
        return mySet.contains(p);
    }

    interface Person {
    }
}
