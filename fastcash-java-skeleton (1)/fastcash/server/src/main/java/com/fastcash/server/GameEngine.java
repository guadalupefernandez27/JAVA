package com.fastcash.server;

import com.fastcash.core.GameConfig;
import com.fastcash.core.GameModules;
import com.fastcash.core.model.GameStateSnapshot;
import com.fastcash.core.model.InputState;
import com.fastcash.core.model.Modulo;
import com.fastcash.core.model.PlayerState;
import com.fastcash.core.model.RecursoCaido;
import com.fastcash.core.model.RecursoDef;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicReference;

/**
 * El "juego()" del Python, pero corriendo del lado del servidor y para N jugadores.
 * No dibuja nada: solo mueve numeros. El cliente es quien los pinta en pantalla.
 *
 * Responsabilidades (todas eran del Python, ahora viven aca y solo aca):
 *  - guardar que modulo se eligio para la partida (compartido entre los 2 jugadores)
 *  - guardar que personaje (0=masculino, 1=femenino) eligio cada jugador
 *  - spawnear recursos cada MS_ENTRE_RECURSOS (recien despues de elegir modulo)
 *  - hacerlos caer
 *  - mover a cada jugador segun su ultimo input
 *  - detectar colisiones recurso-jugador
 *  - actualizar energia y puntaje
 *  - actualizar el MAPA COMPARTIDO de la partida (los 2 jugadores ven el
 *    mismo fondo/recursos siempre, aunque tengan puntajes distintos: avanza
 *    segun el jugador que va ganando, igual que ya se hacia para elegir la
 *    dificultad de spawn)
 *  - detectar game over (perdio por energia 0) y victoria (llego al
 *    puntajeVictoria del modulo elegido)
 */
public class GameEngine {

    private final Map<String, PlayerSession> jugadores = new ConcurrentHashMap<>();
    private final List<RecursoCaido> recursosCaidos = new ArrayList<>();
    private final RecursoSpawner spawner = new RecursoSpawner();

    // "El primero que elige, decide": compareAndSet solo deja que la primera
    // llamada exitosa fije el valor; las siguientes no pisan lo ya elegido.
    private final AtomicReference<String> moduloId = new AtomicReference<>();

    private int mapaActual = 0; // compartido por toda la partida, ver actualizarMapaActual()
    private long ultimoSpawnMillis = 0;
    private boolean juegoTerminado = false;

    public void registrarJugador(PlayerSession session) {
        jugadores.put(session.playerId, session);
    }

    public synchronized void quitarJugador(String playerId) {
        jugadores.remove(playerId);

        // Si no quedan jugadores activos en la sala, se resetea la partida
        // para permitir elegir un modulo nuevo en la proxima conexion.
        if (jugadores.isEmpty()) {
            reiniciarPartida();
        }
    }

    /**
     * Resetea las variables internas del servidor para poder iniciar una nueva partida.
     */
    public synchronized void reiniciarPartida() {
        moduloId.set(null);
        recursosCaidos.clear();
        mapaActual = 0;
        ultimoSpawnMillis = 0;
        juegoTerminado = false;
        System.out.println("[Servidor] Partida reiniciada. Modulo liberado.");
    }

    public void recibirInput(InputState input) {
        PlayerSession session = jugadores.get(input.playerId);
        if (session != null) {
            session.lastInput = input;
        }
    }

    /**
     * Intenta fijar el modulo de la partida. Devuelve true si esta llamada
     * fue la que efectivamente lo fijo (o si ya estaba fijado en ese mismo valor),
     * false si la partida ya tenia otro modulo elegido o si el id no existe.
     */
    public boolean elegirModulo(String candidato) {
        if (GameModules.porId(candidato) == null) return false; // id invalido, se ignora
        String actual = moduloId.compareAndExchange(null, candidato);
        return actual == null || actual.equals(candidato);
    }

    public String moduloElegido() {
        return moduloId.get();
    }

    /** Guarda que personaje (0=masculino, 1=femenino) uso este jugador. */
    public void elegirPersonaje(String playerId, int indice) {
        PlayerSession session = jugadores.get(playerId);
        if (session == null) return;
        if (indice != 0 && indice != 1) return; // solo hay 2 opciones: masculino o femenino

        session.state.personajeIndex = indice;
    }

    /** Un tick de juego. Se llama TICK_RATE veces por segundo desde GameServer. */
    public void tick() {
        if (juegoTerminado) return;

        String idActual = moduloId.get();
        if (idActual == null) return; // esperando a que se elija el modulo antes de arrancar

        Modulo modulo = GameModules.porId(idActual);
        if (modulo == null) return;

        spawnearRecursosSiCorresponde(modulo);
        moverRecursos();
        moverJugadores();
        detectarColisiones();
        actualizarMapaActual(modulo);
        chequearFinDePartida(modulo);
    }

