package com.fastcash.server;

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

/**
 * Un thread por cliente conectado. Hace dos cosas, cada una en su propio "canal":
 *  1) Lee INPUT del cliente en loop y se lo pasa al GameEngine (hilo lector).
 *  2) Expone enviarEstado(...) para que el loop del server le mande el STATE (hilo escritor,
 *     en realidad el mismo objeto pero el metodo se llama desde afuera, desde GameServer).
 *
 * Un mensaje = una linea de texto JSON terminada en '\n'.
 */
public class ClientHandler implements Runnable {

    private final Socket socket;
    private final GameEngine engine;
    private final String playerId;
    private final Runnable onDisconnect;

    private BufferedReader in;
    private PrintWriter out;

    public ClientHandler(Socket socket, GameEngine engine, String playerId, Runnable onDisconnect) {
        this.socket = socket;
        this.engine = engine;
        this.playerId = playerId;
        this.onDisconnect = onDisconnect;
    }

    @Override
    public void run() {
        try {
            in = new BufferedReader(new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));
            out = new PrintWriter(socket.getOutputStream(), true);

            // Avisamos al cliente cual es su playerId asignado.
            enviar(JsonCodec.wrap(MessageType.JOIN_OK, playerId));

            String linea;
            while ((linea = in.readLine()) != null) {
                NetworkMessage envelope = JsonCodec.parseEnvelope(linea);
                if (envelope == null) continue;

                switch (envelope.type) {
                    case INPUT -> {
                        InputState input = JsonCodec.fromJson(envelope.payload, InputState.class);
                        engine.recibirInput(input);
                    }
                    case MODULE_SELECT -> {
                        String moduloId = JsonCodec.fromJson(envelope.payload, String.class);
                        engine.elegirModulo(moduloId); // si ya habia uno elegido, esto no hace nada
                    }
                    case CHARACTER_SELECT -> {
                        int indice = JsonCodec.fromJson(envelope.payload, Integer.class);
                        engine.elegirPersonaje(playerId, indice);
                    }
                    default -> {
                        // JOIN / JOIN_OK / STATE / LEVEL_CHANGE / GAME_OVER no se esperan
                        // en este sentido (cliente -> servidor); se ignoran si llegan.
                    }
                }
            }
        } catch (IOException e) {
            // Conexion cerrada o error de red: se trata igual que una desconexion.
        } finally {
            desconectar();
        }
    }

    public void enviarEstado(GameStateSnapshot snapshot) {
        enviar(JsonCodec.wrap(MessageType.STATE, snapshot));
    }

    private void enviar(NetworkMessage message) {
        if (out == null) return;
        out.println(JsonCodec.toJson(message));
    }

    private void desconectar() {
        try {
            socket.close();
        } catch (IOException ignored) {
        }
        engine.quitarJugador(playerId);
        onDisconnect.run();
    }
}
