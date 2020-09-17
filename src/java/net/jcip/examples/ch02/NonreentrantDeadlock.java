package net.jcip.examples.ch02;

/**
 * NonreentrantDeadlock
 * 
 * @list 2.7
 * @smell Good
 * @author Brian Goetz and Tim Peierls
 * 
 * <p>Code that would deadlock if intrinsic locks were not reentrant.
 * 
 * <p>If intrinsic locks were not reentrant, the call to super.doSomething() 
 * would never be able to acquire the lock because it would be considered 
 * already held, and the thread would permanently stall waiting for a lock it 
 * can never acquire.
 */

class Widget {
    public synchronized void doSomething() {
    }
}

class LoggingWidget extends Widget {
    public synchronized void doSomething() {
        System.out.println(toString() + ": calling doSomething");
        super.doSomething();                                      // 
    }
}
