package net.jcip.examples.ch09;

import java.lang.reflect.InvocationTargetException;
import java.util.concurrent.*;

/**
 * SwingUtilities
 * 
 * @list 9.1
 * @smell Good
 * @author Brian Goetz and Tim Peierls
 * 
 * <p>Implementing SwingUtilities using an Executor.
 */
public class SwingUtilities {
    private static final ExecutorService exec =
            Executors.newSingleThreadExecutor(new SwingThreadFactory());
    private static volatile Thread swingThread;

    private static class SwingThreadFactory implements ThreadFactory {
        public Thread newThread(Runnable r) {
            swingThread = new Thread(r);
            return swingThread;
        }
    }

    public static boolean isEventDispatchThread() {                            // Determines whether the current thread is the event thread
        return Thread.currentThread() == swingThread;
    }

    public static void invokeLater(Runnable task) {                            // Schedules a Runnable for execution on the event thread (callable from any thread)
        exec.execute(task);
    }

    public static void invokeAndWait(Runnable task)                            // Schedules a Runnable task for execution on the event thread and blocks the current thread until it completes (callable only from a non-GUI thread)
            throws InterruptedException, InvocationTargetException {
        Future f = exec.submit(task);
        try {
            f.get();
        } catch (ExecutionException e) {
            throw new InvocationTargetException(e);
        }
    }
}
