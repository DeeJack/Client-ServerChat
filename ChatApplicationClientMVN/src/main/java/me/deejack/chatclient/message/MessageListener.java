package me.deejack.chatclient.message;

import me.deejack.chatclient.socket.Connection;
import me.deejack.chatclient.utils.JsonParser;

import java.io.BufferedReader;

/**
 * @author DeeJack
 */
public class MessageListener extends Thread {
    private Connection connection;

    public MessageListener(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void run() {
        try {
            messages();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void messages() throws Exception {
        String s;
        BufferedReader reader = connection.getReader();
        while (!connection.getSocket().isClosed() && (s = reader.readLine()) != null) {
            ChatMessage m = new JsonParser().fromJson(s);
            if (m.getType() == 1) { // If the message is a command then....
                switch (m.getText()) { // TODO: send screenshot
                    default:
                        break;
                }
            } else // Message is a normal message
                System.out.println(String.format("%s - %s -> %s", m.getPublishedAt(), m.getSender().getUsername(), m.getText()));
        }
    }
}
