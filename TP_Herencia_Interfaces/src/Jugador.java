import java.util.*;

public class Jugador {
    int esmeraldas;
    double muslitos;
    int experiencia;
    Pico pico;
    double reduccionMob;
    List<Item> inventario;
    int vecesMina;

    public Jugador() {
        esmeraldas = 5;
        muslitos = 10;
        experiencia = 0;
        pico = new PicoMadera();
        reduccionMob = 0;
        inventario = new ArrayList<>();
        vecesMina = 0;
    }

    public boolean gastarEsmeraldas(int cant) {
        if (esmeraldas >= cant) {
            esmeraldas -= cant;
            return true;
        }
        System.out.println("no tenes suficientes esmeraldas");
        return false;
    }

    public void agregarItem(String nombre, int cant) {
        for (Item it : inventario) {
            if (it.getNombre().equals(nombre)) {
                it.agregar(cant);
                return;
            }
        }
        if (inventario.size() < 32) {
            inventario.add(new Material(nombre, cant));
        } else {
            System.out.println("inventario lleno, se pierde " + nombre);
        }
    }

    public boolean quitarItem(String nombre, int cant) {
        for (Item it : inventario) {
            if (it.getNombre().equals(nombre)) {
                return it.quitar(cant);
            }
        }
        return false;
    }

    public void dormir() {
        if (vecesMina > 0) {
            muslitos = Math.min(10, muslitos + 2);
            vecesMina--;
            System.out.println("dormiste, muslitos: " + muslitos);
        } else {
            System.out.println("ya usaste tu descanso, primero hay que minar");
        }
    }

    public void aldea(Scanner sc) {
        if (muslitos <= 0) {
            System.out.println("estas muerto");
            return;
        }
        boolean volver = false;
        while (!volver) {
            System.out.println("\nAldeanos disponibles:");
            System.out.println("1. Herrero de armaduras");
            System.out.println("2. Herrero de herramientas");
            System.out.println("3. Granjero");
            System.out.println("4. Clerigo");
            System.out.println("5. Volver");
            int op = sc.nextInt();
            switch (op) {
                case 1:
                    new HerreroArmaduras().comprar(this, sc);
                    break;
                case 2:
                    new HerreroHerramientas().comprar(this, sc);
                    break;
                case 3:
                    new Granjero().comprar(this, sc);
                    break;
                case 4:
                    new Clerigo().comprar(this, sc);
                    break;
                case 5:
                    volver = true;
                    break;
                default:
                    System.out.println("no entendi");
            }
        }
    }

    public void minar(int nivel) {
        vecesMina++;
        Random r = new Random();
        boolean seguir = true;

        while (seguir) {
            if (muslitos <= 0) {
                System.out.println("te quedaste sin comida");
                break;
            }
            if (pico.getDurabilidad() <= 0) {
                System.out.println("se te rompio el pico");
                break;
            }

            int chance = r.nextInt(100);
            String mineral = null;

            if (nivel == 1) {
                if (chance < 20) mineral = "carbon";
                else if (chance < 25) mineral = "hierro";
                else if (chance < 26) mineral = "lapis";
            } else if (nivel == 2) {
                if (chance < 10) mineral = "carbon";
                else if (chance < 20) mineral = "hierro";
                else if (chance < 25) mineral = "lapis";
                else if (chance < 30) mineral = "redstone";
                else if (chance < 33) mineral = "oro";

                if (r.nextInt(100) < 10) {
                    double daño = 1 + r.nextInt(2);
                    muslitos -= daño;
                    System.out.println("aparecio un mob! perdiste " + daño + " muslitos");
                    if (muslitos <= 0) {
                        System.out.println("el mob te mato");
                        seguir = false;
                        continue;
                    }
                }
            } else {
                if (chance < 10) mineral = "hierro";
                else if (chance < 20) mineral = "lapis";
                else if (chance < 30) mineral = "redstone";
                else if (chance < 35) mineral = "oro";
                else if (chance < 38) mineral = "esmeralda";
                else if (chance < 41) mineral = "diamante";

                if (r.nextInt(100) < 15) {
                    double daño = 2 + r.nextInt(2);
                    muslitos -= daño;
                    System.out.println("aparecio un mob! perdiste " + daño + " muslitos");
                    if (muslitos <= 0) {
                        System.out.println("el mob te mato");
                        seguir = false;
                        continue;
                    }
                }
            }

            if (mineral == null) {
                System.out.println("rompiste piedra, nada interesante");
                pico.usar();
                continue;
            }

            if (!pico.puedeRomper(mineral)) {
                System.out.println("encontraste " + mineral + " pero tu pico no lo puede romper");
                pico.usar();
                continue;
            }

            int cantidad = 1;
            int orbes = 0;

            switch (mineral) {
                case "carbon":
                    cantidad = 3 + r.nextInt(3);
                    orbes = 4;
                    break;
                case "redstone":
                    cantidad = 4 + r.nextInt(3);
                    orbes = 6;
                    break;
                case "lapis":
                    cantidad = 4 + r.nextInt(3);
                    orbes = 6;
                    break;
                case "diamante":
                case "esmeralda":
                    cantidad = 1;
                    orbes = 8;
                    break;
                case "oro":
                case "hierro":
                    cantidad = 1;
                    orbes = 0;
                    break;
            }

            if (pico.getFortuna() > 0 && (mineral.equals("carbon") || mineral.equals("redstone") || mineral.equals("lapis") || mineral.equals("diamante") || mineral.equals("esmeralda"))) {
                double bonus = cantidad * 0.10 * pico.getFortuna();
                cantidad += (int) Math.ceil(bonus);
            }

            System.out.println("encontraste " + cantidad + " de " + mineral);
            agregarItem(mineral, cantidad);
            experiencia += orbes;

            boolean sigue = pico.usar();
            if (!sigue) {
                System.out.println("tu pico se rompio");
                seguir = false;
            }
        }
        encantar();
    }

    public void encantar() {
        Random r = new Random();
        if (r.nextInt(100) < 33 && experiencia >= 10) {
            int nivel = calcularNivelDisponible();
            int tipo = r.nextInt(3);

            switch (tipo) {
                case 0:
                    pico.aplicarIrrompibilidad(Math.min(3, nivel / 10 + 1));
                    System.out.println("tu pico se encanto con irrompibilidad nivel " + Math.min(3, nivel / 10 + 1));
                    break;
                case 1:
                    pico.aplicarFortuna(Math.min(3, nivel / 10 + 1));
                    System.out.println("tu pico se encanto con fortuna nivel " + Math.min(3, nivel / 10 + 1));
                    break;
                case 2:
                    pico.aplicarReparacion(experiencia / 2);
                    System.out.println("tu pico se encanto con reparacion");
                    break;
            }

            if (nivel >= 30 && r.nextInt(100) < 50) {
                int tipo2 = r.nextInt(3);
                if (tipo2 == 0) pico.aplicarIrrompibilidad(1);
                else if (tipo2 == 1) pico.aplicarFortuna(1);
                else pico.aplicarReparacion(5);
                System.out.println("ademas se le sumo otro encantamiento");
            }
        }
    }

    public int calcularNivelDisponible() {
        int nivel = 0;
        int orbesRestantes = experiencia;
        while (orbesRestantes > 0) {
            int costo;
            if (nivel < 10) costo = 5;
            else if (nivel < 20) costo = 10;
            else if (nivel < 30) costo = 15;
            else costo = (int) Math.pow(2, nivel - 30) * 15;

            if (orbesRestantes >= costo) {
                orbesRestantes -= costo;
                nivel++;
            } else {
                break;
            }
        }
        return nivel;
    }
}