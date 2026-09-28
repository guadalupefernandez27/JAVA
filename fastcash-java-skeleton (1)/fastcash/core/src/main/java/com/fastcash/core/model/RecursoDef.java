package com.fastcash.core.model;

/**
 * Definicion de un recurso coleccionable (reemplaza al viejo enum fijo
 * BilleteType). Antes habia 3 tipos de billete iguales para todo el juego;
 * ahora cada mapa (modulos 1 a 3) o cada modulo entero (barrios-caba) tiene
 * su propia lista de recursos, cada uno con su propia imagen.
 */
public class RecursoDef {
    public String id;             // unico dentro de su lista, ej. "item1", "item2", "20000"
    public String archivoImagen;  // nombre de archivo tal cual esta en Drive, sin renombrar
    public int valor;             // puntos que suma al atraparlo
    public float velocidadCaida;

    public RecursoDef() {
    }

    public RecursoDef(String id, String archivoImagen, int valor, float velocidadCaida) {
        this.id = id;
        this.archivoImagen = archivoImagen;
        this.valor = valor;
        this.velocidadCaida = velocidadCaida;
    }
}
