package com.fastcash.client.net;

import com.fastcash.core.model.GameStateSnapshot;
import com.fastcash.core.model.InputState;
import com.fastcash.core.net.JsonCodec;
import com.fastcash.core.net.MessageType;
import com.fastcash.core.net.NetworkMessage;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Lado cliente del protocolo. Es "tonto" a proposito: no calcula nada de juego,
 * solo manda inputs y guarda el ultimo GameStateSnapshot que llego del server
 * para que la pantalla lo dibuje.
 */
public class GameClient {

    private final Socket socket;
    private final BufferedReader in;
    private final PrintWriter out;
    private final Thread listenerThread;

    private volatile String playerId;
    private final AtomicReference<GameStateSnapshot> ultimoSnapshot = new AtomicReference<>();
    private volatile boolean conectado = true;

    public GameClient(String host, int port) throws IOException {
        this.socket = new Socket(host, port);
        this.in = new BufferedReader(new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));
        this.out = new PrintWriter(socket.getOutputStream(), true);

        this.listenerThread = new Thread(this::loopDeEscucha, "game-client-listener");
        this.listenerThread.setDaemon(true);
        this.listenerThread.start();
    }

    private void loopDeEscucha() {
        try {
            String linea;
            while (conectado && (linea = in.readLine()) != null) {
                NetworkMessage envelope = JsonCodec.parseEnvelope(linea);
                if (envelope == null) continue;

                switch (envelope.type) {
                    case JOIN_OK:
                        this.playerId = JsonCodec.fromJson(envelope.payload, String.class);
                        break;
                    case STATE:
                        GameStateSnapshot snapshot = JsonCodec.fromJson(envelope.payload, GameStateSnapshot.class);
                        ultimoSnapshot.set(snapshot);
                        break;
                    default:
                        // LEVEL_CHANGE / GAME_OVER: la pantalla de juego los puede
                        // leer directo del snapshot (mapaActual, gano/vivo por jugador).
                        break;
                }
            }
        } catch (IOException e) {
            // Se corto la conexion; la pantalla de juego deberia mostrar un aviso.
        } finally {
            conectado = false;
        }
    }

    public void enviarInput(boolean left, boolean right) {
        if (playerId == null) return; // todavia no nos asignaron id
        InputState input = new InputState(playerId, left, right);
        out.println(JsonCodec.toJson(JsonCodec.wrap(MessageType.INPUT, input)));
    }

    /** Propone un modulo para la partida. Gana el primero que llegue al servidor. */
    public void enviarSeleccionModulo(String moduloId) {
        out.println(JsonCodec.toJson(JsonCodec.wrap(MessageType.MODULE_SELECT, moduloId)));
    }

    /** Elige uno de los 2 personajes del modulo ya confirmado (indice 0 o 1). */
    public void enviarSeleccionPersonaje(int indice) {
        out.println(JsonCodec.toJson(JsonCodec.wrap(MessageType.CHARACTER_SELECT, indice)));
    }

    public GameStateSnapshot ultimoEstado() {
        return ultimoSnapshot.get();
    }

    public String playerId() {
        return playerId;
    }

    public boolean estaConectado() {
        return conectado;
    }

    public void cerrar() {
        conectado = false;
        try {
            socket.close();
        } catch (IOException ignored) {
        }
    }
}
