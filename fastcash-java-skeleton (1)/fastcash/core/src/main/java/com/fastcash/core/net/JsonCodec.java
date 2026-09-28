package com.fastcash.core.net;

import com.google.gson.Gson;

/**
 * Un unico Gson compartido para serializar/deserializar todo el protocolo.
 * Asi evitamos crear un Gson nuevo en cada clase de server y client.
 */
public final class JsonCodec {

    private static final Gson GSON = new Gson();

    private JsonCodec() {}

    public static String toJson(Object obj) {
        return GSON.toJson(obj);
    }

    public static <T> T fromJson(String json, Class<T> clazz) {
        return GSON.fromJson(json, clazz);
    }

    public static NetworkMessage wrap(MessageType type, Object payloadObj) {
        return new NetworkMessage(type, GSON.toJson(payloadObj));
    }

    public static NetworkMessage parseEnvelope(String line) {
        return GSON.fromJson(line, NetworkMessage.class);
    }
}
