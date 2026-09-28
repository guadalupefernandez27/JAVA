package com.fastcash.server;

import com.fastcash.core.model.RecursoDef;

import java.util.List;
import java.util.Random;

/**
 * Puerto de Billete.obtener_imagen() del Python, pero generalizado: antes
 * elegia entre 3 billetes fijos segun una tabla de umbrales de plata
 * acumulada; ahora elige entre los 2 a 7 recursos de la lista que le pasen
 * (los del mapa actual, o los 7 billetes compartidos de barrios-caba).
 *
 * La probabilidad de cada recurso es inversamente proporcional a su valor:
 * el que vale mas cae con menos frecuencia. Es una regla simple que anda
 * igual de bien con 2 recursos que con 7, sin necesitar una tabla de
 * umbrales distinta para cada caso.
 */
public class RecursoSpawner {

    private final Random random = new Random();

    public RecursoDef siguiente(List<RecursoDef> opciones) {
        double pesoTotal = 0;
        double[] pesos = new double[opciones.size()];
        for (int i = 0; i < opciones.size(); i++) {
            pesos[i] = 1.0 / opciones.get(i).valor;
            pesoTotal += pesos[i];
        }

        double r = random.nextDouble() * pesoTotal;
        double acumulado = 0;
        for (int i = 0; i < opciones.size(); i++) {
            acumulado += pesos[i];
            if (r < acumulado) return opciones.get(i);
        }
        return opciones.get(opciones.size() - 1); // fallback por redondeo de floats
    }

    /** Posicion X aleatoria de spawn, igual a random.randint(0, 1100) del Python. */
    public float posicionXAleatoria() {
        return random.nextInt(1101); // 0..1100 inclusive
    }
}
