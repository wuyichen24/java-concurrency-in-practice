package net.jcip.examples.ch06.webserver;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * ThreadPerTaskWebServer
 *
 * @list 6.2
 * @smell Good
 * @author Brian Goetz and Tim Peierls
 * 
 * <p>Web server creates a new thread for servicing each request.
 */
public class ThreadPerTaskWebServer {
    public static void main(String[] args) throws IOException {
        ServerSocket socket = new ServerSocket(80);
        while (true) {
            final Socket connection = socket.accept();
            Runnable task = new Runnable() {                         // The main loop creates a new thread to process the request
                public void run() {
                    handleRequest(connection);
                }
            };
            new Thread(task).start();
        }
    }

    private static void handleRequest(Socket connection) {
        // request-handling logic here
    }
}
