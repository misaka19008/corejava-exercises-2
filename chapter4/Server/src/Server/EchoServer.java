package Server;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class EchoServer {
    public static void main(String[] args) throws IOException {
        try (ServerSocket serversock = new ServerSocket(3000)) {
            try (Socket socket_in = serversock.accept()) {
                InputStream inStream = socket_in.getInputStream();
                OutputStream outStream = socket_in.getOutputStream();
                try (Scanner in = new Scanner(inStream, StandardCharsets.UTF_8)) {
                    PrintWriter out = new  PrintWriter(new OutputStreamWriter(outStream, StandardCharsets.UTF_8), true);
                    out.println("Hello! Enter BYE to exit.");

                    boolean done = false;
                    while (!done && in.hasNextLine()) {
                        String line = in.nextLine();
                        out.println("Echo: %s".formatted(line));
                        if (line.strip().equals("BYE")) done = true;
                    }
                }
            }
        }
    }
}
