package com.stylingandroid.nougat.messenger;

import com.google.auto.value.AutoValue;

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
}
