package net.jcip.examples.ch12;

/**
 * BarrierTimer
 * 
 * @list 12.11
 * @smell Good
 * @author Brian Goetz and Tim Peierls
 * 
 * <p>Barrier-based timer by using a barrier action that measures the start and end time.
 */
public class BarrierTimer implements Runnable {
    private boolean started;
    private long startTime, endTime;

    public synchronized void run() {
        long t = System.nanoTime();
        if (!started) {
            started = true;
            startTime = t;
        } else
            endTime = t;
    }

    public synchronized void clear() {
        started = false;
    }

    public synchronized long getTime() {
        return endTime - startTime;
    }
}
