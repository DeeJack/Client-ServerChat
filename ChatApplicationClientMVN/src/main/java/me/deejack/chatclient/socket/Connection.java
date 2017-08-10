package me.deejack.chatclient.socket;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Timer;
import java.util.TimerTask;

/**
 * @author DeeJack
 */
public class Connection {
    private static final String CRLF = "\r\n";

    private static Connection instance;
    private String hostname;
    private int port;
    private Socket socket;
    private OutputStream outputStream;
    private BufferedReader reader;

    private Connection(String hostname, int port) { // more than 1 socket can create error!
        this.hostname = hostname;
        this.port = port;
        openConnection();
    }

    public static Connection getConnection() {
        if (instance == null)
            instance = new Connection("localhost", 8888);
        return instance;
    }

    private void openConnection() {
        try {
            Long ping = ping();
            if (ping != null) {
                socket = new Socket(hostname, port);
                outputStream = socket.getOutputStream();
                reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                System.out.println("Connected to: " + hostname + ":" + port + " ping: " + ping + "ms");
            } else {
                new Timer().schedule(new TimerTask() {
                    @Override
                    public void run() {
                        System.out.println("Connection refused, retrying every 1000 ms");
                        openConnection();
                    }
                }, 1000);
            }
        } catch (IOException e) {
            System.err.println("Oh shit, connection refused. Possible reason:\n- Hostname or port are wrong\n- Server is offline or in maintenance\n- Firewall block the connection");
            System.exit(1);
        }
    }

    private Long ping() {
        long start = System.currentTimeMillis();
        try {
            InetAddress.getByName(hostname).isReachable(port);
        } catch (IOException e) {
            return null;
        }
        return System.currentTimeMillis() - start;
    }

    public String read() {
        try {
            return reader.readLine();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public Connection write(String message) { // write a message
        try {
            outputStream.write((message + CRLF).getBytes());
            outputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return this;
    }

    public void close() { // Close the connection
        try {
            write(".close");
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public BufferedReader getReader() {
        return reader;
    }

    public Socket getSocket() {
        return socket;
    }
}
