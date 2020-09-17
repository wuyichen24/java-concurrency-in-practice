package net.jcip.examples.ch06.render;

import java.util.*;

/**
 * SingleThreadRendere
 * 
 * @list 6.10
 * @smell Good
 * @author Brian Goetz and Tim Peierls
 * 
 * <p>Example of rendering HTML elements sequentially.
 * 
 * <p>Drawback: Downloading an image mostly involves waiting for I/O to complete, 
 * and during this time the CPU does little work. So the sequential approach may underutilize the CPU.
 */
public abstract class SingleThreadRenderer {
    void renderPage(CharSequence source) {
        renderText(source);                                          // First pass, render the text elements first
        List<ImageData> imageData = new ArrayList<ImageData>();
        for (ImageInfo imageInfo : scanForImageInfo(source))         // Second pass, render the image elements (download the images and draw them into the associated placeholder)
            imageData.add(imageInfo.downloadImage());
        for (ImageData data : imageData)
            renderImage(data);
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
