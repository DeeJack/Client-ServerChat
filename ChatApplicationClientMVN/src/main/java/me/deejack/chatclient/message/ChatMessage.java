package me.deejack.chatclient.message;

import me.deejack.chatclient.user.User;
import me.deejack.chatclient.utils.Date;
import me.deejack.chatclient.utils.JsonParser;

/**
 * @author DeeJack
 */
public class ChatMessage {
    private String text;
    private int type;
    private String toUsername;
    private User sender;
    private String date;

    public ChatMessage(String text, int type, String target, User sender) {
        this.text = text;
        this.toUsername = target;
        this.sender = sender;
        this.type = type;
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

    public String toString() {
        return new JsonParser().toJson(this);
    }
}
