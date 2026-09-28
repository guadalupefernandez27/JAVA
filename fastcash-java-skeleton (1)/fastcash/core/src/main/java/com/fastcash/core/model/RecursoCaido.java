package com.fastcash.core.model;

/**
 * Estado de un recurso cayendo en pantalla (reemplaza a la vieja BilleteState).
 * Solo guarda el ID del recurso (ej. "item1", "20000"): el cliente busca ese
 * ID en la lista de recursos del mapa/modulo activo (Modulo.recursosPara) para
 * saber que imagen dibujar. El servidor tampoco necesita mas que el ID mas
 * el valor/velocidad, que ya trae el RecursoDef correspondiente.
 */
public class RecursoCaido {
    public String id;          // identificador unico de ESTE recurso cayendo (no del tipo)
    public String recursoId;   // id del RecursoDef (ej. "item1", "20000")
    public int valor;
    public float velocidadCaida;
    public float x;
    public float y;

    public RecursoCaido() {
        // constructor vacio requerido por Gson
    }

    public RecursoCaido(String id, String recursoId, int valor, float velocidadCaida, float x, float y) {
        this.id = id;
        this.recursoId = recursoId;
        this.valor = valor;
        this.velocidadCaida = velocidadCaida;
        this.x = x;
        this.y = y;
    }
}
