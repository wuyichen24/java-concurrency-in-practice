package net.jcip.examples.ch06.webserver;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.*;

/**
 * TaskExecutionWebServer
 * 
 * @list 6.4
 * @smell Good
 * @author Brian Goetz and Tim Peierls
 * 
 * <p>Web server using a thread pool.
 */
public class TaskExecutionWebServer {
    private static final int NTHREADS = 100;
    private static final Executor exec = Executors.newFixedThreadPool(NTHREADS);         // A fixed-size thread pool with 100 threads.

    public static void main(String[] args) throws IOException {
        ServerSocket socket = new ServerSocket(80);
        while (true) {
            final Socket connection = socket.accept();
            Runnable task = new Runnable() {
                public void run() {
                    handleRequest(connection);
                }
            };
            exec.execute(task);                                                          // Let the executor executes the task.
        }
    }

    private static void handleRequest(Socket connection) {
        // request-handling logic here
    }
}
