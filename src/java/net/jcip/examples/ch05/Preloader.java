package net.jcip.examples.ch05;

import java.util.concurrent.*;

/**
 * Preloader
 *
 * @list 5.12
 * @smell Good
 * @author Brian Goetz and Tim Peierls
 * 
 * <p>Using FutureTask to preload data that is needed later
 * 
 * <p>It performs an expensive computation whose results are needed later; 
 * by starting the computation early, you reduce the time you would have to 
 * wait later when you actually need the results.
 */
public class Preloader {
    ProductInfo loadProductInfo() throws DataLoadException {
        return null;
    }

    private final FutureTask<ProductInfo> future =
        new FutureTask<ProductInfo>(new Callable<ProductInfo>() {
            public ProductInfo call() throws DataLoadException {
                return loadProductInfo();                                      // Load product information from a database
            }
        });
    private final Thread thread = new Thread(future);                          // The thread in which the computation will be performed.

    public void start() { thread.start(); }

    public ProductInfo get() throws DataLoadException, InterruptedException {  // When the program later needs the ProductInfo, it can call get, which returns the loaded data if it is ready, or waits for the load to complete if not.
        try {
            return future.get();
        } catch (ExecutionException e) {
            Throwable cause = e.getCause();
            if (cause instanceof DataLoadException)
                throw (DataLoadException) cause;
            else
                throw LaunderThrowable.launderThrowable(cause);
        }
    }

    interface ProductInfo {
    }
}

class DataLoadException extends Exception { }
