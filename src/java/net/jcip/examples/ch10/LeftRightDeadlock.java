package net.jcip.examples.ch10;

/**
 * LeftRightDeadlock
 *
 * @list 10.1
 * @smell Bad
 * @author Brian Goetz and Tim Peierls
 * 
 * <p>Example of a simple lock-ordering deadlock: Because the two threads attempted to acquire the same locks in a different order.
 */
public class LeftRightDeadlock {
    private final Object left = new Object();
    private final Object right = new Object();

    public void leftRight() {
        synchronized (left) {                    // One thread calls this function: Acquire the left lock and try to acquire the right lock.
            synchronized (right) {
                doSomething();
            }
        }
    }

    public void rightLeft() {
        synchronized (right) {                   // Another thread calls this function: Acquire the right lock and try to acquire the left lock.
            synchronized (left) {
                doSomethingElse();
            }
        }
    }

    void doSomething() {
    }

    void doSomethingElse() {
    }
}
