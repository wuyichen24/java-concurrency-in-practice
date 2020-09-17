package net.jcip.examples.ch07;

import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.*;

/**
 * CheckForMail
 * 
 * @list 7.20
 * @smell Good
 * @author Brian Goetz and Tim Peierls
 * 
 * <p>Example of using a private Executor whose lifetime is bounded by that method.
 */
public class CheckForMail {
    public boolean checkMail(Set<String> hosts, long timeout, TimeUnit unit) throws InterruptedException {
        ExecutorService exec = Executors.newCachedThreadPool();      // Creates a private executor
        
        final AtomicBoolean hasNewMail = new AtomicBoolean(false);   // In order to access the hasNewMail flag from the inner Runnable, it would have to be final, which would preclude modifying it
        try {
            for (final String host : hosts)
                exec.execute(new Runnable() {                        // For each host, submit the task to the executor
                    public void run() {
                        if (checkMail(host))
                            hasNewMail.set(true);
                    }
                });
        } finally {
            exec.shutdown();                                         // It shuts down the executor and waits for termination, when all the mail-checking tasks have completed
            exec.awaitTermination(timeout, unit);
        }
        return hasNewMail.get();
    }

    private boolean checkMail(String host) {
        // Check for mail
        return false;
    }
}
