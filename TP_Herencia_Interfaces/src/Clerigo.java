import java.util.Scanner;

public class Clerigo implements Vendedor {
    public void mostrarOfertas() {
        System.out.println("1. Vender 32 lapis x 2 esmeraldas");
        System.out.println("2. Vender 32 redstone x 1 esmeralda");
    }

    public void comprar(Jugador j, Scanner sc) {
        mostrarOfertas();
        int op = sc.nextInt();
        switch (op) {
            case 1:
                if (j.quitarItem("lapis", 32)) { j.esmeraldas += 2; System.out.println("vendido"); }
                else System.out.println("no tenes suficiente lapis");
                break;
            case 2:
                if (j.quitarItem("redstone", 32)) { j.esmeraldas += 1; System.out.println("vendido"); }
                else System.out.println("no tenes suficiente redstone");
                break;
            default:
                System.out.println("no entendi");
        }
    }
}