    private void spawnearRecursosSiCorresponde(Modulo modulo) {
        long ahora = System.currentTimeMillis();
        if (ahora - ultimoSpawnMillis > GameConfig.MS_ENTRE_RECURSOS) {
            ultimoSpawnMillis = ahora;

            List<RecursoDef> opciones = modulo.recursosPara(mapaActual);
            RecursoDef elegido = spawner.siguiente(opciones);
            float x = spawner.posicionXAleatoria();

            recursosCaidos.add(new RecursoCaido(
                    UUID.randomUUID().toString(), elegido.id, elegido.valor,
                    elegido.velocidadCaida, x, -30f
            ));
        }
    }

    private void moverRecursos() {
        for (RecursoCaido r : recursosCaidos) {
            r.y += r.velocidadCaida;
        }
    }

    private void moverJugadores() {
        for (PlayerSession session : jugadores.values()) {
            if (!session.state.vivo) continue;

            InputState input = session.lastInput;
            if (input == null) continue;

            float proporcionEnergia = session.state.energia / (float) GameConfig.ENERGIA_MAXIMA;
            float velocidad = GameConfig.PLAYER_BASE_SPEED * proporcionEnergia;

            if (input.left && session.state.x > 0) {
                session.state.x -= velocidad;
            }
            if (input.right && session.state.x < GameConfig.WORLD_WIDTH) {
                session.state.x += velocidad;
            }
        }
    }

    private void detectarColisiones() {
        Iterator<RecursoCaido> it = recursosCaidos.iterator();
        while (it.hasNext()) {
            RecursoCaido recurso = it.next();
            boolean atrapado = false;

            for (PlayerSession session : jugadores.values()) {
                if (!session.state.vivo) continue;
                if (colisiona(recurso, session.state)) {
                    session.state.puntaje += recurso.valor;
                    session.state.energia = Math.min(
                            GameConfig.ENERGIA_MAXIMA,
                            session.state.energia + GameConfig.ENERGIA_GANANCIA_POR_RECURSO
                    );
                    atrapado = true;
                    break;
                }
            }

            if (atrapado) {
                it.remove();
                continue;
            }

            // se cayo al piso sin que nadie lo agarre
            if (recurso.y > GameConfig.WORLD_HEIGHT) {
                it.remove();
                for (PlayerSession session : jugadores.values()) {
                    if (!session.state.vivo) continue;
                    session.state.energia = Math.max(
                            GameConfig.ENERGIA_MINIMA,
                            session.state.energia - GameConfig.ENERGIA_PERDIDA_POR_RECURSO_CAIDO
                    );
                    if (session.state.energia <= GameConfig.ENERGIA_MINIMA) {
                        session.state.vivo = false; // "pantalla_fin(gano=False)" del Python
                    }
                }
            }
        }
    }

    private boolean colisiona(RecursoCaido r, PlayerState p) {
        // Deteccion tipo AABB, equivalente a rect.colliderect() de pygame.
        boolean seSuperponenEnX = r.x < p.x + GameConfig.PLAYER_WIDTH
                && r.x + GameConfig.RECURSO_WIDTH > p.x;
        boolean seSuperponenEnY = r.y < p.y + GameConfig.PLAYER_HEIGHT
                && r.y + GameConfig.RECURSO_HEIGHT > p.y;
        return seSuperponenEnX && seSuperponenEnY;
    }

    /**
     * El mapa es UNO SOLO para toda la partida (fondo/recursos compartidos en
     * pantalla), asi que no puede depender del puntaje de cada jugador por
     * separado: usamos el maximo puntaje entre los jugadores vivos, igual
     * criterio que ya se usaba para la dificultad de spawn.
     */
    private void actualizarMapaActual(Modulo modulo) {
        int referencia = jugadores.values().stream()
                .mapToInt(p -> p.state.puntaje)
                .max()
                .orElse(0);
        mapaActual = GameConfig.calcularMapaActual(modulo, referencia);
    }

    private void chequearFinDePartida(Modulo modulo) {
        for (PlayerSession session : jugadores.values()) {
            if (session.state.puntaje >= modulo.puntajeVictoria) {
                session.state.gano = true;
                session.state.vivo = false;
                juegoTerminado = true;
            }
        }
    }

    public GameStateSnapshot snapshot() {
        List<PlayerState> estados = new ArrayList<>();
        for (PlayerSession s : jugadores.values()) {
            estados.add(s.state);
        }
        return new GameStateSnapshot(
                new ArrayList<>(recursosCaidos), estados, juegoTerminado, moduloId.get(), mapaActual
        );
    }
}