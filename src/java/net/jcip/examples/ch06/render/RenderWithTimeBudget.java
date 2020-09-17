package net.jcip.examples.ch06.render;

import java.util.concurrent.*;
import static java.util.concurrent.TimeUnit.NANOSECONDS;

/**
 * RenderWithTimeBudget
 *
 * @list 6.16
 * @smell Good
 * @author Brian Goetz and Tim Peierls
 * 
 * <p>Example of adding timeout on Future.get().
 */
public class RenderWithTimeBudget {
    private static final Ad DEFAULT_AD = new Ad();
    private static final long TIME_BUDGET = 1000;
    private static final ExecutorService exec = Executors.newCachedThreadPool();

    Page renderPageWithAd() throws InterruptedException {
        long endNanos = System.nanoTime() + TIME_BUDGET;
        Future<Ad> f = exec.submit(new FetchAdTask());
        // Render the page while waiting for the ad
        Page page = renderPageBody();
        Ad ad;
        try {
            // Only wait for the remaining time budget
            long timeLeft = endNanos - System.nanoTime();
            ad = f.get(timeLeft, NANOSECONDS);                       // Add timeout on Future.get()
        } catch (ExecutionException e) {
            ad = DEFAULT_AD;
        } catch (TimeoutException e) {                               // If Future.get() is timeout, it will throw TimeoutException, you can add operation to handle the timeout.
            ad = DEFAULT_AD;                                         // If the get times out, it cancels the ad-fetching task and uses a default advertisement instead.
            f.cancel(true);                                          // The true parameter means that the task thread can be interrupted if the task is currently running.
        }
        page.setAd(ad);
        return page;
    }

    Page renderPageBody() { return new Page(); }


    static class Ad {
    }

    static class Page {
        public void setAd(Ad ad) { }
    }

    static class FetchAdTask implements Callable<Ad> {
        public Ad call() {
            return new Ad();
        }
    }

}

