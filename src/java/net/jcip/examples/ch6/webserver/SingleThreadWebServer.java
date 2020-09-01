package net.jcip.examples.ch6.webserver;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * SingleThreadWebServer
 *
 * @list 6.1
 * @smell Good
 * @author Brian Goetz and Tim Peierls
 * 
 * <p>Web server handles only one request at a time.
 */

public class SingleThreadWebServer {
    public static void main(String[] args) throws IOException {
        ServerSocket socket = new ServerSocket(80);
        while (true) {
            Socket connection = socket.accept();
            handleRequest(connection);
        }
    }

    private static void handleRequest(Socket connection) {
        // request-handling logic here
    }
}
