package net.jcip.examples.ch06;

import java.util.concurrent.*;

/**
 * WithinThreadExecutor
 * 
 * @list 6.6
 * @smell Good
 * @author Brian Goetz and Tim Peierls
 * 
 * <p>Implements an executor that executes tasks synchronously in the calling thread
 */
public class WithinThreadExecutor implements Executor {
    public void execute(Runnable r) {
        r.run();
    };
}
