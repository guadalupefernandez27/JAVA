package org.example;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

import java.util.Random;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        EntityManagerFactory fem =
                Persistence.createEntityManagerFactory("my_sql");

        EntityManager em = fem.createEntityManager();

        Scanner sc = new Scanner(System.in);
        Random random = new Random();

        Pou pou = new Pou("Pou", 50, 50, 50, 50);

        em.getTransaction().begin();
        em.persist(pou);

        Alimento pizza = new Alimento("Pizza", -20, 10, 15, 10);
        Alimento hamburguesa = new Alimento("Hamburguesa", -15, 8, 10, 8);
        Alimento ensalada = new Alimento("Ensalada", -10, 5, 0, 15);

        em.persist(pizza);
        em.persist(hamburguesa);
        em.persist(ensalada);
        em.getTransaction().commit();

        int opcion;

        do {

            System.out.println("\n===== ESTADO DEL POU =====");
            System.out.println("Nombre: " + pou.getNombre());
            System.out.println("Hambre: " + pou.getHambre());
            System.out.println("Felicidad: " + pou.getFelicidad());
            System.out.println("Suciedad: " + pou.getSuciedad());
            System.out.println("Energia: " + pou.getEnergia());

            System.out.println("\n1-Dar de comer");
            System.out.println("2-Bañar");
            System.out.println("3-Jugar");
            System.out.println("4-Dormir");
            System.out.println("5-Acariciar");
            System.out.println("6-Salir");

            opcion = sc.nextInt();

            if(random.nextInt(100) < 20) {
                System.out.println("El Pou no obedecio");

                pou.setFelicidad(pou.getFelicidad() - 10);

                em.getTransaction().begin();
                em.merge(pou);
                em.getTransaction().commit();

                continue;
            }

            switch (opcion) {

                case 1:

                    System.out.println("1-Pizza");
                    System.out.println("2-Hamburguesa");
                    System.out.println("3-Ensalada");

                    int comida = sc.nextInt();

                    Alimento elegido = null;

                    if(comida == 1)
                        elegido = pizza;

                    if(comida == 2)
                        elegido = hamburguesa;

                    if(comida == 3)
                        elegido = ensalada;

                    pou.setHambre(
                            pou.getHambre() + elegido.getHambre());

                    pou.setFelicidad(
                            pou.getFelicidad() + elegido.getFelicidad());

                    pou.setSuciedad(
                            pou.getSuciedad() + elegido.getSuciedad());

                    pou.setEnergia(
                            pou.getEnergia() + elegido.getEnergia());

                    break;

                case 2:

                    pou.setSuciedad(
                            Math.max(0, pou.getSuciedad() - 40));

                    break;

                case 3:

                    pou.setHambre(
                            pou.getHambre() - 20);

                    pou.setEnergia(
                            pou.getEnergia() - 20);

                    pou.setSuciedad(
                            pou.getSuciedad() - 20);

                    pou.setFelicidad(
                            pou.getFelicidad() + 15);

                    break;

                case 4:

                    pou.setEnergia(
                            pou.getEnergia() + 30);

                    break;

                case 5:

                    pou.setFelicidad(
                            pou.getFelicidad() + 10);

                    break;
            }

            em.getTransaction().begin();
            em.merge(pou);
            em.getTransaction().commit();

        } while(opcion != 6);

        em.close();
        fem.close();
    }
}