package me.deejack.chatserver.message;

import me.deejack.chatserver.database.UserDatabase;
import me.deejack.chatserver.files.FileLogger;
import me.deejack.chatserver.server.Server;
import me.deejack.chatserver.user.User;
import me.deejack.chatserver.files.JsonParser;
import me.deejack.chatserver.utils.Date;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

/**
 * @author DeeJack
 */
public class MessageListener extends Thread {
    private Socket client;
    private User user;
    private FileLogger logger;

    public MessageListener(Socket client) {
        this.client = client;
        logger = FileLogger.getLogger();
    }

    @Override
    public void run() {
        String s;
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(client.getInputStream()));
            while (!client.isClosed() && (s = reader.readLine()) != null) {
                ChatMessage m = new JsonParser().fromJson(s);

                if (!(m.getType() == 1 && (m.getText().equals(".register") || m.getText().equals(".login"))) && user == null)
                    sendError("YOU ARE NOT LOGGED IN!");

                logger.log("log", m.toString(user));

                if (m.getType() == 1) {
                    switch (m.getText()) {
                        case ".register":
                            if (UserDatabase.getDatabase().getUser(m.getSender().getUsername()) == null) {
                                user = User.register(m.getSender().getUsername(), m.getSender().getPassword(), User.USER, client.getOutputStream());
                                user.sendMessage("SUCCESS");
                            } else
                                sendError("Name has already been taken!");
                            break;
                        case ".login":
                            if (m.getSender() == null)
                                sendError("User " + m.getSender().getUsername() + " doesn't exists!");
                            else {
                                if (m.getSender().getPassword().equals(UserDatabase.getDatabase().getUser(m.getSender().getUsername()).getPassword())) {
                                    user = m.getSender();
                                    user.setOutputStream(client.getOutputStream());
                                    UserDatabase.getDatabase().getUser(user.getUsername()).setOutputStream(client.getOutputStream());
                                    String z = new JsonParser().toJson(new ChatMessage("SUCCESS",  "BROADCAST",
                                            new User("BROADCAST", new Date().toString(), 2, null)));
                                    user.sendMessage(z);
                                } else
                                    sendError("Username or password wrong");
                            }
                            break;
                        case ".onlineList":
                            StringBuilder builder = new StringBuilder("");
                            for (User u : UserDatabase.getDatabase().values())
                                builder.append(u.getUsername()).append(", ");
                            user.sendMessage(builder.toString());
                            break;
                        default:
                            user.sendMessage("Unrecognised command (or you don't have permissions)! -> " + m.getText());
                            break;
                    }
                } else {
                    System.out.println(String.format("[%s] %s %s -> %s",
                            m.getTarget(), m.getPublishedAt(), m.getSender().getUsername(), m.getText()));
                    if (m.getTarget().equals("BROADCAST")) {
                        Server.getServer().broadcast(s);
                    } else { // Messaggio ad un user
                        try {
                            UserDatabase.getDatabase().getUser(m.getTarget()).sendMessage(s);
                        } catch (NullPointerException npe) {
                            m.getSender().sendMessage("Client offline");
                        }
                    }
                }
            }
        } catch (IOException e) { } // Client offline
    }

    private void sendError(final String error) {
        try {
            client.getOutputStream().write((error + "\r\n").getBytes());
            client.getOutputStream().flush();
            client.close();
            currentThread().interrupt();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
