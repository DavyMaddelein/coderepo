/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.compomics.coderepo.compressedstreams;

/**
 * Simple demonstration of the CompressedBlockOutputStream and
 * CompressedBlockInputStream transmitting data across a socket.
 *
 * $Id: Demo.java,v 1.1 2005/10/26 17:40:19 isenhour Exp $
 */
import java.io.*;
import java.net.*;

public class Demo {
    private static class Server extends Thread {
        int port;
        public Server(int port) {
            this.port = port;
            setDaemon(true);
            start();
        }

        public void run() {
            try {
                // Accept connections, spawning a worker thread
                // when we get one.
                ServerSocket ss = new ServerSocket(port);
                while (true) {
                    ServerWorker worker =
                        new ServerWorker(ss.accept());
                }
            }
            catch(IOException ioe) {
                ioe.printStackTrace();
            }
        }
    }

    private static class ServerWorker extends Thread {
        Socket s = null;
        public ServerWorker(Socket s) {
            this.s = s;
            setDaemon(false);
            start();
        }

        public void run() {
            try {
                // Build a Reader object that wraps the
                // (decompressed) socket input stream.
                BufferedReader in = new BufferedReader(
                    new InputStreamReader(
                    new CompressedInputStream(
                    s.getInputStream())));
                String line = in.readLine();
                while (line != null) {
                    System.out.println(line);
                    line = in.readLine();
                }
                System.out.flush();
            }
            catch(IOException ioe) {
                ioe.printStackTrace();
            }
        }
    }

    public static void sendData(int port) throws IOException {
        // Connect to the server
        Socket s = new Socket("localhost", port);

        // Make a stream that compresses outgoing data,
        // sending a compressed block for every 1K worth of
        // uncompressed data written.
        CompressedOutputStream compressed =
            new CompressedOutputStream(
                s.getOutputStream(), 1024);

        // Build a writer that wraps the (compressed) socket
        // output stream
        PrintWriter out =
            new PrintWriter(new OutputStreamWriter(compressed));

        // Send across 1000 lines of output
        for (int i = 0; i < 1000; i++) {
            out.println("This is line " + (i + 1) +
                " of the test output.");
        }

        // Note that if we don't close the stream, the last
        // block of data may not be sent across the connection. We
        // could also force the last block to be sent by calling
        // flush(), which would leave the socket connection open.
        out.close();
    }

    public static void main(String[] args) throws IOException {
        // Pick an obscure default port. An alternate port
        // can be given as the first command line argument.
        int port = 11535;
        if (args.length > 0) {
            port = Integer.parseInt(args[0]);
        }

        Server s = new Server(port);

        sendData(port);
    }
}
