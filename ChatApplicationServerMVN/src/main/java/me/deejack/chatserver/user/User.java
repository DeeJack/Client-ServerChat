package me.deejack.chatserver.user;

import me.deejack.chatserver.database.MySql;
import me.deejack.chatserver.database.UserDatabase;
import me.deejack.chatserver.files.FileLogger;
import me.deejack.chatserver.files.XmlParser;
import me.deejack.chatserver.server.Server;
import me.deejack.chatserver.utils.Date;
import me.deejack.chatserver.utils.Encrypter;

import java.io.IOException;
import java.io.OutputStream;

/**
 * @author DeeJack
 */
public class User {
    public static final int ADMIN = 0, USER = 1;

    private String username;
    private String password;
    private String creation;
    private int type;

    private OutputStream outputStream;
    private String key;

    public User(String username, String creation, int type, OutputStream outputStream) {
        this.username = username;
        this.creation = creation;
        this.outputStream = outputStream;
        this.type = type;
    }

    public static User register(String username, String password, int type, OutputStream outputStream) {
        User u = new User(username, new Date().toString(), type, outputStream);
        Encrypter.Password psw = cryptPassword(password);
        u.setPassword(psw.getPassword());
        u.setKey(psw.getKey());
        u.save();
        UserDatabase.getDatabase().add(u);
        return u;
    }

    public static Encrypter.Password cryptPassword(String password) {
        try {
            return new Encrypter("asdasdasdASFASD", password).encrypt();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public String getCreation() {
        return creation;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getUsername() {
        return username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    public String getKey() {
        return key;
    }

    public void sendMessage(String msg) {
        try {
            outputStream.write((msg + "\r\n").getBytes());
            outputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public OutputStream getOutputStream() {
        return outputStream;
    }

    public int getType() {
        return type;
    }

    public void setOutputStream(OutputStream outputStream) {
        this.outputStream = outputStream;
    }

    public void save() {
        if(Server.getServer().useFile())
            XmlParser.getParser().add(this);
        else
            new MySql("", "").addUser(this, "");
    }

    @Override
    public String toString() {
        return username;
    }
}
