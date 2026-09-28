package com.fastcash.client.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.fastcash.client.FastCashGame;
import com.fastcash.client.net.GameClient;
import com.fastcash.core.GameModules;
import com.fastcash.core.model.GameStateSnapshot;
import com.fastcash.core.model.Modulo;

/**
 * Pantalla donde se elige el modulo de la partida (Historia Mundial / Historia
 * Argentina / Territorio Argentino / Barrios de CABA). La eleccion es UNICA
 * para toda la partida: apenas el servidor confirma un modulo (propio o del
 * otro jugador, el que haya llegado primero), esta pantalla pasa sola a
 * CharacterSelectScreen. Ver GameEngine.elegirModulo() del lado servidor.
 */
public class ModuleSelectScreen extends ScreenAdapter {

    private final FastCashGame game;
    private final GameClient client;
    private int seleccionLocal = 0;
    private boolean yaEnviado = false;

    public ModuleSelectScreen(FastCashGame game, GameClient client) {
        this.game = game;
        this.client = client;
    }

    @Override
    public void render(float delta) {
        GameStateSnapshot snapshot = client.ultimoEstado();
        if (snapshot != null && snapshot.moduloId != null) {
            // Se confirmo un modulo (el que elegimos nosotros, o el del otro
            // jugador si nos gano de mano): pasamos directo a elegir personaje.
            game.setScreen(new CharacterSelectScreen(game, client, snapshot.moduloId));
            return;
        }

        manejarInput();

        Gdx.gl.glClearColor(0.05f, 0.05f, 0.08f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        game.getBatch().begin();
        dibujar();
        game.getBatch().end();
    }

    private void manejarInput() {
        if (yaEnviado) return; // ya mandamos nuestra eleccion, solo esperamos la confirmacion

        int cantidad = GameModules.MODULOS.size();
        if (Gdx.input.isKeyJustPressed(Input.Keys.LEFT)) {
            seleccionLocal = (seleccionLocal - 1 + cantidad) % cantidad;
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.RIGHT)) {
            seleccionLocal = (seleccionLocal + 1) % cantidad;
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
            client.enviarSeleccionModulo(GameModules.MODULOS.get(seleccionLocal).id);
            yaEnviado = true;
        }
    }

    private void dibujar() {
        game.getFont().setColor(Color.WHITE);
        game.getFont().draw(game.getBatch(), "Elegi un modulo", 60, 580);

        var modulos = GameModules.MODULOS;
        for (int i = 0; i < modulos.size(); i++) {
            Modulo m = modulos.get(i);
            boolean resaltado = i == seleccionLocal;
            game.getFont().setColor(resaltado ? Color.YELLOW : Color.LIGHT_GRAY);
            String prefijo = resaltado ? "> " : "  ";
            game.getFont().draw(game.getBatch(), prefijo + m.nombre, 100, 480 - i * 60);
        }

        game.getFont().setColor(Color.WHITE);
        String instructivo = yaEnviado
                ? "Confirmando con el servidor..."
                : "<- -> para elegir, ESPACIO para confirmar";
        game.getFont().draw(game.getBatch(), instructivo, 60, 150);
    }
}
