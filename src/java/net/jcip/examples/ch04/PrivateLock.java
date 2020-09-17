package net.jcip.examples.ch04;

import net.jcip.annotations.*;


/**
 * PrivateLock
 * 
 * @list 4.3
 * @smell Good
 * @author Brian Goetz and Tim Peierls
 * 
 * <p>Example of guarding state with a private lock.
 */
public class PrivateLock {
    private final Object myLock = new Object();          // This is a private lock.
    @GuardedBy("myLock") Widget widget;

    void someMethod() {
        synchronized (myLock) {                          // Acquires the private lock.
            // Access or modify the state of widget
        }
    }
    
    interface Widget {
    }
}
