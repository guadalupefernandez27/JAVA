package com.fastcash.core;

import com.fastcash.core.model.Modulo;

/**
 * Constantes compartidas por server y client.
 * Equivalen a los "numeros magicos" que en la version Python estaban sueltos
 * (1300, 650, 50, etc). Aca viven en un solo lugar.
 */
public final class GameConfig {

    private GameConfig() {}

    // Red
    public static final int PORT = 5555;
    public static final int TICK_RATE = 60;          // ticks del server por segundo
    public static final long TICK_MILLIS = 1000L / TICK_RATE;
    public static final int MAX_PLAYERS = 2;

    // Ventana / mundo (mismas medidas que ventana = pygame.display.set_mode((1300, 650)))
    public static final int WORLD_WIDTH = 1300;
    public static final int WORLD_HEIGHT = 650;

    // Personaje (velocidad_base = 15 en el Python original)
    public static final float PLAYER_BASE_SPEED = 15f;
    public static final float PLAYER_START_X = 650f;
    public static final float PLAYER_START_Y = 500f;
    public static final float PLAYER_WIDTH = 80f;   // pygame.transform.scale(img, (80, 160))
    public static final float PLAYER_HEIGHT = 160f;

    // Recursos que caen (nuevas_dimensiones = (90, 60) en el Python original;
    // se mantiene el mismo tamaño en pantalla para todos los recursos, sea
    // un billete real o una vasija de barro).
    public static final float RECURSO_WIDTH = 90f;
    public static final float RECURSO_HEIGHT = 60f;

    // Energia (energia_maxima = 50)
    public static final int ENERGIA_MAXIMA = 50;
    public static final int ENERGIA_MINIMA = 0;
    public static final int ENERGIA_GANANCIA_POR_RECURSO = 10;
    public static final int ENERGIA_PERDIDA_POR_RECURSO_CAIDO = 20;

    // Reglas de partida
    public static final long MS_ENTRE_RECURSOS = 1500;  // tiempo_ultimo_billete > 1500 del Python

    // Estructura de modulos: 4 modulos tematicos, cada uno con 7 mapas.
    // El catalogo completo (nombres, textos, archivos de imagen, economia
    // de cada modulo) vive en core/GameModules.java, no aca.
    public static final int CANTIDAD_MODULOS = 4;
    public static final int MAPAS_POR_MODULO = 7;

    /**
     * A que mapa (0..MAPAS_POR_MODULO-1) corresponde este puntaje, dentro del
     * modulo dado. Cada modulo tiene su propia economia (Modulo.pesosPorMapa),
     * porque los "pesos reales" de barrios-caba valen mucho mas que una
     * vasija de barro de los otros modulos.
     */
    public static int calcularMapaActual(Modulo modulo, int puntaje) {
        int mapa = puntaje / modulo.pesosPorMapa();
        return Math.min(mapa, MAPAS_POR_MODULO - 1);
    }
}
