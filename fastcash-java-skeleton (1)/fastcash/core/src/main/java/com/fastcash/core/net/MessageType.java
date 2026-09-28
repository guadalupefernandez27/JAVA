package com.fastcash.core.net;

/**
 * Tipos de mensaje del protocolo. Van en el campo "type" de cada linea JSON
 * que viaja por el socket (un mensaje = una linea, terminada en '\n').
 */
public enum MessageType {
    JOIN,             // client -> server: "quiero entrar a la partida"
    JOIN_OK,          // server -> client: "listo, tu id de jugador es X"
    MODULE_SELECT,    // client -> server: payload = String con el id del modulo elegido.
                       // Gana el primero que llega (la partida es de un solo modulo compartido);
                       // el resultado se ve reflejado en GameStateSnapshot.moduloId para ambos.
    CHARACTER_SELECT,  // client -> server: payload = int (0 o 1), el personaje del modulo elegido.
                       // Se guarda en el PlayerState.personajeIndex de quien lo mando.
    INPUT,            // client -> server: InputState (teclas apretadas)
    STATE,            // server -> client: GameStateSnapshot
    LEVEL_CHANGE,     // reservado para uso futuro (hoy el cliente detecta el cambio de mapa
                       // el mismo, comparando PlayerState.mapaActual entre snapshots)
    GAME_OVER         // server -> client: fin de partida (gano/perdio)
}
