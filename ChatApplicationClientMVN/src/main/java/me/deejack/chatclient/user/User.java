package me.deejack.chatclient.user;

/**
 * @author DeeJack
 */
public class User {
    private String username;
    private String creation;
    private String password;
    private int type;

    public User(String username, String creation, String password, int type) {
        this.username = username;
        this.creation = creation;
        this.password = password;
        this.type = type;
    }

    public String getCreation() {
        return creation;
    }

    public String getUsername() {
        return username;
    }

    public int getType() {
        return type;
    }

    public String getPassword() {
        return password;
    }

    @Override
    public boolean equals(Object otherUser) {
        return ((User) otherUser).getUsername().equals(getUsername());
    }

    @Override
    public String toString() {
        return username;
    }
}
