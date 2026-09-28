package com.fastcash.core.model;

/**
 * Estado de un jugador (equivalente a la clase Personaje del Python,
 * mas el contador de recursos y la energia que en el Python eran variables globales).
 *
 * El mapa actual NO vive aca: es compartido por toda la partida
 * (ver GameStateSnapshot.mapaActual), asi que este jugador y el otro siempre
 * ven el mismo fondo/recursos aunque tengan puntajes distintos.
 */
public class PlayerState {
    public String playerId;
    public float x;
    public float y;
    public int puntaje;
    public int energia;
    public int personajeIndex;  // 0 = personaje masculino, 1 = femenino; -1 = todavia no eligio
    public boolean vivo;
    public boolean gano;

    public PlayerState() {
    }

    public PlayerState(String playerId) {
        this.playerId = playerId;
        this.x = com.fastcash.core.GameConfig.PLAYER_START_X;
        this.y = com.fastcash.core.GameConfig.PLAYER_START_Y;
        this.puntaje = 0;
        this.energia = com.fastcash.core.GameConfig.ENERGIA_MAXIMA;
        this.personajeIndex = -1;
        this.vivo = true;
        this.gano = false;
    }
}
