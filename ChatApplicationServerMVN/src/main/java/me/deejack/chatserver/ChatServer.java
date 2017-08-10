package me.deejack.chatserver;

import me.deejack.chatserver.database.MySql;
import me.deejack.chatserver.files.JsonParser;
import me.deejack.chatserver.files.XmlParser;
import me.deejack.chatserver.message.ChatMessage;
import me.deejack.chatserver.server.Server;
import me.deejack.chatserver.user.User;
import me.deejack.chatserver.utils.Date;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * @author DeeJack
 */
public class ChatServer {

    /*
    TODO: AntiSpam and AntiFlood, ban, dall'id risalire alla chat per mandare mex ecc
     */
    public static void main(String... args) throws IOException {
        if (Boolean.parseBoolean(args[0]))
            XmlParser.getParser().fillDatabase();
        else
            new MySql("root", "pass");

        Server server = Server.getServer(); /* Server instance */
        server.initialize(); /* Initialize the server socket */
        server.setUseFile(Boolean.parseBoolean(args[0]));
        new Thread(server).start(); /* Accept all the connection request */

        XmlParser.getParser().fillDatabase();

        /* Read from the command line and execute the command / broadcast the message */
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        String s;
        while (!server.isClosed() && (s = reader.readLine()) != null) {
            switch (s) {
                case "":
                    break;
                case ".close":
                    server.close();
                    System.exit(0);
                    break;
                default:
                    ChatMessage cm = new ChatMessage(s, "BROADCAST", new User("BROADCAST",
                            new Date().toString(), 2, null));
                    server.broadcast(new JsonParser().toJson(cm));
                    System.out.println(String.format("%s %s -> %s", cm.getPublishedAt(), cm.getSender().getUsername(), cm.getText()));
                    break;
            }
        }
    }
}
