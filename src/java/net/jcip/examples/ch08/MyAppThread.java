package net.jcip.examples.ch08;

import java.util.concurrent.atomic.*;
import java.util.logging.*;

/**
 * MyAppThread
 * 
 * @list 8.7
 * @smell Good
 * @author Brian Goetz and Tim Peierls
 *
 * <p>Example of a custom thread.
 */
public class MyAppThread extends Thread {
    public static final String DEFAULT_NAME = "MyAppThread";
    private static volatile boolean debugLifecycle = false;                    // The debug flag.
    private static final AtomicInteger created = new AtomicInteger();          // The number of threads have been created.
    private static final AtomicInteger alive = new AtomicInteger();            // The number of threads are still alive.
    private static final Logger log = Logger.getAnonymousLogger();

    public MyAppThread(Runnable r) {
        this(r, DEFAULT_NAME);                                                 // Lets you provide a thread name.
    }

    public MyAppThread(Runnable runnable, String name) {
        super(runnable, name + "-" + created.incrementAndGet());
        setUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {    // Sets a custom UncaughtException-Handler that writes a message to a Logger
            public void uncaughtException(Thread t, Throwable e) {
                log.log(Level.SEVERE,
                        "UNCAUGHT in thread " + t.getName(), e);
            }
        });
    }

    public void run() {
        boolean debug = debugLifecycle;
        if (debug) log.log(Level.FINE, "Created " + getName());                // Optionally writes a debug message to the log when a thread is created.
        try {
            alive.incrementAndGet();                                           // Maintains statistics on how many threads have been created and destroyed.
            super.run();
        } finally {
            alive.decrementAndGet();
            if (debug) log.log(Level.FINE, "Exiting " + getName());            // Optionally writes a debug message to the log when a thread is terminated.
        }
    }

    public static int getThreadsCreated() {
        return created.get();
    }

    public static int getThreadsAlive() {
        return alive.get();
    }

    public static boolean getDebug() {
        return debugLifecycle;
    }

    public static void setDebug(boolean b) {
        debugLifecycle = b;
    }
}
