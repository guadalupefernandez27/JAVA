package com.fastcash.core.model;

/**
 * Lo unico que el cliente le manda al servidor: que teclas estan apretadas.
 * Nada de posiciones ni de logica de juego viaja en este sentido (cliente "tonto").
 */
public class InputState {
    public String playerId;
    public boolean left;
    public boolean right;

    public InputState() {
    }

    public InputState(String playerId, boolean left, boolean right) {
        this.playerId = playerId;
        this.left = left;
        this.right = right;
    }
}
