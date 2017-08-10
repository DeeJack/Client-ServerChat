package me.deejack.chatserver.files;

import com.google.gson.Gson;
import me.deejack.chatserver.message.ChatMessage;

/**
 * @author DeeJack
 */
public class JsonParser {
    private Gson gson;

    public JsonParser() {
        gson = new Gson();
    }

    public ChatMessage fromJson(String json) {
        return gson.fromJson(json, ChatMessage.class);
    }

    public String toJson(ChatMessage message) {
        return gson.toJson(message);
    }
}
