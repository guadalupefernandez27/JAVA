package org.example;

import jakarta.persistence.*;

@Entity
public class Pou{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String nombre;
    private int hambre;
    private int felicidad;
    private int suciedad;
    private int energia;

    public Pou() {
    }

    public Pou(String nombre, int hambre, int felicidad, int suciedad, int energia) {
        this.nombre = nombre;
        this.hambre = hambre;
        this.felicidad = felicidad;
        this.suciedad = suciedad;
        this.energia = energia;
    }

    public int getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public int getHambre() {
        return hambre;
    }

    public int getFelicidad() {
        return felicidad;
    }

    public int getSuciedad() {
        return suciedad;
    }

    public int getEnergia() {
        return energia;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setHambre(int hambre) {
        this.hambre = hambre;
    }

    public void setFelicidad(int felicidad) {
        this.felicidad = felicidad;
    }

    public void setSuciedad(int suciedad) {
        this.suciedad = suciedad;
    }

    public void setEnergia(int energia) {
        this.energia = energia;
    }
}