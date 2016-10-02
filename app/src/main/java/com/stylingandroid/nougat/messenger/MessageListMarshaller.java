package com.stylingandroid.nougat.messenger;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

final class MessageListMarshaller {
    private static final Type MESSAGE_LIST_TYPE = new TypeToken<List<Message>>() { }.getType();
    private final Gson gson;

    static MessageListMarshaller newInstance() {
        Gson gson = new GsonBuilder().registerTypeAdapterFactory(MessageFactory.create()).create();
        return new MessageListMarshaller(gson);
    }

    private MessageListMarshaller(Gson gson) {
        this.gson = gson;
    }

    List<Message> decode(String messageListString) {
        return gson.fromJson(messageListString, MESSAGE_LIST_TYPE);
    }

    String encode(List<Message> messageList) {
        return gson.toJson(messageList);
    }
}
