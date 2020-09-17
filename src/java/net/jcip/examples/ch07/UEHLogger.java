package net.jcip.examples.ch07;

import java.util.logging.*;

/**
 * UEHLogger
 * 
 * @list 7.25
 * @smell Good
 * @author Brian Goetz and Tim Peierls
 * 
 * <p>Example of how to handle uncaught exception: Write an error message and stack trace to the application log.
 */
public class UEHLogger implements Thread.UncaughtExceptionHandler {
    public void uncaughtException(Thread t, Throwable e) {
        Logger logger = Logger.getAnonymousLogger();
        logger.log(Level.SEVERE, "Thread terminated with exception: " + t.getName(), e);          //  Write an error message and stack trace to the application log.
    }
}
