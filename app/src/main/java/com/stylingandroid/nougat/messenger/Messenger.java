package com.stylingandroid.nougat.messenger;

import android.content.Context;

import com.stylingandroid.nougat.R;

import java.util.Random;

final class Messenger {
    static final String SENDER = "Styling Android";

    private final String[] phrases;
    private final Random random;

    static Messenger newInstance(Context context) {
        String[] phrases = context.getResources().getStringArray(R.array.phrases);
        Random random = new Random();
        return new Messenger(phrases, random);
    }

    private Messenger(String[] phrases, Random random) {
        this.phrases = phrases;
        this.random = random;
    }

    Message generateNewMessage() {
        return Message.builder()
                .message(getRandomPhrase())
                .sender(SENDER)
                .timestamp(System.currentTimeMillis())
                .build();
    }

    String getRandomPhrase() {
        int index = random.nextInt(phrases.length);
        return phrases[index];
    }
}
