package com.fastcash.client;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.fastcash.client.screens.MenuScreen;

/**
 * Clase raiz del cliente (equivalente al "if __name__ == '__main__': juego()" del Python,
 * pero organizado en pantallas: MenuScreen y GameScreen).
 */
public class FastCashGame extends Game {

    private SpriteBatch batch;
    private BitmapFont font;

    @Override
    public void create() {
        batch = new SpriteBatch();
        font = new BitmapFont(); // TODO: reemplazar por la fuente pixel del proyecto original
                                  // (press-start-2p/PressStart2P.ttf -> convertirla a .fnt con hiero/BMFont)
        setScreen(new MenuScreen(this));
    }

    public SpriteBatch getBatch() {
        return batch;
    }

    public BitmapFont getFont() {
        return font;
    }

    @Override
    public void dispose() {
        batch.dispose();
        font.dispose();
        if (getScreen() != null) {
            getScreen().dispose();
        }
    }
}
