package com.fastcash.server;

import com.fastcash.core.GameConfig;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Orquesta todo del lado servidor:
 *  - acepta conexiones entrantes (hasta MAX_PLAYERS)
 *  - crea un ClientHandler (thread) por jugador
 *  - corre el GameEngine a tick fijo y broadcastea el snapshot a todos
 *
 * Esto reemplaza el "reloj = pygame.time.Clock() / reloj.tick(60)" del Python,
 * pero ahora el tick vive en el servidor, no en cada pantalla de cliente.
 */
public class GameServer {

    private final GameEngine engine = new GameEngine();
    private final Map<String, ClientHandler> handlers = new ConcurrentHashMap<>();
    private final ScheduledExecutorService tickExecutor = Executors.newSingleThreadScheduledExecutor();

    public void start() throws IOException {
        try (ServerSocket serverSocket = new ServerSocket(GameConfig.PORT)) {
            System.out.println("FAST CASH server escuchando en el puerto " + GameConfig.PORT);
            iniciarLoopDeJuego();

            // TODO: si quieren, aca se puede rechazar la conexion cuando
            // handlers.size() >= GameConfig.MAX_PLAYERS en vez de aceptar siempre.
            while (true) {
                Socket socket = serverSocket.accept();
                aceptarJugador(socket);
            }
        }
    }

    private void aceptarJugador(Socket socket) {
        String playerId = "player-" + UUID.randomUUID().toString().substring(0, 8);
        System.out.println("Se conecto " + playerId + " desde " + socket.getRemoteSocketAddress());

        PlayerSession session = new PlayerSession(playerId);
        engine.registrarJugador(session);

        ClientHandler handler = new ClientHandler(socket, engine, playerId, () -> {
            handlers.remove(playerId);
            System.out.println(playerId + " se desconecto");
        });
        session.handler = handler;
        handlers.put(playerId, handler);

        new Thread(handler, "handler-" + playerId).start();
    }

    private void iniciarLoopDeJuego() {
        tickExecutor.scheduleAtFixedRate(() -> {
            try {
                engine.tick();
                var snapshot = engine.snapshot();
                for (ClientHandler handler : handlers.values()) {
                    handler.enviarEstado(snapshot);
                }
            } catch (Exception e) {
                // Nunca dejar que una excepcion en un tick tire abajo el scheduler.
                e.printStackTrace();
            }
        }, 0, GameConfig.TICK_MILLIS, TimeUnit.MILLISECONDS);
    }
}
