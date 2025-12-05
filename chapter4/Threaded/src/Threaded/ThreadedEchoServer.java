package Threaded;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class ThreadedEchoServer {
    public static void main(String[] args) {
        try (ServerSocket server_sock = new ServerSocket(3000)) {
            int i = 1;
            while (true) {
                Socket socket_incoming = server_sock.accept();
                System.out.println("Spawning %d.".formatted(i));
                Runnable r = new ThreadedEchoHandler(socket_incoming);
                new Thread(r).start();
                i++;
            }
        } catch (IOException e) { e.printStackTrace(); }
    }
}


class ThreadedEchoHandler implements Runnable {
    private Socket socket_conn;

    public ThreadedEchoHandler(Socket incoming_socket) {
        this.socket_conn = incoming_socket;
    }

    public void run() {
        try (
            InputStream inStream = socket_conn.getInputStream();
            OutputStream outStream = socket_conn.getOutputStream();
            Scanner in = new Scanner(inStream, StandardCharsets.UTF_8);
            PrintWriter out = new PrintWriter(new OutputStreamWriter(outStream, StandardCharsets.UTF_8), true)
        ) {
            out.println("Hello! Enter BYE to exit.");
            boolean done = false;
            while (!done && in.hasNextLine()) {
                String line = in.nextLine();
                out.println("Echo: %s".formatted(line));
                if (line.strip().equals("BYE")) done = true;
            }
        } catch (IOException e) { e.printStackTrace(); }
    }
}