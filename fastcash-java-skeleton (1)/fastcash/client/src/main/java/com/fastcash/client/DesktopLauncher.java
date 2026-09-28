package com.fastcash.client;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.fastcash.core.GameConfig;

/** Punto de entrada del cliente de escritorio (equivalente a "pygame.display.set_mode"). */
public class DesktopLauncher {
    public static void main(String[] args) {
        Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
        config.setTitle("FAST CASH");
        config.setWindowedMode(GameConfig.WORLD_WIDTH, GameConfig.WORLD_HEIGHT);
        config.useVsync(true);
        config.setForegroundFPS(GameConfig.TICK_RATE);

        new Lwjgl3Application(new FastCashGame(), config);
    }
}
