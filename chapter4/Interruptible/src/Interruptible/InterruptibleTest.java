package Interruptible;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class InterruptibleTest {
    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            InterruptibleSocketFrame frame = new InterruptibleSocketFrame();
            frame.setTitle("InterruptibleSocketTest");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setVisible(true);
        });
    }
}

class InterruptibleSocketFrame extends JFrame {
    private Scanner in;
    private JButton interruptibleButton;
    private JButton blockingButton;
    private JButton cancelButton;
    private JTextArea messages;
    private TestServer server;
    private Thread connectThread;

    public InterruptibleSocketFrame() {
        JPanel northPanel = new JPanel();
        super.add(northPanel, BorderLayout.NORTH);

        final int TEXT_ROWS = 20;
        final int TEXT_COLUMNS = 60;
        this.messages = new JTextArea(TEXT_ROWS, TEXT_COLUMNS);
        super.add(new JScrollPane(this.messages));

        this.interruptibleButton = new JButton("Interruptible");
        this.blockingButton = new JButton("Blocking");
        northPanel.add(this.interruptibleButton);
        northPanel.add(this.blockingButton);

        this.interruptibleButton.addActionListener(event -> {
            this.interruptibleButton.setEnabled(false);
            this.blockingButton.setEnabled(false);
            this.cancelButton.setEnabled(true);
            connectThread = new Thread(() -> {
                try {
                    this.connectInterruptibly();
                } catch (Exception e) { this.messages.append("\nInterruptibleSocketTest.connectInterruptibly: " + e); }
            });
            connectThread.start();
        });
        
        this.blockingButton.addActionListener(event -> {
            this.interruptibleButton.setEnabled(false);
            this.blockingButton.setEnabled(false);
            this.cancelButton.setEnabled(true);
            this.connectThread = new Thread(() -> {
                try {
                    this.connectBlocking();
                } catch (Exception e) { this.messages.append("\nInterruptibleSocketTest.connectBlocking: " + e); }
            });
            this.connectThread.start();
        });

        this.cancelButton = new JButton("Cancel");
        this.cancelButton.setEnabled(false);
        northPanel.add(this.cancelButton);
        this.cancelButton.addActionListener(event -> {
            this.connectThread.interrupt();
            this.cancelButton.setEnabled(false);
        });

        this.server = new TestServer();
        new Thread(server).start();
        pack();
    }

    public void connectInterruptibly() throws IOException {
        this.messages.append("Interruptible:\n");
        try (SocketChannel channel = SocketChannel.open(new InetSocketAddress("localhost", 3000))) {
            this.in = new Scanner(channel, StandardCharsets.UTF_8);
            while (!Thread.currentThread().isInterrupted()) {
                this.messages.append("Reading: ");
                if (this.in.hasNextLine()) {
                    String line = this.in.nextLine();
                    this.messages.append(line + "\n");
                }
            }
        } finally {
            EventQueue.invokeLater(() -> {
                this.messages.append("Channel closed.\n");
                this.interruptibleButton.setEnabled(true);
                this.blockingButton.setEnabled(true);
            });
        }
    }

    public void connectBlocking() throws IOException {
        this.messages.append("Blocking:\n");
        try (Socket socket = new Socket("localhost", 3000)) {
            this.in = new Scanner(socket.getInputStream(), StandardCharsets.UTF_8);
            while (!Thread.currentThread().isInterrupted()) {
                this.messages.append("Reading: ");
                if (this.in.hasNextLine()) {
                    String line = this.in.nextLine();
                    this.messages.append(line + "\n");
                }
            }
        } finally {
            EventQueue.invokeLater(() -> {
                this.messages.append("Socket closed.\n");
                this.interruptibleButton.setEnabled(true);
                this.blockingButton.setEnabled(true);
            });
        }
    }

    class TestServer implements Runnable {
        public void run() {
            try (ServerSocket s = new ServerSocket(3000)) {
                while (true) {
                    Socket socket = s.accept();
                    Runnable r = new TestServerHandler(socket);
                    new Thread(r).start();
                }
            } catch (IOException e) {
                messages.append("\nTestServer.run: %s".formatted(e));
            }
        }
    }

    class TestServerHandler implements Runnable {
        private Socket socket_in;
        private int counter = 0;

        public TestServerHandler(Socket socket_in) {
            this.socket_in = socket_in;
        }

        public void run() {
            try {
                try {
                    OutputStream outStream = this.socket_in.getOutputStream();
                    PrintWriter out = new PrintWriter(new OutputStreamWriter(outStream, StandardCharsets.UTF_8), true);
                    while (this.counter < 100) {
                        this.counter++;
                        if (this.counter <= 10) out.println(counter);
                        Thread.sleep(100);
                    }
                } finally {
                    socket_in.close();
                    messages.append("Closing Server.\n");
                }
            } catch (Exception e) {
                messages.append("\nTestServerHandler.run: %s".formatted(e));
            }
        }
    }
}
