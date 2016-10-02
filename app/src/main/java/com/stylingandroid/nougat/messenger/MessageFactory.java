package com.stylingandroid.nougat.messenger;

import com.google.gson.TypeAdapterFactory;
import com.ryanharter.auto.value.gson.GsonTypeAdapterFactory;

@GsonTypeAdapterFactory
abstract class MessageFactory implements TypeAdapterFactory {
    static TypeAdapterFactory create() {
        return new AutoValueGson_MessageFactory();
    }
}
