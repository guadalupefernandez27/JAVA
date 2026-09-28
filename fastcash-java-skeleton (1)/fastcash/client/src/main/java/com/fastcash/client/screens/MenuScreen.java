package com.fastcash.client.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.fastcash.client.FastCashGame;
import com.fastcash.client.net.GameClient;
import com.fastcash.core.GameConfig;

import java.io.IOException;

/**
 * Pantalla de inicio (equivalente al "else: ventana.blit(fondo_inicio...)" del Python,
 * la parte donde se ve "FAST CASH / Presiona ESPACIO para empezar").
 * Aca es donde se abre la conexion al servidor.
 */
public class MenuScreen extends ScreenAdapter {

    private final FastCashGame game;
    private final Texture fondo;
    private String estado = "Presiona ESPACIO para conectar";

    public MenuScreen(FastCashGame game) {
        this.game = game;
        // fondoprincipal.png vive directo en client/assets/ (no en assets/fondos/,
        // porque no es un fondo de nivel sino la pantalla de inicio).
        var archivo = Gdx.files.internal("fondoprincipal.png");
        this.fondo = archivo.exists() ? new Texture(archivo) : null;
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.05f, 0.05f, 0.08f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        game.getBatch().begin();
        if (fondo != null) {
            game.getBatch().draw(fondo, 0, 0, GameConfig.WORLD_WIDTH, GameConfig.WORLD_HEIGHT);
        }
        game.getFont().setColor(Color.RED);
        game.getFont().draw(game.getBatch(), "FAST CASH", 430, 400);
        game.getFont().setColor(Color.WHITE);
        game.getFont().draw(game.getBatch(), estado, 210, 300);
        game.getBatch().end();

        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
            conectar();
        }
    }

    private void conectar() {
        estado = "Conectando...";
        try {
            // TODO: reemplazar "localhost" por la IP del servidor cuando prueben
            // los dos jugadores en maquinas distintas de la facultad.
            GameClient client = new GameClient("localhost", GameConfig.PORT);
            game.setScreen(new ModuleSelectScreen(game, client));
        } catch (IOException e) {
            estado = "No se pudo conectar al servidor";
        }
    }

    @Override
    public void dispose() {
        if (fondo != null) fondo.dispose();
    }
}
