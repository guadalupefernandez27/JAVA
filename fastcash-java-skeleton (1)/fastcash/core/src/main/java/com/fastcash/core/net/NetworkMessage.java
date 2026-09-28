package com.fastcash.core.net;

/**
 * Sobre generico del protocolo: un tipo + un payload en JSON (como texto).
 * Se manda una linea por mensaje. Ejemplo real sobre el cable:
 *   {"type":"INPUT","payload":"{\"playerId\":\"p1\",\"left\":true,\"right\":false}"}
 */
public class NetworkMessage {
    public MessageType type;
    public String payload;

    public NetworkMessage() {
    }

    public NetworkMessage(MessageType type, String payload) {
        this.type = type;
        this.payload = payload;
    }
}
