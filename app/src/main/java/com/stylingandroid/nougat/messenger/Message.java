package com.stylingandroid.nougat.messenger;

import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;

@AutoValue
abstract class Message {
    public abstract String sender();

    public abstract String message();

    public abstract long timestamp();

    static Builder builder() {
        return new AutoValue_Message.Builder();
    }

    @AutoValue.Builder
    abstract static class Builder {
        abstract Builder sender(String sender);

        abstract Builder message(String message);

        abstract Builder timestamp(long timestamp);

        abstract Message build();
    }

    public static TypeAdapter<Message> typeAdapter(Gson gson) {
        return new AutoValue_Message.GsonTypeAdapter(gson);
    }
}
