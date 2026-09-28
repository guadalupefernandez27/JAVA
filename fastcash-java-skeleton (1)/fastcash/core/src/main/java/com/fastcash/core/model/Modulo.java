package com.fastcash.core.model;

import java.util.List;

/**
 * Uno de los 4 modulos tematicos (Historia Mundial / Historia Argentina /
 * Territorio Argentino / Barrios de CABA). Cada modulo tiene 7 mapas
 * (Modulo.niveles) y su propia economia de puntaje.
 *
 * Este catalogo (ver GameModules.MODULOS) es el mismo tanto para el server
 * como para el client: el server lo usa para saber cuantos mapas tiene el
 * modulo elegido y que recursos pueden caer (logica de juego); el client lo
 * usa ademas para saber que imagenes cargar y que texto mostrar.
 */
public class Modulo {
    public String id;                          // slug estable, ej. "historia-mundial" (va por red y en nombres de carpeta)
    public String nombre;                      // nombre para mostrar, ej. "Historia Mundial"
    public List<Nivel> niveles;                // 7 mapas, en el orden en que se juegan
    public List<RecursoDef> recursosCompartidos; // no-null SOLO en barrios-caba: 7 billetes reales,
                                                 // iguales para los 7 mapas (en vez de 2 por mapa)
    public int puntajeVictoria;                // total de plata para ganar (distinto por modulo:
                                                 // los recursos historicos valen poco, los pesos reales mucho)

    public Modulo() {
    }

    public Modulo(String id, String nombre, List<Nivel> niveles,
                  List<RecursoDef> recursosCompartidos, int puntajeVictoria) {
        this.id = id;
        this.nombre = nombre;
        this.niveles = niveles;
        this.recursosCompartidos = recursosCompartidos;
        this.puntajeVictoria = puntajeVictoria;
    }

    /** Cuanta plata hace falta para pasar de un mapa al siguiente dentro de este modulo. */
    public int pesosPorMapa() {
        return puntajeVictoria / niveles.size();
    }

    /** Los recursos que pueden caer para un mapa dado: los propios del mapa, o los del modulo si los comparte. */
    public List<RecursoDef> recursosPara(int indiceMapa) {
        if (recursosCompartidos != null) return recursosCompartidos;
        return niveles.get(indiceMapa).recursos;
    }
}
