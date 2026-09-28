package com.fastcash.core.model;

import java.util.List;

/**
 * Un mapa dentro de un modulo. Es solo catalogo/contenido, no estado de
 * partida (el mapa en el que esta la partida vive en GameStateSnapshot.mapaActual,
 * un indice a la lista Modulo.niveles).
 *
 * "id" es el nombre real de la carpeta en el Drive del equipo (ej. "01_edad_piedra"):
 * se usa tal cual como nombre de subcarpeta en client/assets/modulos/<idModulo>/<id>/,
 * asi que alcanza con copiar esa carpeta de Drive sin renombrar nada.
 */
public class Nivel {
    public String id;
    public String titulo;
    public String textoInformativo;
    public String archivoFondo;
    public String archivoPersonajeMasculino;
    public String archivoPersonajeFemenino;

    // 2 recursos propios de este mapa (modulos 1 a 3). Si el modulo entero
    // comparte una sola lista de recursos para sus 7 mapas (ver
    // Modulo.recursosCompartidos, el caso de barrios-caba), esto queda en null
    // y se usa esa lista en su lugar.
    public List<RecursoDef> recursos;

    public Nivel() {
    }

    public Nivel(String id, String titulo, String textoInformativo, String archivoFondo,
                 String archivoPersonajeMasculino, String archivoPersonajeFemenino,
                 List<RecursoDef> recursos) {
        this.id = id;
        this.titulo = titulo;
        this.textoInformativo = textoInformativo;
        this.archivoFondo = archivoFondo;
        this.archivoPersonajeMasculino = archivoPersonajeMasculino;
        this.archivoPersonajeFemenino = archivoPersonajeFemenino;
        this.recursos = recursos;
    }
}
