package com.fastcash.client.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.fastcash.client.FastCashGame;
import com.fastcash.client.net.GameClient;
import com.fastcash.core.GameConfig;
import com.fastcash.core.GameModules;
import com.fastcash.core.model.GameStateSnapshot;
import com.fastcash.core.model.Modulo;
import com.fastcash.core.model.Nivel;
import com.fastcash.core.model.PlayerState;
import com.fastcash.core.model.RecursoCaido;
import com.fastcash.core.model.RecursoDef;

import java.util.HashMap;
import java.util.Map;

public class GameScreen extends ScreenAdapter {

    private static final float DURACION_INFO_SEGUNDOS = 4f;

    private final FastCashGame game;
    private final GameClient client;
    private final SpriteBatch batch;
    private final String moduloId;
    private final Modulo modulo;
    private final int personajeIndex; // 0=masculino, 1=femenino

    private final Texture[] fondosMapa;          // 1 por mapa (7)
    private final Texture[] personajeTexturas;   // 1 por mapa (7)
    private final Texture[] imagenesInfoMapa;    // 1 por mapa (7)
    private final Texture[][] recursoTexturasPorMapa;
    private final Map<String, Texture> recursoTexturasCompartidas = new HashMap<>();
    private final Texture pixelBlanco;

    private boolean mostrandoInfo = false;
    private float tiempoRestanteInfo = 0f;
    private int ultimoMapaMostrado = -1;

    public GameScreen(FastCashGame game, GameClient client, String moduloId, int personajeIndex) {
        this.game = game;
        this.client = client;
        this.batch = game.getBatch();
        this.moduloId = moduloId;
        this.personajeIndex = personajeIndex;

        Modulo encontrado = GameModules.porId(moduloId);
        this.modulo = encontrado != null ? encontrado : GameModules.MODULOS.get(0);

        int cantidadMapas = modulo.niveles.size();
        this.pixelBlanco = crearPixelBlanco();
        this.fondosMapa = new Texture[cantidadMapas];
        this.personajeTexturas = new Texture[cantidadMapas];
        this.imagenesInfoMapa = new Texture[cantidadMapas];
        this.recursoTexturasPorMapa = modulo.recursosCompartidos == null
                ? new Texture[cantidadMapas][2]
                : null;

        cargarFondosYPersonajes();
        cargarTexturasDeRecursos();
        cargarImagenesInformativas();
    }

    private Texture crearPixelBlanco() {
        Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pixmap.setColor(Color.WHITE);
        pixmap.fill();
        Texture textura = new Texture(pixmap);
        pixmap.dispose();
        return textura;
    }

    private Texture cargarSiExiste(String ruta) {
        FileHandle archivo = Gdx.files.internal(ruta);
        return archivo.exists() ? new Texture(archivo) : null;
    }

    private void cargarFondosYPersonajes() {
        for (int i = 0; i < modulo.niveles.size(); i++) {
            Nivel nivel = modulo.niveles.get(i);
            String base = "modulos/" + moduloId + "/" + nivel.id + "/";

            fondosMapa[i] = cargarSiExiste(base + nivel.archivoFondo);

            String archivoPersonaje = personajeIndex == 0
                    ? nivel.archivoPersonajeMasculino
                    : nivel.archivoPersonajeFemenino;
            personajeTexturas[i] = cargarSiExiste(base + archivoPersonaje);
        }
    }

    private void cargarTexturasDeRecursos() {
        if (modulo.recursosCompartidos != null) {
            for (RecursoDef def : modulo.recursosCompartidos) {
                Texture t = cargarSiExiste("modulos/" + moduloId + "/recursos/" + def.archivoImagen);
                if (t != null) recursoTexturasCompartidas.put(def.id, t);
            }
            return;
        }

        for (int i = 0; i < modulo.niveles.size(); i++) {
            Nivel nivel = modulo.niveles.get(i);
            String base = "modulos/" + moduloId + "/" + nivel.id + "/";
            for (int j = 0; j < nivel.recursos.size(); j++) {
                recursoTexturasPorMapa[i][j] = cargarSiExiste(base + nivel.recursos.get(j).archivoImagen);
            }
        }
    }

    private void cargarImagenesInformativas() {
        for (int i = 0; i < modulo.niveles.size(); i++) {
            Nivel nivel = modulo.niveles.get(i);
            imagenesInfoMapa[i] = cargarSiExiste("modulos/" + moduloId + "/" + nivel.id + "/info.png");
        }
    }

