package net.jcip.examples.ch07;

import java.io.PrintWriter;
import java.io.Writer;
import java.util.concurrent.*;

import net.jcip.annotations.*;

/**
 * LogService
 * 
 * @list 7.15
 * @smell Bad
 * @author Brian Goetz and Tim Peierls
 * 
 * <p>Improve {@code LogWriter} by providing reliable shutdown.
 */
public class LogService {
    private final BlockingQueue<String> queue;
    private final LoggerThread loggerThread;
    private final PrintWriter writer;
    @GuardedBy("this") private boolean isShutdown;
    @GuardedBy("this") private int reservations;

    public LogService(Writer writer) {
        this.queue = new LinkedBlockingQueue<String>();
        this.loggerThread = new LoggerThread();
        this.writer = new PrintWriter(writer);
    }

    public void start() {
        loggerThread.start();
    }

    public void stop() {
        synchronized (this) {
            isShutdown = true;
        }
        loggerThread.interrupt();
    }

    public void log(String msg) throws InterruptedException {
        synchronized (this) {                                        // make the submission of a new log message atomic
            if (isShutdown)                                          // atomically check for shutdown and conditionally increment a counter to “reserve” the right to submit a message
                throw new IllegalStateException(/*...*/);
            ++reservations;                                         
        }
        queue.put(msg);
    }

    private class LoggerThread extends Thread {
        public void run() {
            try {
                while (true) {
                    try {
                        synchronized (LogService.this) {
                            if (isShutdown && reservations == 0)
                                break;
                        }
                        String msg = queue.take();
                        synchronized (LogService.this) {
                            --reservations;
                        }
                        writer.println(msg);
                    } catch (InterruptedException e) { /* retry */
                    }
                }
            } finally {
                writer.close();
            }
        }
    }
}

