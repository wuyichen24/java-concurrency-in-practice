package net.jcip.examples.ch08;

import java.util.concurrent.*;

/**
 * MyThreadFactory
 *
 * @list 8.6
 * @smell Good
 * @author Brian Goetz and Tim Peierls
 * 
 * <p>Example of a custom thread factory.
 */
public class MyThreadFactory implements ThreadFactory {    // Implement a ThreadFactory interface.
    private final String poolName;

    public MyThreadFactory(String poolName) {              // Pass a pool-specific name to the constructor so that threads from each pool can be distinguished.
        this.poolName = poolName;
    }

    public Thread newThread(Runnable runnable) {
        return new MyAppThread(runnable, poolName);
    }
}
