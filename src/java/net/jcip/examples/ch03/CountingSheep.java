package net.jcip.examples.ch03;

/**
 * CountingSheep
 * 
 * @list 3.4
 * @smell Good
 * @author Brian Goetz and Tim Peierls
 */
public class CountingSheep {
    volatile boolean asleep;

    void tryToSleep() {
        while (!asleep)                          // The thread can be notified when asleep has been set by another thread.
            countSomeSheep();
    }

    void countSomeSheep() {
        // One, two, three...
    }
}