    @Override
    public void render(float delta) {
        GameStateSnapshot snapshot = client.ultimoEstado();
        PlayerState propio = snapshot != null ? buscarPropio(snapshot) : null;

        // Si la partida terminó para este jugador, se evalúa la salida al menú principal
        if (propio != null && !propio.vivo) {
            if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE) || Gdx.input.justTouched()) {
                client.cerrar();
                game.setScreen(new MenuScreen(game));
                return;
            }
        }

        actualizarPantallaInformativa(snapshot, delta);

        if (mostrandoInfo) {
            client.enviarInput(false, false);
        } else {
            leerInputYEnviar();
        }

        Gdx.gl.glClearColor(0f, 0f, 0f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.begin();
        if (snapshot == null) {
            game.getFont().draw(batch, "Esperando al servidor...", 400, 350);
        } else if (mostrandoInfo) {
            dibujarInfoMapa(snapshot.mapaActual);
        } else {
            dibujarFondo(snapshot.mapaActual);
            dibujarRecursos(snapshot);
            dibujarJugadores(snapshot);
            dibujarHud(snapshot, propio);
        }
        batch.end();
    }

    private void actualizarPantallaInformativa(GameStateSnapshot snapshot, float delta) {
        if (snapshot == null) return;

        if (!mostrandoInfo && snapshot.mapaActual != ultimoMapaMostrado) {
            mostrandoInfo = true;
            tiempoRestanteInfo = DURACION_INFO_SEGUNDOS;
            ultimoMapaMostrado = snapshot.mapaActual;
            return;
        }

        if (mostrandoInfo) {
            tiempoRestanteInfo -= delta;
            if (tiempoRestanteInfo <= 0f || Gdx.input.isKeyJustPressed(Input.Keys.SPACE) || Gdx.input.justTouched()) {
                mostrandoInfo = false;
            }
        }
    }

    private void dibujarInfoMapa(int mapaActual) {
        Nivel nivel = modulo.niveles.get(mapaActual);
        Texture imagen = imagenesInfoMapa[mapaActual];

        if (imagen != null) {
            batch.draw(imagen, 0, 0, GameConfig.WORLD_WIDTH, GameConfig.WORLD_HEIGHT);
        } else {
            batch.setColor(0.08f, 0.08f, 0.15f, 1f);
            batch.draw(pixelBlanco, 0, 0, GameConfig.WORLD_WIDTH, GameConfig.WORLD_HEIGHT);
            batch.setColor(Color.WHITE);
        }

        game.getFont().setColor(Color.WHITE);
        game.getFont().draw(batch, modulo.nombre + " - " + nivel.titulo, 60, GameConfig.WORLD_HEIGHT - 80);
        game.getFont().draw(batch, nivel.textoInformativo, 60, GameConfig.WORLD_HEIGHT - 140);
        game.getFont().setColor(Color.LIGHT_GRAY);
        game.getFont().draw(batch, "ESPACIO para continuar", 60, 60);
        game.getFont().setColor(Color.WHITE);
    }

    private void leerInputYEnviar() {
        boolean left = Gdx.input.isKeyPressed(Input.Keys.LEFT) || Gdx.input.isKeyPressed(Input.Keys.A);
        boolean right = Gdx.input.isKeyPressed(Input.Keys.RIGHT) || Gdx.input.isKeyPressed(Input.Keys.D);
        client.enviarInput(left, right);
    }

    private void dibujarFondo(int mapaActual) {
        Texture fondo = fondosMapa[mapaActual];

        if (fondo != null) {
            batch.draw(fondo, 0, 0, GameConfig.WORLD_WIDTH, GameConfig.WORLD_HEIGHT);
        } else {
            batch.setColor(colorDeRespaldo(mapaActual));
            batch.draw(pixelBlanco, 0, 0, GameConfig.WORLD_WIDTH, GameConfig.WORLD_HEIGHT);
            batch.setColor(Color.WHITE);
        }
    }

    private Color colorDeRespaldo(int mapa) {
        float tono = mapa / (float) Math.max(1, modulo.niveles.size() - 1);
        return new Color(0.10f + 0.10f * tono, 0.10f, 0.20f - 0.10f * tono, 1f);
    }

    private void dibujarRecursos(GameStateSnapshot snapshot) {
        for (RecursoCaido r : snapshot.recursos) {
            Texture textura = texturaDeRecurso(r, snapshot.mapaActual);
            float yPantalla = GameConfig.WORLD_HEIGHT - r.y - GameConfig.RECURSO_HEIGHT;

            if (textura != null) {
                batch.draw(textura, r.x, yPantalla, GameConfig.RECURSO_WIDTH, GameConfig.RECURSO_HEIGHT);
            } else {
                batch.setColor(0.7f, 0.6f, 0.1f, 1f);
                batch.draw(pixelBlanco, r.x, yPantalla, GameConfig.RECURSO_WIDTH, GameConfig.RECURSO_HEIGHT);
                batch.setColor(Color.WHITE);
            }
        }
    }

    private Texture texturaDeRecurso(RecursoCaido r, int mapaActual) {
        if (modulo.recursosCompartidos != null) {
            return recursoTexturasCompartidas.get(r.recursoId);
        }
        int indice = "item1".equals(r.recursoId) ? 0 : 1;
        return recursoTexturasPorMapa[mapaActual][indice];
    }

    private void dibujarJugadores(GameStateSnapshot snapshot) {
        for (PlayerState p : snapshot.jugadores) {
            int indice = p.personajeIndex >= 0 ? p.personajeIndex : 0;
            Texture textura = indice == personajeIndex
                    ? personajeTexturas[snapshot.mapaActual]
                    : texturaDelOtroPersonaje(indice, snapshot.mapaActual);

            float yPantalla = GameConfig.WORLD_HEIGHT - p.y - GameConfig.PLAYER_HEIGHT;

            if (textura != null) {
                batch.setColor(p.vivo ? Color.WHITE : Color.DARK_GRAY);
                batch.draw(textura, p.x, yPantalla, GameConfig.PLAYER_WIDTH, GameConfig.PLAYER_HEIGHT);
                batch.setColor(Color.WHITE);
            } else {
                batch.setColor(indice == 0 ? new Color(0.6f, 0.2f, 0.2f, 1f) : new Color(0.2f, 0.3f, 0.6f, 1f));
                batch.draw(pixelBlanco, p.x, yPantalla, GameConfig.PLAYER_WIDTH, GameConfig.PLAYER_HEIGHT);
                batch.setColor(Color.WHITE);
            }

            game.getFont().setColor(Color.WHITE);
            game.getFont().draw(batch, p.playerId, p.x, yPantalla + GameConfig.PLAYER_HEIGHT + 20);
        }
    }

    private final Map<String, Texture> texturasOtroGeneroCache = new HashMap<>();

    private Texture texturaDelOtroPersonaje(int indice, int mapaActual) {
        Nivel nivel = modulo.niveles.get(mapaActual);
        String archivo = indice == 0 ? nivel.archivoPersonajeMasculino : nivel.archivoPersonajeFemenino;
        String ruta = "modulos/" + moduloId + "/" + nivel.id + "/" + archivo;

        return texturasOtroGeneroCache.computeIfAbsent(ruta, this::cargarSiExisteONull);
    }

    private Texture cargarSiExisteONull(String ruta) {
        return cargarSiExiste(ruta);
    }

    private void dibujarHud(GameStateSnapshot snapshot, PlayerState propio) {
        if (propio == null) return;

        Nivel nivel = modulo.niveles.get(snapshot.mapaActual);
        game.getFont().setColor(Color.RED);
        game.getFont().draw(batch, "Puntaje: " + propio.puntaje, 10, GameConfig.WORLD_HEIGHT - 10);
        game.getFont().draw(batch, "Energia: " + propio.energia, 10, GameConfig.WORLD_HEIGHT - 40);
        game.getFont().draw(batch, modulo.nombre + " - " + nivel.titulo, 10, GameConfig.WORLD_HEIGHT - 70);

        if (!propio.vivo) {
            String resultado = propio.gano ? "¡GANASTE!" : "PERDISTE";
            game.getFont().setColor(propio.gano ? Color.GREEN : Color.RED);
            game.getFont().draw(batch, resultado, 530, 350);

            game.getFont().setColor(Color.WHITE);
            game.getFont().draw(batch, "Presiona ESPACIO para volver al inicio", 400, 300);
        }
    }

    private PlayerState buscarPropio(GameStateSnapshot snapshot) {
        for (PlayerState p : snapshot.jugadores) {
            if (p.playerId.equals(client.playerId())) return p;
        }
        return null;
    }

    @Override
    public void hide() {
        client.cerrar();
    }

    @Override
    public void dispose() {
        for (Texture t : personajeTexturas) {
            if (t != null) t.dispose();
        }
        for (Texture t : fondosMapa) {
            if (t != null) t.dispose();
        }
        for (Texture t : imagenesInfoMapa) {
            if (t != null) t.dispose();
        }
        if (recursoTexturasPorMapa != null) {
            for (Texture[] fila : recursoTexturasPorMapa) {
                for (Texture t : fila) {
                    if (t != null) t.dispose();
                }
            }
        }
        for (Texture t : recursoTexturasCompartidas.values()) {
            t.dispose();
        }
        for (Texture t : texturasOtroGeneroCache.values()) {
            if (t != null) t.dispose();
        }
        pixelBlanco.dispose();
    }
}