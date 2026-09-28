package com.fastcash.client.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.fastcash.client.FastCashGame;
import com.fastcash.client.net.GameClient;
import com.fastcash.core.GameConfig;
import com.fastcash.core.GameModules;
import com.fastcash.core.model.Modulo;
import com.fastcash.core.model.Nivel;

/**
 * Pantalla donde cada jugador elige SU personaje: masculino (0) o femenino (1).
 * No es compartido con el otro jugador como el modulo. La eleccion vale para
 * los 7 mapas del modulo (no se vuelve a preguntar entre mapa y mapa), pero
 * el DIBUJO de ese personaje va a ir cambiando de vestimenta solo en cada
 * mapa (ver GameScreen), porque cada uno de los 7 mapas trae su propio
 * archivo de personaje masculino/femenino acorde a la epoca/lugar.
 *
 * La imagen que se muestra aca es un preview con el personaje del PRIMER
 * mapa del modulo (niveles.get(0)), nada mas que a modo de muestra.
 *
 * A diferencia de ModuleSelectScreen, aca no hace falta esperar confirmacion
 * del servidor para avanzar: es una eleccion puramente cosmetica (no afecta
 * la fisica ni el puntaje), asi que apenas el jugador confirma pasamos
 * directo a GameScreen. El servidor igual la valida y la guarda en
 * PlayerState.personajeIndex (ver GameEngine.elegirPersonaje).
 */
public class CharacterSelectScreen extends ScreenAdapter {

    private static final String[] ETIQUETAS = {"Masculino", "Femenino"};

    private final FastCashGame game;
    private final GameClient client;
    private final String moduloId;
    private final Modulo modulo;
    private final Texture[] previews = new Texture[2];

    private int seleccionLocal = 0;

    public CharacterSelectScreen(FastCashGame game, GameClient client, String moduloId) {
        this.game = game;
        this.client = client;
        this.moduloId = moduloId;
        this.modulo = GameModules.porId(moduloId);
        cargarPreviews();
    }

    private void cargarPreviews() {
        Nivel primerMapa = modulo.niveles.get(0);
        String base = "modulos/" + moduloId + "/" + primerMapa.id + "/";

        previews[0] = cargarSiExiste(base + primerMapa.archivoPersonajeMasculino);
        previews[1] = cargarSiExiste(base + primerMapa.archivoPersonajeFemenino);
    }

    private Texture cargarSiExiste(String ruta) {
        FileHandle archivo = Gdx.files.internal(ruta);
        return archivo.exists() ? new Texture(archivo) : null;
    }

    @Override
    public void render(float delta) {
        manejarInput();

        Gdx.gl.glClearColor(0.05f, 0.05f, 0.08f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        game.getBatch().begin();
        dibujar();
        game.getBatch().end();
    }

    private void manejarInput() {
        if (Gdx.input.isKeyJustPressed(Input.Keys.LEFT) || Gdx.input.isKeyJustPressed(Input.Keys.RIGHT)) {
            seleccionLocal = 1 - seleccionLocal; // solo hay 2 opciones, alternar alcanza
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
            client.enviarSeleccionPersonaje(seleccionLocal);
            game.setScreen(new GameScreen(game, client, moduloId, seleccionLocal));
        }
    }

    private void dibujar() {
        game.getFont().setColor(Color.WHITE);
        game.getFont().draw(game.getBatch(), modulo.nombre + " - Elegi tu personaje", 60, 580);

        float anchoPorSlot = GameConfig.WORLD_WIDTH / 2f;
        for (int i = 0; i < 2; i++) {
            float x = anchoPorSlot * i + anchoPorSlot / 2f - GameConfig.PLAYER_WIDTH / 2f;
            float y = 250;
            boolean resaltado = i == seleccionLocal;

            if (previews[i] != null) {
                game.getBatch().setColor(resaltado ? Color.WHITE : Color.GRAY);
                game.getBatch().draw(previews[i], x, y, GameConfig.PLAYER_WIDTH, GameConfig.PLAYER_HEIGHT);
                game.getBatch().setColor(Color.WHITE);
            } else {
                game.getFont().setColor(resaltado ? Color.YELLOW : Color.GRAY);
                game.getFont().draw(game.getBatch(), "(sin imagen)", x - 20, y + 80);
            }

            game.getFont().setColor(resaltado ? Color.YELLOW : Color.LIGHT_GRAY);
            String prefijo = resaltado ? "> " : "  ";
            game.getFont().draw(game.getBatch(), prefijo + ETIQUETAS[i], x - 10, y - 20);
        }

        game.getFont().setColor(Color.WHITE);
        game.getFont().draw(game.getBatch(), "<- -> para elegir, ESPACIO para confirmar", 60, 150);
    }

    @Override
    public void dispose() {
        for (Texture t : previews) {
            if (t != null) t.dispose();
        }
    }
}
