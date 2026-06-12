import java.util.Scanner;

public class Granjero implements Vendedor {
    public void mostrarOfertas() {
        System.out.println("1. 2 panes - 3 esmeraldas (recupera 2 muslitos)");
        System.out.println("2. 4 sandias - 1 esmeralda (recupera 0.5 cada una)");
        System.out.println("3. Zanahoria de oro - 5 esmeraldas (recupera 6 muslitos)");
    }

    public void comprar(Jugador j, Scanner sc) {
        mostrarOfertas();
        int op = sc.nextInt();
        switch (op) {
            case 1:
                if (j.gastarEsmeraldas(3)) j.muslitos = Math.min(10, j.muslitos + 2);
                break;
            case 2:
                if (j.gastarEsmeraldas(1)) j.muslitos = Math.min(10, j.muslitos + 2);
                break;
            case 3:
                if (j.gastarEsmeraldas(5)) j.muslitos = Math.min(10, j.muslitos + 6);
                break;
            default:
                System.out.println("no entendi");
        }
        System.out.println("muslitos actuales: " + j.muslitos);
    }
}