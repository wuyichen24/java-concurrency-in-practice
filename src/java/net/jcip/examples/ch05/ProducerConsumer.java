package net.jcip.examples.ch05;

import java.io.File;
import java.io.FileFilter;
import java.util.concurrent.*;

/**
 * ProducerConsumer
 * 
 * @list 5.8
 * @list 5.9
 * @smell Good
 * @author Brian Goetz and Tim Peierls
 * 
 * <p>Producer and consumer tasks in a desktop search application.
 */
public class ProducerConsumer {
    /**
     * A producer task that searches a file hierarchy for files meeting an indexing criterion and puts their names on the work queue
     */
    static class FileCrawler implements Runnable {
        private final BlockingQueue<File> fileQueue;
        private final FileFilter fileFilter;
        private final File root;

        public FileCrawler(BlockingQueue<File> fileQueue,
                           final FileFilter fileFilter,
                           File root) {
            this.fileQueue = fileQueue;
            this.root = root;
            this.fileFilter = new FileFilter() {
                public boolean accept(File f) {
                    return f.isDirectory() || fileFilter.accept(f);
                }
            };
        }

        private boolean alreadyIndexed(File f) {
            return false;
        }

        public void run() {
            try {
                crawl(root);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

        private void crawl(File root) throws InterruptedException {
            File[] entries = root.listFiles(fileFilter);
            if (entries != null) {
                for (File entry : entries)
                    if (entry.isDirectory())
                        crawl(entry);
                    else if (!alreadyIndexed(entry))
                        fileQueue.put(entry);
            }
        }
    }

    /**
     * A consumer task that takes file names from the queue and indexes them.
     */
    static class Indexer implements Runnable {
        private final BlockingQueue<File> queue;

        public Indexer(BlockingQueue<File> queue) {
            this.queue = queue;
        }

        public void run() {
            try {
                while (true)                                                   // The consumer threads never exit, which prevents the program from terminating.
                    indexFile(queue.take());
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

        public void indexFile(File file) {
            // Index the file...
        };
    }

    private static final int BOUND = 10;
    private static final int N_CONSUMERS = Runtime.getRuntime().availableProcessors();

    public static void startIndexing(File[] roots) {
        BlockingQueue<File> queue = new LinkedBlockingQueue<File>(BOUND);
        FileFilter filter = new FileFilter() {
            public boolean accept(File file) {
                return true;
            }
        };

        for (File root : roots)
            new Thread(new FileCrawler(queue, filter, root)).start();          // Searches a file hierarchy for files meeting an indexing criterion and puts their names on the work queue

        for (int i = 0; i < N_CONSUMERS; i++)
            new Thread(new Indexer(queue)).start();                            // Takes file names from the queue and indexes them.
    }
}
