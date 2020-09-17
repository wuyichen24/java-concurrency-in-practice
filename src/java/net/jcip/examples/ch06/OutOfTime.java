package net.jcip.examples.ch06;

import java.util.*;
import static java.util.concurrent.TimeUnit.SECONDS;

/**
 * OutOfTime
 * 
 * @list 6.9
 * @smell Bad
 * @author Brian Goetz and Tim Peierls
 * 
 * <p>Example of the problem with Timer: 
 *     You might expect the program to run for six seconds and exit, 
 *     but what actually happens is that it terminates after one second with an IllegalStateException.
 */

public class OutOfTime {
    public static void main(String[] args) throws Exception {
        Timer timer = new Timer();
        timer.schedule(new ThrowTask(), 1);
        SECONDS.sleep(1);
        timer.schedule(new ThrowTask(), 1);                // After the first timer task throws RuntimeException. The timer will become unavailable and throw the exception with message: Timer already cancelled.
        SECONDS.sleep(5);
    }

    static class ThrowTask extends TimerTask {
        public void run() {
            throw new RuntimeException();
        }
    }
}
