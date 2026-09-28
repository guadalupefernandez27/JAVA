package com.fastcash.core.model;

import java.util.List;

/**
 * "Foto" completa del estado del juego en un instante dado.
 * El server arma uno de estos por tick y lo manda a todos los clientes conectados.
 * El cliente solo usa esto para dibujar: nunca lo modifica.
 */
public class GameStateSnapshot {
    public List<RecursoCaido> recursos;
    public List<PlayerState> jugadores;
    public boolean juegoTerminado;
    public String moduloId;  // null hasta que algun jugador elige modulo (ver MessageType.MODULE_SELECT)

    // Mapa actual de LA PARTIDA (no de cada jugador): los 2 comparten la misma
    // pantalla/fondo/recursos, asi que el mapa tiene que ser uno solo. Avanza
    // segun el jugador que va ganando (ver GameEngine.actualizarMapaActual).
    public int mapaActual;

    public GameStateSnapshot() {
    }

    public GameStateSnapshot(List<RecursoCaido> recursos, List<PlayerState> jugadores,
                              boolean juegoTerminado, String moduloId, int mapaActual) {
        this.recursos = recursos;
        this.jugadores = jugadores;
        this.juegoTerminado = juegoTerminado;
        this.moduloId = moduloId;
        this.mapaActual = mapaActual;
    }
}
