package net.jcip.examples.ch08;

import java.util.concurrent.*;

/**
 * ThreadDeadlock
 *
 * @list 8.1
 * @smell Bad
 * @author Brian Goetz and Tim Peierls
 * 
 * <p>Example to illustrates thread starvation deadlock.
 */
public class ThreadDeadlock {
    ExecutorService exec = Executors.newSingleThreadExecutor();                // This is a single-thread executor

    public class LoadFileTask implements Callable<String> {
        private final String fileName;

        public LoadFileTask(String fileName) {
            this.fileName = fileName;
        }

        public String call() throws Exception {
            // Here's where we would actually read the file
            return "";
        }
    }

    public class RenderPageTask implements Callable<String> {
        public String call() throws Exception {
            Future<String> header, footer;
            header = exec.submit(new LoadFileTask("header.html"));
            footer = exec.submit(new LoadFileTask("footer.html"));
            String page = renderBody();          
            return header.get() + page + footer.get();                         // Will always deadlock - The page task waiting for result of dependent subtasks: the header task and the footer task.
        }

        private String renderBody() {
            // Here's where we would actually render the page
            return "";
        }
    }
}
