package com.fastcash.server;

import com.fastcash.core.model.InputState;
import com.fastcash.core.model.PlayerState;

/**
 * Todo lo que el server necesita saber de UN jugador conectado:
 * su estado de juego (PlayerState) + su ultimo input recibido + el handler de su socket.
 */
public class PlayerSession {
    public final String playerId;
    public final PlayerState state;
    public volatile InputState lastInput;
    public ClientHandler handler;

    public PlayerSession(String playerId) {
        this.playerId = playerId;
        this.state = new PlayerState(playerId);
        this.lastInput = new InputState(playerId, false, false);
    }
}
