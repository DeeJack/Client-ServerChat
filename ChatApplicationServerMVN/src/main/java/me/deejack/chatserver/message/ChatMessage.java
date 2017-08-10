package me.deejack.chatserver.message;

import me.deejack.chatserver.database.UserDatabase;
import me.deejack.chatserver.user.User;
import me.deejack.chatserver.utils.Date;
import me.deejack.chatserver.files.JsonParser;

/**
 * @author DeeJack
 */
public class ChatMessage {
    private String text;
    private int type;
    private String toUsername;
    private User sender;
    private String date;

    public ChatMessage(String text, String toUsername, User sender) {
        this.text = text;
        this.toUsername = toUsername;
        this.sender = sender;
        date = new Date().toString();
    }

    public String getText() {
        return text;
    }

    public String getTarget() {
        return toUsername;
    }

    public User getSender() {
        return sender;
    }

    public int getType() {
        return type;
    }

    public String getPublishedAt() {
        return date;
    }

    public String toString(User u) {
        return new JsonParser().toJson(this);
    }
}
