package me.deejack.chatclient.utils;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import me.deejack.chatclient.message.ChatMessage;

/**
 * @author DeeJack
 */
public class JsonParser {
    private Gson gson;

    public JsonParser() {
        gson = new Gson();
    }

    public ChatMessage fromJson(String json) { // Get a ChatMessage instance with the json arguments
        try {
            return gson.fromJson(json, ChatMessage.class);
        } catch (JsonSyntaxException e) {
            System.err.println("Exception while parsing from json (Json: \n" + json);
            return null;
        }
    }

    public String toJson(ChatMessage message) { // Get the json from the class parameters
        try {
            return gson.toJson(message);
        } catch (JsonSyntaxException e) {
            System.err.println("Exception while parsing to json (class: \n" + e.getMessage());
            return null;
        }
    }
}
