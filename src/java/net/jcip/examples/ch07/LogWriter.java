package net.jcip.examples.ch07;

import java.io.PrintWriter;
import java.io.Writer;
import java.util.concurrent.*;

/**
 * LogWriter
 * 
 * @list 7.13
 * @smell Bad
 * @author Brian Goetz and Tim Peierls
 * 
 * <p>A simple logging service in which the logging activity is moved to a separate logger thread.
 * 
 * <p>Drawback: This log writer can not be shutdown because of canceling a producer-consumer activity requires canceling both the producers and the consumers. 
 *              But because the producers in this case are not dedicated threads, canceling them is harder.
 */
public class LogWriter {
    private final BlockingQueue<String> queue;
    private final LoggerThread logger;
    private static final int CAPACITY = 1000;

    public LogWriter(Writer writer) {
        this.queue = new LinkedBlockingQueue<String>(CAPACITY);
        this.logger = new LoggerThread(writer);
    }

    public void start() {
        logger.start();
    }

    public void log(String msg) throws InterruptedException {
        queue.put(msg);                                              // LogWriter hands the log activity off to the logger thread via a BlockingQueue
    }

    private class LoggerThread extends Thread {
        private final PrintWriter writer;

        public LoggerThread(Writer writer) {
            this.writer = new PrintWriter(writer, true); 
        }

        public void run() {
            try {
                while (true)
                    writer.println(queue.take());                    // The logger thread writes it out.    
            } catch (InterruptedException ignored) {
            } finally {
                writer.close();
            }
        }
    }
}
