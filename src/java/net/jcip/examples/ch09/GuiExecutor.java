package net.jcip.examples.ch09;

import java.util.*;
import java.util.concurrent.*;

/**
 * GuiExecutor
 * 
 * @list 9.2
 * @smell Good
 * @author Brian Goetz and Tim Peierls
 * 
 * <p>An Executor that delegates tasks to {@code SwingUtilities} for execution
 */
public class GuiExecutor extends AbstractExecutorService {
    private static final GuiExecutor instance = new GuiExecutor();   // Singletons have a private constructor and a public factory

    private GuiExecutor() {
    }

    public static GuiExecutor instance() {
        return instance;
    }

    public void execute(Runnable r) {
        if (SwingUtilities.isEventDispatchThread())
            r.run();
        else
            SwingUtilities.invokeLater(r);
    }

    public void shutdown() {
        throw new UnsupportedOperationException();
    }

    public List<Runnable> shutdownNow() {
        throw new UnsupportedOperationException();
    }

    public boolean awaitTermination(long timeout, TimeUnit unit)
            throws InterruptedException {
        throw new UnsupportedOperationException();
    }

    public boolean isShutdown() {
        return false;
    }

    public boolean isTerminated() {
        return false;
    }
}
