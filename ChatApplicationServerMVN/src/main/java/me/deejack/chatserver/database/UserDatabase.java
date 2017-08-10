package me.deejack.chatserver.database;

import me.deejack.chatserver.user.User;

import java.util.*;

/**
 * @author DeeJack
 */
public class UserDatabase {
    private static UserDatabase instance;
    private static Map<String, User> users;

    private UserDatabase() {
        users = new HashMap<>();
    }

    public static UserDatabase getDatabase() {
        if (instance == null)
            instance = new UserDatabase();
        return instance;
    }

    public synchronized void add(User u) {
        users.put(u.getUsername(), u);
    }

    public synchronized void remove(User u) {
        users.remove(u.getUsername());
    }

    public User getUser(String username) {
        return users.get(username);
    }

    public Collection<User> values() {
        return users.values();
    }
}
