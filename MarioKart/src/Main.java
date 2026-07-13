import java.util.Scanner;
import java.util.Random;
import java.util.ArrayList;
public class Main {
    public static void main(String[] args) throws InterruptedException {
        Scanner scanner = new Scanner(System.in);
        Random rand = new Random();

        Componente[] ruedas = {
                new Componente("Rueda Pequena", -1, -2, 2),
                new Componente("Rueda Mediana", 0, 1, 1),
                new Componente("Rueda Monstruo", 2, 2, 0)
        };

        Componente[] chasis = {
                new Componente("Chasis Liviano", -2, -2, 2),
                new Componente("Chasis Medio", 1, 0, 1),
                new Componente("Chasis Pesado", 2, 2, 0)
        };

        Pista[] pistas = {
                new Pista("Circuito Mario", 100, 2),
                new Pista("Senda Arcoiris", 150, 3),
                new Pista("Castillo de Bowser", 200, 3)
        };

        System.out.println("--- CONFIGURACION DE JUGADOR ---");
        System.out.print("Nombre del piloto: ");
        String nombreJugador = scanner.nextLine();

        System.out.print("Seleccione Vehiculo (1. Auto / 2. Moto): ");
        Vehiculo vJugador = (scanner.nextInt() == 2) ? new Moto() : new Auto();

        System.out.print("Seleccione Ruedas (1. Chica / 2. Mediana / 3. Monstruo): ");
        Componente rJugador = ruedas[scanner.nextInt() - 1];

        System.out.print("Seleccione Chasis (1. Liviano / 2. Medio / 3. Pesado): ");
        Componente cJugador = chasis[scanner.nextInt() - 1];

        System.out.print("Seleccione Pista (1. Mario / 2. Senda / 3. Castillo): ");
        Pista pistaElegida = pistas[scanner.nextInt() - 1];

        System.out.print("Seleccione Modo de Rivales (1. Aleatorio / 2. Clasico): ");
        int modoRival = scanner.nextInt();

        HiloReloj relojGlobal = new HiloReloj();
        ArrayList<Corredor> carrera = new ArrayList<>();
        Corredor.puestoActual = 1;

        carrera.add(new Corredor(nombreJugador, 40, 3, 1, vJugador, rJugador, cJugador, pistaElegida, relojGlobal));

        if (modoRival == 2) {
            carrera.add(new Corredor("Luigi", 39, 3, 2, new Auto(), ruedas[1], chasis[1], pistaElegida, relojGlobal));
            carrera.add(new Corredor("Peach", 42, 2, 3, new Moto(), ruedas[0], chasis[0], pistaElegida, relojGlobal));
            carrera.add(new Corredor("Bowser", 45, 5, 0, new Auto(), ruedas[2], chasis[2], pistaElegida, relojGlobal));
        } else {
            String[] nombres = {"Yoshi", "Toad", "Koopa"};
            for (String n : nombres) {
                Vehiculo vR = rand.nextBoolean() ? new Auto() : new Moto();
                Componente rR = ruedas[rand.nextInt(3)];
                Componente cR = chasis[rand.nextInt(3)];
                carrera.add(new Corredor(n, 38 + rand.nextInt(5), 2 + rand.nextInt(3), rand.nextInt(3), vR, rR, cR, pistaElegida, relojGlobal));
            }
        }

        System.out.println("\n--- INICIO DE LA COMPETENCIA ---\n");

        relojGlobal.start();
        for (Corredor c : carrera) {
            c.start();
        }

        for (Corredor c : carrera) {
            c.join();
        }

        relojGlobal.detenerReloj();
        relojGlobal.interrupt();

        System.out.println("\n=========================================");
        System.out.println("         RESULTADOS DE LA CARRERA        ");
        System.out.println("=========================================");

        carrera.sort((c1, c2) -> Integer.compare(c1.puestoLlegada, c2.puestoLlegada));

        System.out.println("GANADOR: " + carrera.get(0).nombre.toUpperCase() + "\n");

        System.out.println("--- ORDEN DE LLEGADA ---");
        for (Corredor c : carrera) {
            System.out.println(c.puestoLlegada + " Puesto: " + c.nombre + " - Tiempo total: " + c.tiempoFinal + "s");
        }

        System.out.println("\n--- CONFIGURACIONES FINALES ---");
        for (Corredor c : carrera) {
            System.out.println("- " + c.nombre + " | Vel. Final: " + c.velocidadFinal + "m/s | Estabilidad: " + c.estabilidadFinal + " | Drift: " + c.driftFinal);
        }
    }
}