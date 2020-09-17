package net.jcip.examples.ch06;

import java.util.concurrent.*;

/**
 * ThreadPerTaskExecutor
 * 
 * @list 6.5
 * @smell Good
 * @author Brian Goetz and Tim Peierls
 * 
 * <p>Implement an executor that starts a new thread for each task.
 */
public class ThreadPerTaskExecutor implements Executor {
    public void execute(Runnable r) {
        new Thread(r).start();
    };
}
