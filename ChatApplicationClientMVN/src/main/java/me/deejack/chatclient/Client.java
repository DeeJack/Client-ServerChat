package me.deejack.chatclient;

import me.deejack.chatclient.message.ChatMessage;
import me.deejack.chatclient.message.MessageListener;
import me.deejack.chatclient.socket.Connection;
import me.deejack.chatclient.user.User;
import me.deejack.chatclient.utils.Date;
import me.deejack.chatclient.utils.JsonParser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * @author DeeJack
 */
public class Client {
    private static BufferedReader sc;
    public static User u;

    public static void main(String... args) throws Exception {
        Connection connection = Connection.getConnection(); // Create the instance of the connection

        sc = new BufferedReader(new InputStreamReader(System.in)); // Create the reader for the console

        boolean b = true;
        while (b) {
            String username = getResponse("Insert username: "); // Get username
            String password = getResponse("Insert password: "); // Get password

            switch (getResponse("Login or register? ")) { // Choose login or register
                case "register":
                    ChatMessage c = new ChatMessage(".register", 1, "BROADCAST", (u = new User(username, new Date().toString(), password, 1)));
                    connection.write(new JsonParser().toJson(c)); // Write the message to the server
                    String s = connection.read();
                    if(!s.equals("Name has already been taken!")) // if true OK
                        b = false;
                    break;
                case "login":
                    ChatMessage response = new JsonParser().fromJson(connection.write(new JsonParser().toJson(new ChatMessage(".login", 1,
                            "BROADCAST", (u = new User(username, new Date().toString(), password, 1))))).read()); // Write user and password to server and take response
                    if (response.getText().equals("error")) // If response != error OK
                        System.out.println("Error: Username or password are wrong\n");
                    else
                        b = false;
                    break;
                case "close":
                    connection.close();
                    System.exit(0);
                default:
                    System.out.println("Unknown command, use:\nregister\nlogin");
                    break;
            }
        }

        MessageListener listener = new MessageListener(connection);
        Thread t = new Thread(listener);
        t.start(); // Start new thread for the message listener

        String s;
        String target;
        while ((target = getResponse("Insert target's name: [BROADCAST or username] ")) != null) { // Always true
            s = getResponse("Message: ");
            switch (s) {
                case ".close":
                    connection.close();
                    break;
                default:
                    ChatMessage cm = new ChatMessage(s, 2, target, u);
                    if(!cm.getTarget().equals("BROADCAST"))
                        System.out.println(String.format("%s - %s -> %s", cm.getPublishedAt(), cm.getSender().getUsername(), cm.getText()));
                    connection.write(new JsonParser().toJson(cm)); // Send json message to the server
                    break;
            }
        }
    }

    private static String getResponse(String answer) throws IOException {
        System.out.print(answer);
        return sc.readLine();
    }
}
