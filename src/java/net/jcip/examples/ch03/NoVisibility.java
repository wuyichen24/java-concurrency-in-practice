package net.jcip.examples.ch03;

/**
 * NoVisibility
 * 
 * @list 3.1
 * @smell Bad
 * @author Brian Goetz and Tim Peierls
 * 
 * The program may print 42 in the happy path, but it is possible that it will 
 * print zero, or never terminate at all.
 */
public class NoVisibility {
    private static boolean ready;
    private static int number;

    private static class ReaderThread extends Thread {     
        public void run() {
            while (!ready)                                   // After the main thread update the values of ready and number, 
                Thread.yield();                              // there is no guarantee that the values of ready and number written by the main thread will be visible to the reader thread.
            System.out.println(number);
        }
    }

    public static void main(String[] args) {
        new ReaderThread().start();
        number = 42;
        ready = true;
    }
}
