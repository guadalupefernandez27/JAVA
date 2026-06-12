import java.util.*;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        Jugador j = new Jugador();
        boolean salir = false;

        while (!salir) {
            System.out.println("\n1. Ir a la mina");
            System.out.println("2. Ir a la aldea");
            System.out.println("3. Dormir");
            System.out.println("4. Salir");
            int opc = sc.nextInt();

            switch (opc) {
                case 1:
                    System.out.println("Elegi nivel (1: 60-40, 2: 40-15, 3: 15-5)");
                    int nivel = sc.nextInt();
                    j.minar(nivel);
                    break;
                case 2:
                    j.aldea(sc);
                    break;
                case 3:
                    j.dormir();
                    break;
                case 4:
                    salir = true;
                    break;
                default:
                    System.out.println("opcion invalida");
            }

            if (j.muslitos <= 0) {
                System.out.println("te moriste, fin del juego");
                salir = true;
            }
        }
    }
}
