package me.deejack.chatserver.server;

import me.deejack.chatserver.database.UserDatabase;
import me.deejack.chatserver.message.ChatMessage;
import me.deejack.chatserver.message.MessageListener;
import me.deejack.chatserver.user.User;
import me.deejack.chatserver.utils.Date;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @author DeeJack
 */
public class Server extends Thread {
    private static Server instance;
    private boolean useFile;
    private ServerSocket serverSocket;

    public static Server getServer() {
        if (instance == null)
            instance = new Server();
        return instance;
    }

    public void initialize() {
        try {
            serverSocket = new ServerSocket(8888) {
                protected void finalize() throws IOException {
                    this.close();
                }
            };
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        while (!isClosed()) {
            try {
                Socket socket = serverSocket.accept();
                new Thread(new MessageListener(socket)).start();
            } catch (IOException e) {
                System.err.println("Server closed");
            } // happens when server is closed
        }
    }

    public boolean isClosed() {
        return serverSocket.isClosed();
    }

    public void broadcast(String m) {
        for (User user : UserDatabase.getDatabase().values()) {
            if(user.getOutputStream() != null) {
                user.sendMessage(m);
                System.out.println(user.getUsername() + " ONLINE");
            }
        }
    }

    public void close() {
        try {
            serverSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setUseFile(boolean useFile) {
        this.useFile = useFile;
    }

    public boolean useFile() {
        return useFile;
    }
}
