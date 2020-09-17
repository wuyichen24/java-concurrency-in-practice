package net.jcip.examples.ch06.render;

import static net.jcip.examples.ch05.LaunderThrowable.launderThrowable;

import java.util.*;
import java.util.concurrent.*;

/**
 * FutureRenderer
 *
 * @list 6.13
 * @smell Good
 * @author Brian Goetz and Tim Peierls
 * 
 * <p>Example of rendering HTML elements and wait for image download with Future.
 * 
 * <p>Allows the text to be rendered concurrently with downloading the image data.
 * 
 * <p>Drawback: If rendering the text is much faster than downloading the images, as is entirely possible, 
 * the resulting performance is not much different from the sequential version, but the code is a lot more complicated.
 */
public abstract class FutureRenderer {
    private final ExecutorService executor = Executors.newCachedThreadPool();

    void renderPage(CharSequence source) {
        final List<ImageInfo> imageInfos = scanForImageInfo(source);
        Callable<List<ImageData>> task =                                       // Create a Callable to download all the images
                new Callable<List<ImageData>>() {
                    public List<ImageData> call() {
                        List<ImageData> result = new ArrayList<ImageData>();
                        for (ImageInfo imageInfo : imageInfos)
                            result.add(imageInfo.downloadImage());
                        return result;
                    }
                };

        Future<List<ImageData>> future = executor.submit(task);                // Submit the Callable to an ExecutorService, this returns a Future describing the task's execution
        renderText(source);

        try {
            List<ImageData> imageData = future.get();                          // When the main task gets to the point where it needs the images, it waits for the result by calling Future.get()
            for (ImageData data : imageData)
                renderImage(data);
        } catch (InterruptedException e) {                                     // Possible problem issued by Future.get(): The thread calling get was interrupted before the results were available.
            // Re-assert the thread's interrupted status
            Thread.currentThread().interrupt();
            // We don't need the result, so cancel the task too
            future.cancel(true);
        } catch (ExecutionException e) {                                       // Possible problem issued by Future.get(): The task encountered an Exception
            throw launderThrowable(e.getCause());
        }
    }

    interface ImageData {
    }

    interface ImageInfo {
        ImageData downloadImage();
    }

    abstract void renderText(CharSequence s);

    abstract List<ImageInfo> scanForImageInfo(CharSequence s);

    abstract void renderImage(ImageData i);
}
